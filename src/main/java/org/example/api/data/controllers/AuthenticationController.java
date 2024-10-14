package org.example.api.data.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.api.data.entity.Customer;
import org.example.api.data.request.LoginRequest;
import org.example.api.service.AuthService;
import org.example.api.service.CustomerService;
import org.example.api.token.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
public class AuthenticationController {
    @Autowired
    private AuthService authService;
    @Autowired
    private Token token;

    private final CustomerService customerService;

    private final String jwtCookie = "cookieToken";

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


    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {

        String jwt = authService.getJwtFromCookies(request);


        if (jwt == null || !token.validateToken(jwt)){
             return ResponseEntity.badRequest().body("You have to Log In first");

        }
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, "")
                .path("/public").maxAge(0).httpOnly(true).build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Logged out successfully. Cookies cleared.");
    }
}
