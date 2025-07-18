package com.example.observability.inventory.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.example.observability.inventory.model.dto.Billing;


@Service
@Slf4j
public class BillingService {

    private final BillingProcessingService orderProcessingService;

    public BillingService(BillingProcessingService orderProcessingService) {
        this.orderProcessingService = orderProcessingService;
    }

    public void placeOrder(Billing order) {
        log.info("Placing order : order id {}", order.id());
        orderProcessingService.processOrder(order);
    }

    public void updateOrderStatus(Billing order) {
        log.info("Updating order status : order id {}", order.id());
    }

    public void shipOrder() {
        // Ship order logic
    }

    public void deliverOrder() {
        // Deliver order logic
    }

    public void cancelOrder() {
        // Cancel order logic
    }
}
