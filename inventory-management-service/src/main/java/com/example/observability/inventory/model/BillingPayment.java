package com.example.observability.inventory.model;

public record BillingPayment(Long id,
                           String paymentToken,
                           double amount) {
}
