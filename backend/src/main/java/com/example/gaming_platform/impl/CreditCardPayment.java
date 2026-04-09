package com.example.gaming_platform.impl;

import com.example.gaming_platform.Payment;
import com.example.gaming_platform.entity.PaymentResponse;
import java.util.Calendar;
import java.util.UUID;
import java.util.regex.Pattern;

public class CreditCardPayment implements Payment {
    private String cardNumber;
    private String cardHolderName;
    private String expiryDate;
    private String cvv;

    @Override
    public boolean validatePaymentDetails() {
        return isValidCardNumber() && isValidCVV() && isValidExpiry();
    }

    @Override
    public PaymentResponse processPayment(float amount) {
        if (!validatePaymentDetails()) {
            return new PaymentResponse(false, null, "Invalid credit card details", Calendar.getInstance());
        }
        
        // Simulate payment processing
        String transactionId = "CC_" + UUID.randomUUID().toString();
        return new PaymentResponse(true, transactionId, 
            "Credit card payment processed successfully", Calendar.getInstance());
    }

    @Override
    public String getPaymentMethod() {
        return "CREDIT_CARD";
    }

    public void setCardNumber(String newCardNumber){this.cardNumber = newCardNumber;}
    public void setCardHolderName(String newCardHolderName){this.cardHolderName = newCardHolderName;}
    public void setExpiryDate(String newExpiryDate){this.expiryDate = newExpiryDate;}
    public void setCVV(String newCVV){this.cvv = newCVV;}

    private boolean isValidCardNumber() {
        return cardNumber != null && cardNumber.replaceAll("\\s", "").length() == 16;
    }

    private boolean isValidCVV() {
        return cvv != null && Pattern.matches("\\d{3,4}", cvv);
    }

    private boolean isValidExpiry() {
        return expiryDate != null && Pattern.matches("\\d{2}/\\d{2}", expiryDate);
    }
}