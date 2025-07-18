package com.example.observability.inventory.model.dto;

import java.util.List;

import com.example.observability.inventory.model.BillingStatus;

public record Billing(Long id, Long customerId, List<BillingItem> items, String paymentToken, BillingStatus orderStatus) {
}

