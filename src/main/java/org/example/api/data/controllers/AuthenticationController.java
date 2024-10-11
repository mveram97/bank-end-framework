package org.example.api.data.controllers;

import org.example.api.data.entity.Customer;
import org.example.api.service.AuthService;
import org.example.api.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class AuthenticationController {

    private CustomerService customerService;
    private AuthService login;

   public AuthenticationController(AuthService login) {
        this.login = login;
    }

    public AuthenticationController(CustomerService customerService) {
        this.customerService = customerService;
    }
   public AuthenticationController(){

    }

    @PostMapping("/register")
    public String addCustomer(@RequestBody Customer nuevoCust){

          customerService.save(nuevoCust);
          return "The customer has registered succesfully";

    }

    @PostMapping("/logIn")
    public String logIn(@RequestBody Customer logInCust ){

        login.login(logInCust.getEmail(), logInCust.getPassword());
        return "The customer has loged in succesfully";

    }

    /*@PostMapping("/logOut")
    public String logOut(@RequestBody Customer nuevoCust){

        return "The customer has loged out succesfully";

    }*/

}
