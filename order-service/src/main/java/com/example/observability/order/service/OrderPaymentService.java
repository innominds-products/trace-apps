package com.example.observability.order.service;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.observability.order.model.OrderPayment;
import com.example.observability.order.model.dto.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderPaymentService {
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Value("${order.events.topic}")
	private String topic;

	public void processOrderPayments(Order order) {
		try {
			log.info("Processing order payment");
			// Serialize the Order object to JSON
			String orderJson = objectMapper.writeValueAsString(getOrderPayment(order));
			// Send the JSON payload to Kafka
			kafkaTemplate.send(topic, String.valueOf(order.id()), orderJson).exceptionally(ex -> {
				log.error("Send failed", ex);
				return null;
			});
			log.info("Submitted message to payment service client to process it asynchronously");

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	private OrderPayment getOrderPayment(Order order) {
		double amount = ThreadLocalRandom.current().nextDouble(10, 120);
		return new OrderPayment(order.id(), order.paymentToken(), amount);
	}
}
