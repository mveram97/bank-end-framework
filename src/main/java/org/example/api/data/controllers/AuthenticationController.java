package org.example.api.data.controllers;

import org.example.api.data.entity.Customer;
import org.example.api.service.AuthService;
import org.example.api.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

@RestController
public class AuthenticationController {

    private CustomerService customerService;

    public AuthenticationController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public String addCustomer(@RequestBody Customer nuevoCust){
       try {
           customerService.save(nuevoCust);
           return "The customer has registered succesfully";
       } catch (Exception e){
           return "Failed to register customer: Invalid email";
       }
    }

    @PostMapping("/logIn")
    public String logIn(@RequestBody Customer logInCust ){

        return customerService.login(logInCust.getEmail(), logInCust.getPassword());
        //return "The customer has loged in succesfully";
    }

    /*@PostMapping("/logOut")
    public String logOut(@RequestBody Customer nuevoCust){

        return "The customer has loged out succesfully";

    }*/

}
