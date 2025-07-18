package com.example.observability.customer.service;

import org.springframework.stereotype.Service;

import com.example.observability.customer.model.dto.Customer;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class CustomerService {

    public void searchCustomer() {
        log.info("Customer Search}");
        Customer customer= new Customer(101L,"test","New york");
        log.info("Customer  :",customer.toString());
    }     
}
