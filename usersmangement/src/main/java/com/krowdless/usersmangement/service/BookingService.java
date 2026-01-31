package com.krowdless.usersmangement.service;

import jakarta.transaction.Transactional;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Service;

import com.krowdless.usersmangement.dto.BookingDetailDto;
import com.krowdless.usersmangement.dto.BookingListDto;
import com.krowdless.usersmangement.entity.Stay;
import com.krowdless.usersmangement.entity.StayAvailability;
import com.krowdless.usersmangement.entity.StayBooking;
import com.krowdless.usersmangement.entity.StayBookingPrice;
import com.krowdless.usersmangement.entity.StayMedia;
import com.krowdless.usersmangement.entity.StayPricing;
import com.krowdless.usersmangement.repository.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookingService {

    private final StayPaymentRepository stayPaymentRepository;

    private final StayMediaRepository stayMediaRepository;

    private final StayBookingRepository bookingRepository;
    private final StayBookingPriceRepository priceRepository;
    private final StayAvailabilityRepository availabilityRepository;
    private final StayRepository stayRepository;
    private final StayPricingRepository stayPricingRepository;
    private final UserRepository userRepository;

    public BookingService(
            StayBookingRepository bookingRepository,
            StayBookingPriceRepository priceRepository,
            StayAvailabilityRepository availabilityRepository,
            StayRepository stayRepository, StayMediaRepository stayMediaRepository,
            UserRepository userRepository, StayPaymentRepository stayPaymentRepository,
            StayPricingRepository stayPricingRepository) {
        this.bookingRepository = bookingRepository;
        this.priceRepository = priceRepository;
        this.availabilityRepository = availabilityRepository;
        this.stayRepository = stayRepository;
        this.stayMediaRepository = stayMediaRepository;
        this.userRepository = userRepository;
        this.stayPaymentRepository = stayPaymentRepository;
        this.stayPricingRepository = stayPricingRepository;
    }

    // 🔥 MAIN METHOD
    @Transactional
    public Long createBooking(
            Long stayId,
            Long userId,
            LocalDate checkIn,
            LocalDate checkOut,
            int guests,
            double pricePerNight) {

        // 1️⃣ Availability check (date overlap)
        boolean alreadyBooked = bookingRepository.existsOverlappingBooking(
                stayId, checkIn, checkOut);

        if (alreadyBooked) {
            throw new RuntimeException("Stay not available for selected dates");
        }

        // 2️⃣ Create booking (PENDING / CONFIRMED)
        StayBooking booking = new StayBooking();
        booking.setStayId(stayId);
        booking.setGuestUserId(userId);
        booking.setCheckInDate(checkIn);
        booking.setCheckOutDate(checkOut);
        booking.setGuestsCount(guests);
        booking.setBookingStatus("CONFIRMED"); // aaj confirm, kal payment pe

        booking = bookingRepository.save(booking);

        // 3️⃣ Price calculation
        int nights = (int) (checkOut.toEpochDay() - checkIn.toEpochDay());
        double baseAmount = nights * pricePerNight;
        double serviceFee = baseAmount * 0.10;
        double taxes = baseAmount * 0.05;
        double finalAmount = baseAmount + serviceFee + taxes;

        StayBookingPrice price = new StayBookingPrice();
        price.setBooking(booking);
        price.setPricePerNight(pricePerNight);
        price.setNights(nights);
        price.setBaseAmount(baseAmount);
        price.setServiceFee(serviceFee);
        price.setTaxes(taxes);
        price.setFinalAmount(finalAmount);

        priceRepository.save(price);

        // 4️⃣ Lock availability dates
        lockDates(booking);

        return booking.getId();
    }

    // 🔒 Lock dates
    private void lockDates(StayBooking booking) {
        LocalDate date = booking.getCheckInDate();

        while (date.isBefore(booking.getCheckOutDate())) {
            StayAvailability availability = new StayAvailability();
            availability.setStayId(booking.getStayId());
            availability.setBookingDate(date);
            availability.setStatus("BOOKED");
            availability.setBooking(booking);

            availabilityRepository.save(availability);
            date = date.plusDays(1);
        }
    }

    /* ================= LIST ================= */
    public List<BookingListDto> getMyBookingList(Long userId) {

        List<StayBooking> bookings = bookingRepository.findByGuestUserIdOrderByCreatedAtDesc(userId);

        if (bookings.isEmpty()) {
            return List.of();
        }

        // 1️⃣ collect stayIds
        List<Long> stayIds = bookings.stream()
                .map(StayBooking::getStayId)
                .distinct()
                .toList();

        // 2️⃣ fetch all images in ONE query
        List<StayMedia> mediaList = stayMediaRepository.findByStayIdInAndMediaTypeOrderBySortOrderAsc(
                stayIds,
                "IMAGE");

        // 3️⃣ map stayId -> first image only
        Map<Long, String> stayCoverImageMap = new HashMap<>();
        for (StayMedia media : mediaList) {
            stayCoverImageMap.putIfAbsent(
                    media.getStayId(),
                    media.getMediaUrl());
        }

        // 4️⃣ build DTO list
        return bookings.stream()
                .map(b -> {

                    Stay stay = stayRepository
                            .findById(b.getStayId())
                            .orElseThrow();

                    StayBookingPrice price = priceRepository
                            .findById(b.getId())
                            .orElseThrow();

                    BookingListDto dto = new BookingListDto();
                    dto.setBookingId(b.getId());
                    dto.setStayId(stay.getId());
                    dto.setStayTitle(stay.getTitle());
                    dto.setStayLocation(stay.getFullAddress());

                    // ✅ ONLY ONE COVER IMAGE
                    dto.setStayImageUrl(
                            stayCoverImageMap.get(b.getStayId()));

                    dto.setCheckInDate(b.getCheckInDate());
                    dto.setCheckOutDate(b.getCheckOutDate());
                    dto.setGuests(b.getGuestsCount());
                    dto.setNights(price.getNights());
                    dto.setTotalAmount(price.getFinalAmount());
                    dto.setBookingStatus(b.getBookingStatus());

                    return dto;
                })
                .toList();
    }

    /* ================= DETAIL ================= */
    public BookingDetailDto getBookingDetail(Long bookingId, Long userId) {

        // 1️⃣ Booking ownership check
        StayBooking booking = bookingRepository
                .findByIdAndGuestUserId(bookingId, userId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // 2️⃣ Stay
        Stay stay = stayRepository
                .findById(booking.getStayId())
                .orElseThrow(() -> new RuntimeException("Stay not found"));

        // 3️⃣ Price
        StayBookingPrice price = priceRepository
                .findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Price not found"));

        // 4️⃣ Stay pricing (optional)
        StayPricing pricing = stayPricingRepository
                .findById(stay.getId())
                .orElse(null);

        // 5️⃣ Cover image (first IMAGE)
        String coverImage = stayMediaRepository
                .findByStayIdInAndMediaTypeOrderBySortOrderAsc(
                        List.of(stay.getId()),
                        "IMAGE")
                .stream()
                .findFirst()
                .map(StayMedia::getMediaUrl)
                .orElse(null);

        BookingDetailDto dto = new BookingDetailDto();

        // ===== BOOKING =====
        dto.setBookingId(booking.getId());
        dto.setBookingStatus(booking.getBookingStatus());
        dto.setBookedAt(booking.getCreatedAt());

        // ===== STAY =====
        dto.setStayId(stay.getId());
        dto.setStayTitle(stay.getTitle());
        dto.setStayAddress(stay.getFullAddress());
        dto.setPropertyType(stay.getPropertyType());
        dto.setStayImageUrl(coverImage);

        // ===== DATES =====
        dto.setCheckInDate(booking.getCheckInDate());
        dto.setCheckOutDate(booking.getCheckOutDate());
        dto.setGuests(booking.getGuestsCount());
        dto.setNights(price.getNights());

        if (pricing != null) {
            dto.setCheckInTime(pricing.getCheckInTime());
            dto.setCheckOutTime(pricing.getCheckOutTime());
        }

        // ===== PRICE =====
        dto.setPricePerNight(price.getPricePerNight());
        dto.setBaseAmount(price.getBaseAmount());
        dto.setServiceFee(price.getServiceFee());
        dto.setTaxes(price.getTaxes());
        dto.setFinalAmount(price.getFinalAmount());

        // ===== PAYMENT =====
        stayPaymentRepository
                .findTopByBookingIdOrderByCreatedAtDesc(bookingId)
                .ifPresent(p -> {
                    dto.setPaymentStatus(p.getPaymentStatus());
                    dto.setPaymentMethod(p.getPaymentMethod());
                    dto.setTransactionId(p.getTransactionId());
                });

        return dto;
    }

}