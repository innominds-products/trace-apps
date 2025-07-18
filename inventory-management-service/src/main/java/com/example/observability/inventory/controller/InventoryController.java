package com.example.observability.inventory.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.observability.inventory.service.InventoryMangementService;
import com.inm.tracewise.context.TraceWiseContextAware;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/v1/inventory")
@Slf4j
public class InventoryController {

	private final InventoryMangementService inventoryManagementService;

	public InventoryController(InventoryMangementService inventoryManagementService) {
		this.inventoryManagementService = inventoryManagementService;
	}
	
	@GetMapping("/inventory-management")
	public ResponseEntity<String> inventoryMaangement() {
		log.info("Inventory Service");
		log.info("Received Order Input :: {}");
		customerInfo();
		TraceWiseContextAware.supplyAsync(() -> customerInfo());
		return ResponseEntity.ok("Inventory called successfully");
	}
	
	private Object customerInfo() {
		inventoryManagementService.customerInfo();
		return null;
	}

}
