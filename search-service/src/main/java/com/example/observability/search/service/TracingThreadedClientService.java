package com.example.observability.search.service;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import io.micrometer.context.ContextSnapshot;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import io.swagger.v3.oas.models.PathItem.HttpMethod;
import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
public class TracingThreadedClientService {

	 private final RestTemplate restTemplate;
	    private final Tracer tracer;

	    public TracingThreadedClientService(RestTemplateBuilder builder, Tracer tracer) {
	        this.restTemplate = builder.build();
	        this.tracer = tracer;
	    }
	    ExecutorService executor = Executors.newFixedThreadPool(3);
	    public void callServiceBInMultipleThreads() {
	     
	        ContextSnapshot snapshot = ContextSnapshot.captureAll();

	        for (int i = 1; i <= 3; i++) {
	            int taskNum = i;
	            executor.submit(() -> {
	                snapshot.setThreadLocals(); // Restore context in thread

	                Span span = tracer.nextSpan().name("thread-" + taskNum + "-call").start();
	                try (Tracer.SpanInScope scope = tracer.withSpan(span)) {
	                    String response = restTemplate.getForObject("http://localhost:8084/api/v1/customer/process", String.class);
	                    System.out.println("Thread " + taskNum + " got response: " + response);
	                } finally {
	                    span.end();
	                }
	            });
	        }

	        executor.shutdown();
	    }
	    
	    
	    
	    public String callInventoryManagement() { 
	    	
	    	log.info("Asynchronous messaage for Search and Inventory");
	    	
	    	Span currentSpan = tracer.currentSpan();
	        TraceContext context = currentSpan.context();

	        executor.submit(() -> {
	            // Important: Manually propagate the span
	            try (Tracer.SpanInScope ws = tracer.withSpan(currentSpan)) {
	                // Propagate context manually
	                HttpHeaders headers = new HttpHeaders();
	                headers.add("X-B3-TraceId", context.traceId());
	                headers.add("X-B3-SpanId", context.spanId());
	                headers.add("X-B3-Sampled", "1");

	                HttpEntity<String> request = new HttpEntity<>(headers);

	                restTemplate.getForObject("http://localhost:8085/api/v1/inventory/inventory-management", String.class);
	            }
	        });
	        executor.shutdown();
	         return "Search Service inventory responded";
    }
	public void callCustomer() {
	    	log.info("Calling customer service");
	        String response =  restTemplate.getForObject("http://localhost:8084/api/v1/customer/search", String.class);
	        log.info("Customer response ",response);
	               
	    }
	}

