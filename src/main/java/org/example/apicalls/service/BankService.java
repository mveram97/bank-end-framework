package org.example.apicalls.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import org.example.api.data.entity.Transfer;
import org.example.api.data.request.CardRequest;
import org.example.api.data.request.LoginRequest;
import org.example.api.data.request.TransferRequest;
import org.example.apicalls.apiconfig.BankAPI;
import org.example.apicalls.client.BankClient;
import org.example.apicalls.dto.AccountDTO;
import org.example.apicalls.dto.CardDTO;
import org.example.apicalls.dto.CustomerDTO;
import org.example.apicalls.dto.TransferDTO;
import org.example.apicalls.utils.Generator;

import java.util.Map;

public class BankService {
    private BankClient client;
    private BankAPI proxy;
    private Response response;
    NewCookie cookie;
    public BankService(){
        client = new BankClient();
        proxy = client.getAPI();
    }

    public Response getResponse(){
        return response;
    }

    public Response doRegister(String name, String surname, String email, String password){
        BankAPI proxy = client.getAPI();
        CustomerDTO customerDTO= new CustomerDTO();
        customerDTO.setName(name);
        customerDTO.setSurname(surname);
        customerDTO.setEmail(email);
        customerDTO.setPassword(password);

        response = proxy.addCustomer(customerDTO);
        return response;
    }

    // Register a new customer randomly generated
    public CustomerDTO registerRandomCustomer(){
        CustomerDTO customerDTO = Generator.generateRandomCustomerDTO(0,0);
        BankAPI proxy = client.getAPI();
        response = proxy.addCustomer(customerDTO);
        return customerDTO;
    }

    // Register a new customer randomly generated (with n accounts, m cards)
    public CustomerDTO registerRandomCustomer(int numAccounts, int numCards){
        CustomerDTO customerDTO = Generator.generateRandomCustomerDTO(numCards,numAccounts);
        BankAPI proxy = client.getAPI();
        response = proxy.addCustomer(customerDTO);
        return customerDTO;
    }


    public Response doLogin (String email, String password){
        proxy = client.getAPI();
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        response = proxy.login(loginRequest, null);
        System.out.println("HTTP Status: "+ response.getStatus());
        if (response.getStatus() == 200) {
            Map<String, NewCookie> cookies = response.getCookies();
            NewCookie newCookie = cookies.entrySet().iterator().next().getValue();
            proxy = client.getAPI(newCookie);
        }
        return response;
    }

    public Response doLogin(CustomerDTO randomCustomerDTO){
        proxy = client.getAPI();
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(randomCustomerDTO.getEmail());
        loginRequest.setPassword(randomCustomerDTO.getPassword());

        response = proxy.login(loginRequest, null);
        System.out.println("HTTP Status: "+ response.getStatus());
        if (response.getStatus() == 200) {
            Map<String, NewCookie> cookies = response.getCookies();
            cookie = cookies.entrySet().iterator().next().getValue();
            proxy = client.getAPI(cookie);
        }
        return response;
    }

    public Response doLogout (){
        response = proxy.logout(null);
        System.out.println(response.getStatus());
        proxy = client.getAPI();
        return response;
    }

    // Creates a new Account (if it is for a random user, set the id on the account before calling this method)
     public Response doNewAccount (AccountDTO newAccount, HttpServletRequest request){
        response = proxy.createAccount(newAccount, request);
         System.out.println(response.getStatus());
         return response;
     }

     // Creates a new Card (if it is for a random account, set the id on the card before calling this method)
     public Response doNewCard(CardDTO newCard){
         CardRequest cardRequest = new CardRequest();
         cardRequest.setAccountId(newCard.getAccountId());
         cardRequest.setType(newCard.getType());
         cardRequest.setDate(newCard.getExpirationDate());
         response = proxy.newCard(cardRequest);
         System.out.println(response.getStatus());
         return response;
     }

     public Response doNewTransfer (TransferDTO dto, HttpServletRequest request){
         TransferRequest transferRequest = new TransferRequest();
         transferRequest.setTransferAmount(dto.getTransferAmount());
         if(dto.getCurrencyType().equals("USD")){
             transferRequest.setCurrencyType(Transfer.CurrencyType.USD);
         } else{
             transferRequest.setCurrencyType(Transfer.CurrencyType.EUR);
         }
         transferRequest.setOriginAccountId(dto.getOriginAccountId());
         transferRequest.setReceivingAccountId(dto.getReceivingAccountId());
         response = proxy.localTransfer(transferRequest, request);
         System.out.println(response.getStatus());
         return response;
     }

}