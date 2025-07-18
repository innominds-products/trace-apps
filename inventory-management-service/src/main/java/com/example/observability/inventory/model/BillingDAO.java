package com.example.observability.inventory.model;

import java.time.LocalDateTime;
import java.util.List;

import com.example.observability.inventory.model.dto.BillingItem;

public record BillingDAO(Long orderId, Long customerId, List<BillingItem> orderItems, BillingStatus status, LocalDateTime createdDate, LocalDateTime updatedDate) {
}
