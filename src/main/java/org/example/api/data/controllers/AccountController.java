package org.example.api.data.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.example.api.data.entity.Account;
import org.example.api.data.entity.Customer;
import org.example.api.service.AccountService;
import org.example.api.service.AuthService;
import org.example.api.token.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController

public class AccountController {

    private AccountService account;

    public AccountController(AccountService account) {
        this.account = account;
    }

    @Autowired
    private AuthService authService;

    @Autowired
    private Token tokenService;

    @GetMapping("/account/{id}")
    public Optional<Account> accountById(@PathVariable Integer id){


        return account.findById(id);
    }

    @GetMapping("/accounts/{customerId}")
    public List<Account> accountsByCustomer(@PathVariable Integer customerId){
        return account.findByCustomer(customerId);
    }
    @GetMapping("/api/accounts")
    public ResponseEntity<List<Account>> getUserAccounts(HttpServletRequest request) {
        // Obtener el token JWT desde las cookies
        String jwt = authService.getJwtFromCookies(request);
        System.out.println(jwt);

        // Validar el token
        if (jwt == null || !tokenService.validateToken(jwt)) {
            return ResponseEntity.status(401).build(); // 401 Unauthorized si el token es inválido
        }

        // Obtener el email del usuario a partir del token
        String email = tokenService.getCustomerEmailFromJWT(jwt);

        // Obtener el usuario por email
        Optional<Customer> customerOpt = authService.findCustomerByEmail(email);
        if (!customerOpt.isPresent()) {
            return ResponseEntity.status(404).build(); // 404 Not Found si no se encuentra el usuario
        }

        // Obtener las cuentas del usuario autenticado
        Customer customer = customerOpt.get();
        List<Account> accounts = account.findByCustomer(customer.getCustomerId());

        return ResponseEntity.ok(accounts); // 200 OK con las cuentas del usuario
    }

    @GetMapping ("/api/accounts/amount")
    public ResponseEntity<Double> getUserAmount(HttpServletRequest request){
        // Obtener el token JWT desde las cookies
        String jwt = authService.getJwtFromCookies(request);
        System.out.println(jwt);

        // Validar el token
        if (jwt == null || !tokenService.validateToken(jwt)) {
            return ResponseEntity.status(401).build(); // 401 Unauthorized si el token es inválido
        }

        // Obtener el email del usuario a partir del token
        String email = tokenService.getCustomerEmailFromJWT(jwt);

        // Obtener el usuario por email
        Optional<Customer> customerOpt = authService.findCustomerByEmail(email);
        if (!customerOpt.isPresent()) {
            return ResponseEntity.status(404).build(); // 404 Not Found si no se encuentra el usuario
        }

        // Obtener las cuentas del usuario autenticado
        Customer customer = customerOpt.get();
        List<Account> accounts = account.findByCustomer(customer.getCustomerId());

        //Calcular el total de dinero en todas las cuentas
        double totalAmount = 0.0;
        for (Account acc : accounts){
            totalAmount += acc.getAmount();
        }

        return ResponseEntity.ok(totalAmount);


    }
}
