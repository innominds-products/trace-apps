package com.example.observability.inventory.service;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InventoryMangementService {

	private final InventoryManagementProcessing inventoryManagementProcessing;

	public InventoryMangementService(InventoryManagementProcessing inventoryManagementProcessing) {
		this.inventoryManagementProcessing = inventoryManagementProcessing;
	}

	public void customerInfo() {
		log.info("CustomerInfo ");
		inventoryManagementProcessing.fetchCustomer();
	}

	

}
