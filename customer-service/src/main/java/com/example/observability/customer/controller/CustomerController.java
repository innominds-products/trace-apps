package com.example.observability.customer.controller;

import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.observability.customer.service.CustomerService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/v1/customer")
@Slf4j
public class CustomerController {

	private final CustomerService customerService;

	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}
	
	@GetMapping("/search")
	public ResponseEntity<String> searchCustomer() {
		log.info(" Enter into the customer service search call");
		customerService.searchCustomer();
		return ResponseEntity.ok("List of customer");
	}
	

    @GetMapping("/process")
    public String process() throws InterruptedException {
    	log.info("Enter into the thread process for customer service");
        Thread.sleep(5300); // Simulate processing
        return "Processed by customer" + Instant.now();
    }
}
