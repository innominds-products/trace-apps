package com.example.observability.order.service;

import org.springframework.stereotype.Service;

import com.example.observability.order.model.dto.Order;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderService {

	private final OrderProcessingService orderProcessingService;

	public OrderService(OrderProcessingService orderProcessingService) {
		this.orderProcessingService = orderProcessingService;
	}

	public void placeOrder(Order order) {
		log.info("Placing order : order id {}", order.id());
		orderProcessingService.processInventory();
		orderProcessingService.processOrder(order);
		
	}
	
	public void placeInventory()
	{
		log.info("Place inventory");
		orderProcessingService.processInventory();
	}

	public void updateOrderStatus(Order order) {
		log.info("Updating order status : order id {}", order.id());
//		shipOrder(order);
	}

	public void shipOrder(Order order) {
		log.info("Order is being shipped : order id {}", order.id());
	}

	public void deliverOrder() {
		// Deliver order logic
	}

	public void cancelOrder() {
		// Cancel order logic
	}
}
