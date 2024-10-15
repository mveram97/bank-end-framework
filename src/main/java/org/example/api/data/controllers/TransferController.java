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
import org.example.api.token.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
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


    @PostMapping("/api/transfer/new")
    public ResponseEntity<String> localTransfer(@RequestBody TransferRequest transferRequest, HttpServletRequest request, Transfer transfer) {

        // Set transfer default fields

        // Date
        /*
        LocalDate localDate = LocalDate.now();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        transfer.setTransferDate(date); */

        // Currency
        transfer.setCurrencyType(Transfer.CurrencyType.EUR);

        // Amount
        Double transferAmount = transferRequest.getTransferAmount();
        transfer.setTransferAmount(transferAmount);


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
        Account receiver = receiverOpt.get();
        transfer.setReceivingAccount(receiver);

        // Check requesting account belongs to requesting user
        if (senderAccount.getCustomer().equals(customer)) {
            // Check - if receiving account is blocked
            //       - if requesting account is in debt
            //       - if there is not enough money in requesting account
            if (receiver.getIsBlocked() || senderAccount.getIsInDebt()
                    || senderAccount.getAmount() < transferAmount) {
                transfer.setTransferStatus(Transfer.TransferStatus.FAILED);
                transferRepository.save(transfer);
                return ResponseEntity.badRequest()
                        .body("Transfer can not be done. Not enough money or blocked receiver.");
            } else if(transferAmount <= 0){
                transfer.setTransferStatus(Transfer.TransferStatus.FAILED);
                transferRepository.save(transfer);
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

            // Save transfer in database
            transfer.setTransferStatus(Transfer.TransferStatus.SUCCESSFUL);
            transferRepository.save(transfer);

            // Check operation has been done successfully
            return ResponseEntity.ok().body("Transfer made successfully");
        }

        // In this scenario, the failed transfer is not stored in the database.
        return ResponseEntity.badRequest().body("Account does not belong to the user");
    }


    private boolean checkAccountInDebt(Account account){
        return account.getAmount() < 0;
    }
}
