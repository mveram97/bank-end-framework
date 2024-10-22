
package org.example.api.data.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.example.api.data.entity.Account;
import org.example.api.data.entity.Customer;
import org.example.api.data.entity.Transfer;
import org.example.api.data.repository.AccountRepository;
import org.example.api.data.repository.CustomerRepository;
import org.example.api.service.AccountService;
import org.example.api.service.AuthService;
import org.example.api.token.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private Token tokenService;

    @GetMapping("/api/account/{id}")    // get 1 account by accountId
    public Optional<Account> accountById(@PathVariable Integer id) {
        return account.findById(id);
    }

    @GetMapping("/api/accounts/{customerId}")   // get all accounts by customerId
    public List<Account> accountsByCustomer(@PathVariable Integer customerId) {
        return account.findByCustomer(customerId);
    }

    @GetMapping("/api/accounts")    // get all accounts from the logged-in user
    public ResponseEntity<List<Account>> getUserAccounts(HttpServletRequest request) {
        // Get JWT token from cookies
        String jwt = authService.getJwtFromCookies(request);
        System.out.println(jwt);

        // Validate token
        if (jwt == null || !tokenService.validateToken(jwt)) {
            return ResponseEntity.status(401).build(); // 401 Unauthorized if token is not valid
        }

        // Get user mail from token
        String email = tokenService.getCustomerEmailFromJWT(jwt);

        // Get user from email
        Optional<Customer> customerOpt = authService.findCustomerByEmail(email);
        if (!customerOpt.isPresent()) {
            return ResponseEntity.status(404).build(); // 404 Not Found if error finding user
        }

        // Get logged user´s accounts
        Customer customer = customerOpt.get();
        List<Account> accounts = account.findByCustomer(customer.getCustomerId());

        return ResponseEntity.ok(accounts); // 200 OK with users accounts
    }

    @GetMapping ("/api/accounts/amount")    // get total amount from all accounts (logged-in user)
    public ResponseEntity<Double> getUserAmount(HttpServletRequest request){
        // Get JWT token from cookies
        String jwt = authService.getJwtFromCookies(request);
        System.out.println(jwt);

        // Validate token
        if (jwt == null || !tokenService.validateToken(jwt)) {
            return ResponseEntity.status(401).build(); // 401 Unauthorized if token is not valid
        }

        // Get user email from token
        String email = tokenService.getCustomerEmailFromJWT(jwt);

        // Get user from email
        Optional<Customer> customerOpt = authService.findCustomerByEmail(email);
        if (!customerOpt.isPresent()) {
            return ResponseEntity.status(404).build(); // 404 Not Found if user is not found
        }

        // Get logged user´s accounts
        Customer customer = customerOpt.get();
        List<Account> accounts = account.findByCustomer(customer.getCustomerId());

        // Calculate total money from all accounts
        double totalAmount = 0.0;
        for (Account acc : accounts){
            totalAmount += acc.getAmount();
        }

        return ResponseEntity.ok(totalAmount); // 200 OK with total money
    }

    private boolean checkAccountInDebt(Account account){
        return account.getAmount() < 0;
    }


    @GetMapping("/api/amount/{accountId}")  // get amount by accountId
    public Double amountOfAccount(@PathVariable Integer accountId) {
        return account.findById(accountId).get().getAmount();


    }

    @PostMapping("/api/account/new")
    public ResponseEntity<String> createAccount(@RequestBody Account newAcc, HttpServletRequest request) {
        // Obtener el token JWT de las cookies
        String jwt = authService.getJwtFromCookies(request);

        // Validar el token
        if (jwt == null || !tokenService.validateToken(jwt)) {
            return ResponseEntity.status(401).body("Unauthorized: Token is not valid."); // 401 Unauthorized
        }

        // Obtener el email del usuario a partir del token
        String email = tokenService.getCustomerEmailFromJWT(jwt);

        // Obtener el cliente usando el email
        Optional<Customer> customerOpt = authService.findCustomerByEmail(email);
        if (!customerOpt.isPresent()) {
            return ResponseEntity.status(404).body("Error: User not found."); // 404 Not Found
        }

        // Asignar el cliente a la nueva cuenta
        Customer customer = customerOpt.get();
        Account newAccount = account.convertAccountToEntity(newAcc);
        newAccount.setCustomer(customer);

        // Asignar creationDate a la hora y fecha actual
        newAccount.setCreationDate(LocalDateTime.now());

        // Asignar expirationDate, por ejemplo, un año después de la creationDate
        newAccount.setExpirationDate(newAccount.getCreationDate().plusYears(1)); // Ajusta según tu lógica de negocio

        // isBlocked debe ser FALSE para una nueva cuenta
        newAccount.setIsBlocked(false);

        // isInDebt depende del amount, si es mayor a 0 se considera que no está en deuda
        if (newAccount.getAmount() != null && newAccount.getAmount() > 0) {
            newAccount.setIsInDebt(false); // No está en deuda
        } else {
            newAccount.setIsInDebt(true);  // Está en deuda si el amount es 0 o negativo
        }

        // Asignar el accountType si está presente, de lo contrario, asignar un tipo por defecto
        if (newAccount.getAccountType() != null) {
            newAccount.setAccountType(newAccount.getAccountType());
        } else {
            newAccount.setAccountType(Account.AccountType.CHECKING_ACCOUNT); // Establecer un tipo predeterminado
        }

        try {
            // Guardar la nueva cuenta en el repositorio
            Account createdAccount = account.save(newAccount);
            return ResponseEntity.status(201).body("Account created successfully: " + createdAccount.getAccountId()); // 201 Created
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: Could not create account. " + e.getMessage()); // 500 Internal Server Error
        }
    }

    @DeleteMapping ("/api/account/delete/{accountId}")
    public ResponseEntity<String> deleteAccount(@PathVariable int accountId, HttpServletRequest request){
         // Check if the account exists
        Optional<Account> account = accountRepository.findByAccountId(accountId);
        if (account.isEmpty()){
            return ResponseEntity.badRequest().body("Error: account not found");
        }

        // Check if the accounts customer exists
        Customer customer = account.get().getCustomer();
        if (customer == null){
            return ResponseEntity.badRequest().body("Error: client of the account not found");
        }

        // We try to delete the account from the customer list and from the database
        if (!customer.deleteAccount(accountId)){
            return ResponseEntity.badRequest().body("Error: could not delete account from customer");
        }
        accountRepository.delete(account.get());

        return ResponseEntity.ok("todo correcto");
    }
}
