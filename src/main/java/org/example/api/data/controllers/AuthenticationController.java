package org.example.api.data.controllers;

import org.example.api.data.entity.Customer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

public class AuthenticationController {
    private void checkEmpty(String string){
        Optional.of(string)
                .filter(e -> !e.isEmpty())
                .orElseThrow(() -> new RuntimeException("The name value is empty!"));
    }

    @PutMapping("/register/{name}, {surname}, {email}, {password}")
    public ResponseEntity<Customer> customerResponseEntity(@RequestBody String name, @RequestBody String surname, @RequestBody String email, @RequestBody String password){

        Optional.of(name)
                .filter(e -> !e.isEmpty())
                .orElseThrow(() -> new RuntimeException("The name value is empty!"));

        Optional.of(email)
                .filter(e -> (!e.isEmpty() || !email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")))
                .orElseThrow(() -> new RuntimeException("The email value is empty or isn't compatible"));


        return null;
    }
}
