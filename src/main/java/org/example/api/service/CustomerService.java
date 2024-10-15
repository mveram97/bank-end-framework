package org.example.api.service;

import org.example.api.data.entity.Account;
import org.example.api.data.entity.Customer;
import org.example.api.data.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

  @Autowired private CustomerRepository customerRepository;

  public List<Customer> findAll() {
    return customerRepository.findAll();
  }

  public Optional<Customer> findById(Integer customerId) {
    return customerRepository.findById(customerId);
  }

  public Customer register(Customer customer) {
    return customerRepository.save(customer);
  }

  public void deleteById(Integer customerId) {
    customerRepository.deleteById(customerId);
  }

  public Optional<Customer> findByEmail(String email) {
    return customerRepository.findByEmail(email);
  }

  @Transactional
  public boolean deleteByEmail(String email) {
    if (customerRepository.existsByEmail(email)) {
      System.out.println("Eliminando cliente con email: " + email);
      customerRepository.deleteByEmail(email);
      return true;
    } else {
      System.out.println("Cliente no encontrado con email: " + email);
      return false;
    }
  }
  public Optional<Customer> findByPassword(String password){
    return customerRepository.findByPassword(password);
  }
}