
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
/*
    @PatchMapping("/transfer")
    public ResponseEntity<String> localTransfer(@RequestBody Transfer transfer, HttpServletRequest request) {
        // Get request client
        String jwt = authService.getJwtFromCookies(request);
        String email = Token.getCustomerEmailFromJWT(jwt);
        Customer customer = customerRepository.findByEmail(email).get();

        // Get receiving client info
        Integer senderAccountId = transfer.getCustomerAccountId();
        Optional<Account> senderAccountOpt = accountRepository.findByAccountId(senderAccountId);
        if (!senderAccountOpt.isPresent()) {
            return ResponseEntity.badRequest().body("No existe la cuenta emisora ");
        }
        Account senderAccount = senderAccountOpt.get();

        // Get receiving account info
        Integer receiverId = transfer.getReceiverId();
        Optional<Account> receiverOpt = accountRepository.findByAccountId(receiverId);
        if (!receiverOpt.isPresent()) {
            return ResponseEntity.badRequest().body("No existe la cuenta receptora ");
        }
        Account receiver = receiverOpt.get();

        // Check requesting account belongs to requesting user
        if (senderAccount.getCustomer().equals(customer)) {
            Double transferAmount = transfer.getAmount();
            // Check - if receiving account is blocked
            //       - if requesting account is in debt
            //       - if there is not enough money in requesting account
            if (receiver.getIsBlocked() || senderAccount.getIsInDebt()
                    || senderAccount.getAmount() < transferAmount) {
                return ResponseEntity.badRequest()
                        .body("Transfer can not be done. Not enough money or blocked receiver.");
            } else if(transferAmount <= 0){
                return ResponseEntity.badRequest()
                        .body("Money to transfer must be greater than 0");
            }

            // Transfer the money
            senderAccount.setAmount(senderAccount.getAmount() - transferAmount);
            receiver.setAmount(receiver.getAmount() + transferAmount);

            //Check none of the accounts is in debt
            senderAccount.setIsInDebt(checkAccountInDebt(senderAccount));
            receiver.setIsInDebt(checkAccountInDebt(receiver));

            accountRepository.save(senderAccount);
            accountRepository.save(receiver);

            // Check operation has been done successfully
            return ResponseEntity.ok().body("Transfer made successfully");
        }
        return ResponseEntity.badRequest().body("Account does not belong to the user");
    }
*/
    private boolean checkAccountInDebt(Account account){
        return account.getAmount() < 0;
    }


    @GetMapping("/api/amount/{accountId}")  // get amount by accountId
    public Double amountOfAccount(@PathVariable Integer accountId) {
        return account.findById(accountId).get().getAmount();


    }

    @PostMapping("/api/accounts/news")
    public ResponseEntity<String> createAccount(@RequestBody Account newAccount, HttpServletRequest request) {
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
        newAccount.setCustomer(customer);

        try {
            // Guardar la nueva cuenta en el repositorio
            Account createdAccount = account.save(newAccount);
            return ResponseEntity.status(201).body("Account created successfully: " + createdAccount.getAccountId()); // 201 Created
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: Could not create account. " + e.getMessage()); // 500 Internal Server Error
        }
    }
}
