package org.example.api.data.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.example.api.data.entity.Account;
import org.example.api.data.entity.Customer;
import org.example.api.data.entity.Transfer;
import org.example.api.data.repository.AccountRepository;
import org.example.api.data.repository.CustomerRepository;
import org.example.api.data.repository.TransferRepository;
import org.example.api.data.request.TransferRequest;
import org.example.api.service.AuthService;
import org.example.api.service.TransferService;
import org.example.api.token.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@RestController
public class TransferController {

    @Autowired
    private AuthService authService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private Token tokenService;

    @Autowired
    private TransferService transferService;


    @PostMapping("/api/transfer/new")
    public ResponseEntity<String> localTransfer(@RequestBody TransferRequest transferRequest, HttpServletRequest request, Transfer transfer) {

        // Set transfer default fields
        Double transferAmount = transferService.setCurrencyAndReturnAmount(transferRequest, transfer);

        // Get request client
        String jwt = authService.getJwtFromCookies(request);
        String email = Token.getCustomerEmailFromJWT(jwt);
        Customer customer = customerRepository.findByEmail(email).get();

        // Get sender client info
        Integer senderAccountId = transferRequest.getOriginAccountId();
        Optional<Account> senderAccountOpt = accountRepository.findByAccountId(senderAccountId);
        if (!senderAccountOpt.isPresent()) {

            // In this scenario, the failed transfer is not stored in the database.
            return ResponseEntity.badRequest().body("Sender account does not exist");

        }
        Account senderAccount = senderAccountOpt.get();
        transfer.setOriginAccount(senderAccount);

        // Get receiving account info
        Integer receiverId = transferRequest.getReceivingAccountId();
        Optional<Account> receiverOpt = accountRepository.findByAccountId(receiverId);
        if (!receiverOpt.isPresent()) {

            // In this scenario, the failed transfer is not stored in the database.
            return ResponseEntity.badRequest().body("Receiver account does not exist");
        }
        Account receiverAccount = receiverOpt.get();
        transfer.setReceivingAccount(receiverAccount);

        // Check requesting account belongs to requesting user
        if (senderAccount.getCustomer().equals(customer)) {

            return transferService.transferOperation(senderAccount, receiverAccount, transferAmount, transfer);

            // In this scenario, the failed transfer is not stored in the database.

        }
        System.out.println(senderAccount.getCustomer().equals(customer));
        return ResponseEntity.badRequest().body("Account does not belong to the user");
    }

    @GetMapping("/api/transfers/receive/all")
    public ResponseEntity<String> getAllReceivedTransfers() {
        // Verify user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Assuming username is contained in the customerId
        Integer customerId = Integer.valueOf(authentication.getName());
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isEmpty())
            return ResponseEntity.badRequest().body("You are not logged");

        // Verify user has accounts
        List<Account> accountList = accountRepository.findByCustomer_CustomerId(customerId);
        if (accountList.isEmpty())
            return ResponseEntity.badRequest().body("This user does not have any account");

        // Get all the transfers received from all accounts
        List<Transfer> allTransfersReceived = new ArrayList<>();
        for (Account account : accountList) {
            allTransfersReceived.addAll(transferService.findByReceivingAccount(account.getAccountId()));
        }

        // Inform if there are no transfer received
        if (allTransfersReceived.isEmpty())
            return ResponseEntity.ok("This user has not received any transfer");

        // Show every transfer information
        StringBuilder responseBody = new StringBuilder();
        for (Transfer transfer : allTransfersReceived) {
            responseBody.append("{\n\tid: ")
                    .append(transfer.getTransferId())
                    .append("\n\tdate: ")
                    .append(transfer.getTransferDate())
                    .append("\n\tcurrencyType: ")
                    .append(transfer.getCurrencyType())
                    .append("\n\tamount: ")
                    .append(transfer.getTransferAmount())
                    .append("\n\tstatus: ")
                    .append(transfer.getTransferStatus())
                    .append("\n}\n");
        }
        return ResponseEntity.ok(responseBody.toString());
    }

    @GetMapping("/api/transfer/receive/{accountId}")
    public ResponseEntity<String> getReceiveTransfer(@PathVariable Integer accountId) {
        // Verify account exists
        Optional<Account> account = accountRepository.findByAccountId(accountId);
        if (account.isEmpty())
            return ResponseEntity.badRequest().body("Account does not exist");

        // Find all transfer -> inform if we did not find any transfer
        List<Transfer> receivedTransfers = transferService.findByReceivingAccount(accountId);
        if (receivedTransfers.isEmpty()) {
            return ResponseEntity.ok("This account did not receive any transfer");
        }

        // Show every transfer information
        StringBuilder responseBody = new StringBuilder();
        for (Transfer transfer : receivedTransfers) {
            responseBody.append("{\n\tid: ")
                    .append(transfer.getTransferId())
                    .append("\n\tdate: ")
                    .append(transfer.getTransferDate())
                    .append("\n\tcurrencyType: ")
                    .append(transfer.getCurrencyType())
                    .append("\n\tamount: ")
                    .append(transfer.getTransferAmount())
                    .append("\n\tstatus: ")
                    .append(transfer.getTransferStatus())
                    .append("\n}\n");
        }
        return ResponseEntity.ok(responseBody.toString());
    }
}