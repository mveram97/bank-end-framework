package org.example.apicalls.service;

import jakarta.servlet.http.HttpServletRequest;
import org.example.api.data.controllers.AccountController;
import org.example.api.data.controllers.AuthenticationController;
import org.example.api.data.controllers.CardController;
import org.example.api.data.controllers.TransferController;
import org.example.api.data.entity.Account;
import org.example.api.data.entity.Customer;
import org.example.api.data.entity.Transfer;
import org.example.api.data.request.CardRequest;
import org.example.api.data.request.LoginRequest;
import org.example.api.data.request.TransferRequest;
import org.example.apicalls.dto.AccountDTO;
import org.example.apicalls.dto.CardDTO;
import org.example.apicalls.dto.CustomerDTO;
import org.example.apicalls.dto.TransferDTO;
import org.example.apicalls.utils.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

public class BankService {
    @Autowired
    private AuthenticationController authenticationController;
    @Autowired
    private AccountController accountController;
    @Autowired
    private CardController cardController;
    @Autowired
    private TransferController transferController;

    public String doRegister (String name, String surname, String email, String password){

        CustomerDTO customerDTO= new CustomerDTO();
        customerDTO.setName(name);
        customerDTO.setSurname(surname);
        customerDTO.setEmail(email);
        customerDTO.setPassword(password);

        ResponseEntity<String> responseEntity = authenticationController.addCustomer(customerDTO);

        if (responseEntity.getStatusCode().is2xxSuccessful()){
            return  "The registration has been successful";
        } else {
            return "The registration has not been successful";
        }
    }

    // Register a new customer randomly generated
    public CustomerDTO registerRandomCustomer(){
        CustomerDTO customerDTO = Generator.generateRandomCustomerDTO(0,0);

        ResponseEntity<String> responseEntity = authenticationController.addCustomer(customerDTO);
        if (responseEntity.getStatusCode().is2xxSuccessful()){
            return  customerDTO;
        } else {
            return null;
        }
    }

    // Register a new customer randomly generated (with n accounts, m cards)
    public CustomerDTO registerRandomCustomer(int numAccounts, int numCards){
        CustomerDTO customerDTO = Generator.generateRandomCustomerDTO(numCards,numAccounts);

        ResponseEntity<String> responseEntity = authenticationController.addCustomer(customerDTO);
        if (responseEntity.getStatusCode().is2xxSuccessful()){
            return  customerDTO;
        } else {
            return null;
        }
    }


    public String doLogin (String email, String password, HttpServletRequest request){

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        ResponseEntity<String> responseEntity = authenticationController.login(loginRequest, request);

        if (responseEntity.getStatusCode().is2xxSuccessful()){
            return  "The login has been successful";
        } else {
            return "The login has not been successful";
        }
    }

    public String doLogin(CustomerDTO randomCustomerDTO, HttpServletRequest request){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(randomCustomerDTO.getEmail());
        loginRequest.setPassword(randomCustomerDTO.getPassword());

        ResponseEntity<String> responseEntity = authenticationController.login(loginRequest, request);

        if (responseEntity.getStatusCode().is2xxSuccessful()){
            return  "The login has been successful";
        } else {
            return "The login has not been successful";
        }
    }

    public String doLogout (HttpServletRequest request){

        ResponseEntity<String> responseEntity = authenticationController.logout(request);

        if(responseEntity.getStatusCode().is2xxSuccessful()){
            return "The login has been successful";
        } else {
            return "The login has not been successful";
        }
    }

    // Creates a new Account (if it is for a random user, set the id on the account before calling this method)
     public String doNewAccount (AccountDTO newAccount, HttpServletRequest request){

        ResponseEntity<String> responseEntity = accountController.createAccount(newAccount, request);

        if (responseEntity.getStatusCode().is2xxSuccessful()){
            return "A new account was created";
        } else {
            return "Error "+ responseEntity.getBody();
        }
     }

     // Creates a new Card (if it is for a random account, set the id on the card before calling this method)
     public String doNewCard(CardDTO newCard){
         CardRequest cardRequest = new CardRequest();
         cardRequest.setAccountId(newCard.getAccountId());
         cardRequest.setType(newCard.getType());
         cardRequest.setDate(newCard.getExpirationDate());
         ResponseEntity<String> responseEntity = cardController.newCard(cardRequest);

         if (responseEntity.getStatusCode().is2xxSuccessful()){
             return "A new card was created";
         } else {
             return "Error "+ responseEntity.getBody();
         }
     }

     public String doNewTransfer (TransferDTO dto, HttpServletRequest request){
         TransferRequest transferRequest = new TransferRequest();
         transferRequest.setTransferAmount(dto.getTransferAmount());
         if(dto.getCurrencyType().equals("USD")){
             transferRequest.setCurrencyType(Transfer.CurrencyType.USD);
         } else{
             transferRequest.setCurrencyType(Transfer.CurrencyType.EUR);
         }
         transferRequest.setOriginAccountId(dto.getOriginAccountId());
         transferRequest.setReceivingAccountId(dto.getReceivingAccountId());

         ResponseEntity<String> responseEntity = transferController.localTransfer(transferRequest, request, dto);

         if(responseEntity.getStatusCode().is2xxSuccessful()){
             return "The transfer has been succesful";
         } else {
             return "Error:" + responseEntity.getBody();
         }

     }


}
