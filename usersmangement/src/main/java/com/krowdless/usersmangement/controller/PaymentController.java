package com.krowdless.usersmangement.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.krowdless.usersmangement.dto.PaymentVerifyRequest;
import com.krowdless.usersmangement.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // 🔹 Step 1: Create Razorpay Order
   // 🔹 Step 1: Create Order (DEMO now, Razorpay later)
    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestParam Long bookingId) {
        return ResponseEntity.ok(paymentService.createOrder(bookingId));
    }

    // 🔹 Step 2: Verify Payment (DEMO now, Razorpay later)
    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody PaymentVerifyRequest request) {
        paymentService.verifyPayment(request);
        return ResponseEntity.ok(Map.of("message", "Payment verified successfully"));
    }
}

