package com.example.gaming_platform;

import com.example.gaming_platform.entity.PaymentResponse;

public interface Payment {
    boolean validatePaymentDetails();
    PaymentResponse processPayment(float amount);
    String getPaymentMethod();
}