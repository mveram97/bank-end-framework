package org.example.api.data.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.example.api.data.entity.Customer;
import org.example.api.service.AuthService;
import org.example.api.service.CustomerService;
import org.example.api.token.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
public class CustomerController {
  @Autowired
  private Token token;
  @Autowired
  AuthService authService;

  private final CustomerService customer;

  public CustomerController(CustomerService customer) {
    this.customer = customer;
  }

  @GetMapping("/customer/{id}")
  public Optional<Customer> customer(@PathVariable Integer id) {

    return customer.findById(id);
  }

  @GetMapping("/public/customer")
  public List<Customer> customer(HttpServletRequest request){
    String jwt = authService.getJwtFromCookies(request);

    if (jwt == null || !token.validateToken(jwt)){
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Cookie no válida");
    }

    return customer.findAll();

  }
}
