package com.example.observability.inventory.service;

import com.example.observability.inventory.model.BillingPayment;
import com.example.observability.inventory.model.dto.Billing;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class BillingPaymentService {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void processOrderPayments(Billing order) {
        try {
            // Serialize the Order object to JSON
            String orderJson = objectMapper.writeValueAsString(getOrderPayment(order));

            // Send the JSON payload to Kafka
            kafkaTemplate.send("order-events", String.valueOf(order.id()), orderJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private BillingPayment getOrderPayment(Billing order){
        // TODO: generate actual amount wrt to an inventory
        double amount = ThreadLocalRandom.current().nextDouble(10, 120);
        return new BillingPayment(order.id(),order.paymentToken(), amount);
    }
}
