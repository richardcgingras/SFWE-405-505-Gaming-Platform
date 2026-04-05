package com.example.gaming_platform.entity;

import java.time.LocalDateTime;

public class PaymentResponse {
    private boolean success;
    private String transactionId;
    private String message;
    private LocalDateTime timestamp;

    public PaymentResponse(boolean success, String transactionId, String message, LocalDateTime timestamp) {
        this.success = success;
        this.transactionId = transactionId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}