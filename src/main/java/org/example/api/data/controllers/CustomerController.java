package org.example.api.data.controllers;

import org.example.api.data.entity.Customer;
import org.example.api.service.CustomerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class CustomerController {
    private CustomerService customer;

    public CustomerController(CustomerService customer) {
        this.customer = customer;
    }

    @GetMapping("/customer/{id}")
    public Optional<Customer> customer(@PathVariable Integer id){
        return customer.findById(id);

    }

    @GetMapping("/customer")
    public List<Customer> customer(){
        return customer.findAll();
    }
}

