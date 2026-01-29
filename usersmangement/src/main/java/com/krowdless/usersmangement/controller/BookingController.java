package com.krowdless.usersmangement.controller;

import com.krowdless.usersmangement.service.BookingService;
import com.krowdless.usersmangement.util.SecurityUtil;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createBooking(
            @RequestParam Long stayId,
            @RequestParam String checkInDate,
            @RequestParam String checkOutDate,
            @RequestParam int guests,
            @RequestParam double pricePerNight) {

        Long userId = SecurityUtil.getCurrentUserId();

        Long bookingId = bookingService.createBooking(
                stayId,
                userId,
                LocalDate.parse(checkInDate),
                LocalDate.parse(checkOutDate),
                guests,
                pricePerNight);

        return ResponseEntity.ok(
                Map.of(
                        "message", "Booking created successfully",
                        "bookingId", bookingId));
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyBookings(
            @RequestParam Long userId) {
        return ResponseEntity.ok(
                bookingService.getBookingsForUser(userId));
    }

}
