package org.example.apicalls.service;

import jakarta.servlet.http.HttpServletRequest;
import org.example.api.data.controllers.AuthenticationController;
import org.example.api.data.request.LoginRequest;
import org.example.apicalls.dto.CustomerDTO;
import org.springframework.http.ResponseEntity;

public class BankService {

    private AuthenticationController authenticationController;


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

    public String doLogin (String email, String password, HttpServletRequest request){

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);


        ResponseEntity<String> responseEntity = authenticationController.login(loginRequest, request);


        if (responseEntity.getStatusCode().is2xxSuccessful()){
            return  "The login has been successful";
        } else {
            return "The logout has not been successful";
        }

    }

}
