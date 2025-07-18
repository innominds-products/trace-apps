package com.example.observability.inventory.service;

import com.example.observability.inventory.model.BillingDAO;
import com.example.observability.inventory.model.BillingStatus;
import com.example.observability.inventory.model.dto.Billing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class BillingProcessingService {

    @Autowired
    BillingPaymentService orderPaymentService;

    public void processOrder(Billing order) {
        validateOrder(order);
        persistOrder(order);
        orderPaymentService.processOrderPayments(order);
    }


    private void persistOrder(Billing order) {
        BillingDAO orderDAO = new BillingDAO(order.id(), order.customerId(), order.items(), BillingStatus.PLACED, LocalDateTime.now(), LocalDateTime.now());
        log.info("Persisting order details for order : order id {}", order.id());
    }

    private void validateOrder(Billing order) {
        log.info("validating order details for order {}", order.id());
    }
}
