package com.krowdless.usersmangement.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.krowdless.usersmangement.entity.StayAvailability;
import com.krowdless.usersmangement.entity.StayBooking;
import com.krowdless.usersmangement.entity.StayBookingPrice;
import com.krowdless.usersmangement.repository.StayAvailabilityRepository;
import com.krowdless.usersmangement.repository.StayBookingPriceRepository;
import com.krowdless.usersmangement.repository.StayBookingRepository;
import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService {

    private final StayBookingRepository bookingRepository;
    private final StayBookingPriceRepository priceRepository;
    private final StayAvailabilityRepository availabilityRepository;

    public BookingService(
            StayBookingRepository bookingRepository,
            StayBookingPriceRepository priceRepository,
            StayAvailabilityRepository availabilityRepository) {
        this.bookingRepository = bookingRepository;
        this.priceRepository = priceRepository;
        this.availabilityRepository = availabilityRepository;
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

    public List<StayBooking> getBookingsForUser(Long userId) {
        return bookingRepository.findByGuestUserIdOrderByCreatedAtDesc(userId);
    }

}
