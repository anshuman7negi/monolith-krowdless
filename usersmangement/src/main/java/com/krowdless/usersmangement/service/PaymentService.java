package com.krowdless.usersmangement.service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.krowdless.usersmangement.dto.PaymentVerifyRequest;
import com.krowdless.usersmangement.entity.StayBooking;
import com.krowdless.usersmangement.entity.StayBookingPrice;
import com.krowdless.usersmangement.entity.StayPayment;
import com.krowdless.usersmangement.repository.StayBookingPriceRepository;
import com.krowdless.usersmangement.repository.StayBookingRepository;
import com.krowdless.usersmangement.repository.StayPaymentRepository;

import jakarta.transaction.Transactional;

@Service
public class PaymentService {

    private final StayBookingRepository bookingRepository;
    private final StayBookingPriceRepository priceRepository;
    private final StayPaymentRepository paymentRepository;

    public PaymentService(
            StayBookingRepository bookingRepository,
            StayBookingPriceRepository priceRepository,
            StayPaymentRepository paymentRepository) {

        this.bookingRepository = bookingRepository;
        this.priceRepository = priceRepository;
        this.paymentRepository = paymentRepository;
    }

    // 🔹 SAME AS RAZORPAY create-order
    @Transactional
    public Map<String, Object> createOrder(Long bookingId) {

        StayBooking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!"PENDING".equals(booking.getBookingStatus())) {
            throw new RuntimeException("Booking already processed");
        }

        StayBookingPrice price = priceRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Price not found"));

        // 🔥 Fake order id (same as Razorpay order_id)
        String fakeOrderId = "demo_order_" + UUID.randomUUID();

        StayPayment payment = new StayPayment();
        payment.setBooking(booking);
        payment.setPaymentMethod("DEMO");
        payment.setPaymentStatus("INITIATED");
        payment.setAmount(price.getFinalAmount());
        payment.setTransactionId(fakeOrderId);

        paymentRepository.save(payment);

        // async success simulation
        simulatePaymentSuccessAsync(fakeOrderId);

        return Map.of(
                "orderId", fakeOrderId,
                "amount", price.getFinalAmount(),
                "currency", "INR");
    }

    // 🔹 SAME AS RAZORPAY verify
    @Transactional
    public void verifyPayment(PaymentVerifyRequest request) {

        StayPayment payment = paymentRepository
                .findByTransactionId(request.getRazorpayOrderId())
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (!"INITIATED".equals(payment.getPaymentStatus())) {
            return;
        }

        payment.setPaymentStatus("SUCCESS");
        paymentRepository.save(payment);

        StayBooking booking = payment.getBooking();
        booking.setBookingStatus("CONFIRMED");
        bookingRepository.save(booking);
    }

    // 🔹 DEMO async delay
    @Async
    public CompletableFuture<Void> simulatePaymentSuccessAsync(String orderId) {

        try {
            Thread.sleep(60_000); // 1 minute
        } catch (InterruptedException ignored) {
        }

        PaymentVerifyRequest req = new PaymentVerifyRequest();
        req.setRazorpayOrderId(orderId);

        verifyPayment(req);

        return CompletableFuture.completedFuture(null);
    }
}
