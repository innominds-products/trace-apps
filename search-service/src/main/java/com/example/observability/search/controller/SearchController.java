package com.example.observability.search.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.observability.search.service.TracingThreadedClientService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/v1/search")
@Slf4j
public class SearchController {


 private final TracingThreadedClientService clientService;

	    public SearchController(TracingThreadedClientService clientService) {
	        this.clientService = clientService;
	    }

	    @GetMapping("/trigger")
	    public String triggerAsyncCalls() {
	    log.info("Test for multiple threads");
	        clientService.callServiceBInMultipleThreads();
	        log.info("End of Thread");
	        return "Triggered multithreaded calls to Service B";
	    }

    @GetMapping("/inventory")
    public String callInventoryManagement() {
      //  clientService.callServiceBInMultipleThreads();
    	log.info("Search for inventory");
    	 clientService.callInventoryManagement();
        return "Return Inventory Management";
    }
    
    @GetMapping("/customer")
    public String callCustomer() {
      //  clientService.callServiceBInMultipleThreads();
    	log.info("Search for customer");
    	 clientService.callCustomer();
        return "Customer displayed";
    }

}
