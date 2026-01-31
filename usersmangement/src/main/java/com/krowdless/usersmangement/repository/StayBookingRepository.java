package com.krowdless.usersmangement.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.krowdless.usersmangement.entity.StayBooking;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StayBookingRepository
        extends JpaRepository<StayBooking, Long> {

    @Query("""
        SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END
        FROM StayBooking b
        WHERE b.stayId = :stayId
          AND b.bookingStatus = 'CONFIRMED'
          AND b.checkInDate < :checkOut
          AND b.checkOutDate > :checkIn
    """)
    boolean existsOverlappingBooking(
            Long stayId,
            LocalDate checkIn,
            LocalDate checkOut
    );

    List<StayBooking> findByGuestUserIdOrderByCreatedAtDesc(Long guestUserId);

     Optional<StayBooking> findByIdAndGuestUserId(
            Long id,
            Long guestUserId
    );

}


