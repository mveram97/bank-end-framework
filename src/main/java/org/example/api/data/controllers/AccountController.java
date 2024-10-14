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

    @GetMapping("/account/{id}")
    public Optional<Account> accountById(@PathVariable Integer id) {


        return account.findById(id);
    }

    @GetMapping("/accounts/{customerId}")
    public List<Account> accountsByCustomer(@PathVariable Integer customerId) {
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

    @PatchMapping("/transfer")
    public ResponseEntity<String> localTransfer(@RequestBody Transfer transfer, HttpServletRequest request) {

        //Obtiene el cliente que esta realizando la peticion
        String jwt = authService.getJwtFromCookies(request);
        String email = Token.getCustomerEmailFromJWT(jwt);
        Customer customer = customerRepository.findByEmail(email).get();

        //Recibe la informacion de la cuenta emisora
        Integer senderAccountId = transfer.getCustomerAccountId();
        Optional<Account> senderAccountOpt = accountRepository.findByAccountId(senderAccountId);
        if (!senderAccountOpt.isPresent()) {
            return ResponseEntity.badRequest().body("No existe la cuenta emisora ");
        }
        Account senderAccount = senderAccountOpt.get();

        //Recibe la informacion de la cuenta receiver
        Integer receiverId = transfer.getReceiverId();
        Optional<Account> receiverOpt = accountRepository.findByAccountId(receiverId);
        if (!receiverOpt.isPresent()) {
            return ResponseEntity.badRequest().body("No existe la cuenta receptora ");
        }
        Account receiver = receiverOpt.get();

        //Comprueba la cuenta emisora esta asociada al usuario que esta haciendo la peticion
        if (senderAccount.getCustomer().equals(customer)) {
        Double transferAmount = transfer.getAmount();
            //Comprueba si las cuenta receptora esta bloqueada, si la emisora tiene deudas o si se quiere
            // pasar mas dinero del que hay en la cuenta
            if (receiver.getIsBlocked() || senderAccount.getIsInDebt()
                    || senderAccount.getAmount() < transferAmount) {
                return ResponseEntity.badRequest()
                        .body("No se puede realizar la transferencia. Saldo insuficiente o receptor bloqueado.");
            } else if(transferAmount <= 0){
                return ResponseEntity.badRequest()
                        .body("El traspaso debe ser mayor que cero");
            }

            //Realiza la operacion de transferencia del amount
            senderAccount.setAmount(senderAccount.getAmount() - transferAmount);
            receiver.setAmount(receiver.getAmount() + transferAmount);
            accountRepository.save(senderAccount);
            accountRepository.save(receiver);

            //Devuelve que se ha llevado a cabo la operacion sin problema
            return ResponseEntity.ok().body("La transferencia fue realizada con exito ");
        }
        return ResponseEntity.badRequest().body("La cuenta no esta asociada a este usuario");
    }
}
