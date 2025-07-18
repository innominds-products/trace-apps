package com.example.observability.inventory.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.inm.tracewise.context.TraceWiseContextAware;
import com.inm.tracewise.context.TraceWiseContextAware.TraceWiseContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service

public class InventoryManagementProcessing {

	private final RestTemplate restTemplate;
	 
	@Value("${api.url.customer}")
	private String url;
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build(); // trace context auto-propagated
    }
    
    public InventoryManagementProcessing(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();

    }

	public void fetchCustomer() {	
		log.info("customerInfo");
		fetchCustomerThread();
		
	}

	private void fetchCustomerThread() {
		TraceWiseContext traceContext = TraceWiseContextAware.setTraceContext();
		(new Thread(() -> {
			traceContext.set();
			log.info("New Thread");
			fetchCustomerInfo();
			traceContext.clear();
		})).start();
	}



	private void fetchCustomerInfo() {        
		restTemplate.getForObject(url+"/search", String.class);
		log.info("Fetch customer information ");
	}
	
	
}

