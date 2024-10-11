package org.example.api.service;

import org.example.api.data.entity.Account;
import org.example.api.data.entity.Customer;
import org.example.api.data.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

  @Autowired private CustomerRepository customerRepository;
  @Autowired private AuthService authService;

  public List<Customer> findAll() {
    return customerRepository.findAll();
  }

  public Optional<Customer> findById(Integer customerId) {
    return customerRepository.findById(customerId);
  }

  public Customer save(Customer customer) {
    return customerRepository.save(customer);
  }

  public String login(String email, String password){
    try {
      Optional<Customer> customerByEmail = findByEmail(email);
      Optional<Customer> customerByPassword = findByPassword(password);
      return authService.login(customerByEmail, customerByPassword);
    } catch (RuntimeException e) {
      return "Invalid credentials";
    }
  }

  public void deleteById(Integer customerId) {
    customerRepository.deleteById(customerId);
  }

  public Optional<Customer> findByEmail(String email) {
    return customerRepository.findByEmail(email);
  }

  public Optional<Customer> findByPassword(String password){
    return customerRepository.findByPassword(password);
  }
}