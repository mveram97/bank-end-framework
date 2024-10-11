package org.example.api.data.controllers;

import org.example.api.data.entity.Customer;
import org.example.api.data.request.LoginRequest;
import org.example.api.service.AuthService;
import org.example.api.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class AuthenticationController {
    @Autowired
    private AuthService authService;

    private final CustomerService customerService;

    public AuthenticationController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public String addCustomer(@RequestBody Customer nuevoCust){
       try {
           customerService.register(nuevoCust);
           return "The customer has registered succesfully";
       } catch (Exception e){
           return "Failed to register customer: Invalid email";
       }


    }

    @PostMapping("/logIn")
    public ResponseEntity<String> logIn(@RequestBody LoginRequest logInRequest){
        if (authService.authenticate(logInRequest.getEmail(), logInRequest.getPassword())){
            ResponseCookie jwtCookie = authService.generateJwtCookie(logInRequest);
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body("Correct authentication");
        }
        return ResponseEntity.badRequest().body("Invalid credentials");
    }

    /*@PostMapping("/logOut")
    public String logOut(@RequestBody Customer nuevoCust){

        return "The customer has loged out succesfully";

    }*/

}
