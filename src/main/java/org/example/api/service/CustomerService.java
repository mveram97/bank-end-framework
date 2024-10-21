package org.example.api.service;

import org.example.api.data.entity.Account;
import org.example.api.data.entity.Customer;
import org.example.api.data.repository.AccountRepository;
import org.example.api.data.repository.CustomerRepository;
import org.example.apicalls..Account;
import org.example.apicalls..Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

  @Autowired private CustomerRepository customerRepository;
  @Autowired private AccountRepository accountRepository;
  @Autowired private AccountService accountService;

  public List<Customer> findAll() {
    return customerRepository.findAll();
  }

  public Optional<Customer> findById(Integer customerId) {
    return customerRepository.findById(customerId);
  }

  //public Customer register(Customer customer) { return customerRepository.save(customer);}

  public Customer register(Customer customer){
    // verify if email exists
    if(customerRepository.existsByEmail(customer.getEmail())){
      throw new IllegalArgumentException("Email already registered.");
    }
    Customer customer = convertCustomerToEntity(customer);
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

  // Map Customer  to Customer Entity
  public Customer convertCustomerToEntity(Customer ) {
    Customer customer = new Customer();
    customer.setName(.getName());
    customer.setSurname(.getSurname());
    customer.setEmail(.getEmail());
    customer.setPassword(.getPassword());
    List<Account> accounts = Collections.emptyList();

    if (.getAccounts() == null) {
      .setAccounts(Collections.emptyList()); // Lo manejamos como una lista vacía
    }
    else {
      for (Account account : .getAccounts()) {
        accounts.add(accountService.convertAccountToEntity(account));
      }
    }
    customer.setAccounts(accounts);
    return customer;
  }
}