package com.krowdless.controller;

import com.krowdless.service.BookingService;
import com.krowdless.util.SecurityUtil;

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
            @RequestParam int guests) {

        Long userId = SecurityUtil.getCurrentUserId();

        Long bookingId = bookingService.createBooking(
                stayId,
                userId,
                LocalDate.parse(checkInDate),
                LocalDate.parse(checkOutDate),
                guests);

        return ResponseEntity.ok(
                Map.of(
                        "message", "Booking created successfully",
                        "bookingId", bookingId));
    }

    // LIST
    @GetMapping("/my")
    public ResponseEntity<?> myBookings() {
        Long userId = SecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(
                bookingService.getMyBookingList(userId));
    }

        // DETAIL
    @GetMapping("/{bookingId}")
    public ResponseEntity<?> bookingDetail(
            @PathVariable Long bookingId) {

        Long userId = SecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(
                bookingService.getBookingDetail(bookingId, userId));
    }

}
