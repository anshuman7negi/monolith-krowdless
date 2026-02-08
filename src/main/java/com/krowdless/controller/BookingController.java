package com.krowdless.controller;

import com.krowdless.entity.UserEntity;
import com.krowdless.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    	
    	Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) authentication.getPrincipal();
        Long userId = user.getId();

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
    	Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        UserEntity user = (UserEntity) authentication.getPrincipal();

        Long userId = user.getId();
        return ResponseEntity.ok(
                bookingService.getMyBookingList(userId));
    }

        // DETAIL
    @GetMapping("/{bookingId}")
    public ResponseEntity<?> bookingDetail(
            @PathVariable Long bookingId) {

    	Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        UserEntity user = (UserEntity) authentication.getPrincipal();

        Long userId = user.getId();
        return ResponseEntity.ok(
                bookingService.getBookingDetail(bookingId, userId));
    }

}
