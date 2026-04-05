package com.example.gaming_platform.impl;

import com.example.gaming_platform.Payment;
import com.example.gaming_platform.entity.PaymentResponse;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Pattern;

public class GiftCardPayment implements Payment {
    private String cardNumber;
    private String secretCode;

    @Override
    public boolean validatePaymentDetails() {
        return isValidCardNumber() && isValidSecretCode();
    }

    @Override
    public PaymentResponse processPayment(float amount) {
        if (!validatePaymentDetails()) {
            return new PaymentResponse(false, null, "Invalid gift card details", LocalDateTime.now());
        }
        
        // Simulate payment processing
        String transactionId = "GC_" + UUID.randomUUID().toString();
        return new PaymentResponse(true, transactionId, 
            "Gift card payment processed successfully", LocalDateTime.now());
    }

    @Override
    public String getPaymentMethod() {
        return "GIFT_CARD";
    }

    public void setCardNumber(String newCardNumber){this.cardNumber = newCardNumber;}
    public void setSecretCode(String newSecretCode){this.secretCode = newSecretCode;}

    private boolean isValidCardNumber() {
        return cardNumber != null && cardNumber.replaceAll("\\s", "").length() == 12;
    }

    private boolean isValidSecretCode() {
        return secretCode != null && Pattern.matches("\\d{4}", secretCode);
    }
}