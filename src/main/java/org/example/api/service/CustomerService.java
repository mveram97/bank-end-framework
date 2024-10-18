package org.example.api.service;

import org.example.api.data.entity.Account;
import org.example.api.data.entity.Customer;
import org.example.api.data.repository.AccountRepository;
import org.example.api.data.repository.CustomerRepository;
import org.example.apicalls.dto.AccountDTO;
import org.example.apicalls.dto.CustomerDTO;
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

  public Customer register(CustomerDTO customerDto){
    // verify if email exists
    if(customerRepository.existsByEmail(customerDto.getEmail())){
      throw new IllegalArgumentException("Email already registered.");
    }
    Customer customer = convertCustomerDtoToEntity(customerDto);
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

  // Map Customer DTO to Customer Entity
  public Customer convertCustomerDtoToEntity(CustomerDTO dto) {
    Customer customer = new Customer();
    customer.setName(dto.getName());
    customer.setSurname(dto.getSurname());
    customer.setEmail(dto.getEmail());
    customer.setPassword(dto.getPassword());
    List<Account> accounts = Collections.emptyList();

    if (dto.getAccounts() == null) {
      dto.setAccounts(Collections.emptyList()); // Lo manejamos como una lista vacía
    }
    else {
      for (AccountDTO accountDto : dto.getAccounts()) {
        accounts.add(accountService.convertAccountDtoToEntity(accountDto));
      }
    }
    customer.setAccounts(accounts);
    return customer;
  }
}