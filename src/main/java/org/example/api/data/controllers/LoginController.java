package org.example.api.data.controllers;

import org.example.api.data.request.LoginRequest;
import org.example.api.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private AuthService authService;

    @PostMapping("/public/login")
    public ResponseEntity<String> loginCustomer(@RequestBody LoginRequest loginRequest) {

        if (authService.authenticate(loginRequest.getEmail(), loginRequest.getPassword())) {
            ResponseCookie jwtCookie = authService.generateJwtCookie(loginRequest);
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body("Autenticación exitosa");
        }
        
        return ResponseEntity.badRequest().body("El usuario o la contraseña no son correctos");
    }




}

