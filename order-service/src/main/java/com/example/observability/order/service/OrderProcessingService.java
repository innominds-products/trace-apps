package com.example.observability.order.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.observability.order.model.OrderDAO;
import com.example.observability.order.model.OrderStatus;
import com.example.observability.order.model.dto.Order;
//import com.inm.tracewise.context.TraceWiseContextAware;
//import com.inm.tracewise.context.TraceWiseContextAware.TraceWiseContext;

//import io.micrometer.tracing.Tracer;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderProcessingService {

	@Autowired
	OrderPaymentService orderPaymentService;
	
	private final RestTemplate restTemplate;
	
	
	@Value("${ap.url.inventory}")
	private String url;
	 
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build(); // trace context auto-propagated
    }
    
    public OrderProcessingService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();

    }

	public void processOrder(Order order) {
		validateOrder(order);
        persistOrder(order);
//		persistOrderNewThread(order);
		orderPaymentService.processOrderPayments(order);
	}
	
	public void processInventory()
	{
		log.info("Process in processInventory");
//		persistInventoryRecommendNewThread();
	}

	/*
	 * private void persistOrderNewThread(Order order) { TraceWiseContext
	 * traceContext = TraceWiseContextAware.setTraceContext(); (new Thread(() -> {
	 * traceContext.set(); log.info("New Thread"); persistOrder(order);
	 * traceContext.clear(); })).start(); }
	 * 
	 * 
	 * private void persistInventoryRecommendNewThread() {
	 * log.info("Enter into the persistInventoryRecommendNewThread");
	 * TraceWiseContext traceContext = TraceWiseContextAware.setTraceContext(); (new
	 * Thread(() -> { traceContext.set(); log.info("New Thread");
	 * persistRecommend(); traceContext.clear(); })).start(); }
	 */
	private void persistOrder(Order order) {
		OrderDAO orderDAO = new OrderDAO(order.id(), order.customerId(), order.items(), OrderStatus.PLACED,
				LocalDateTime.now(), LocalDateTime.now());
		log.info("Persisting order details for order : order id {}", order.id());
	}

	private void persistRecommend() {
		restTemplate.getForObject(url+"/inventory-management", String.class);
		log.info("Persisting Inventory details");
	}
	
	
	private void validateOrder(Order order) {
		log.info("validating order details for order {}", order.id());
	}
}
