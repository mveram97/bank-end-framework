package org.example.api.data.controllers;

import org.example.api.data.entity.Account;
import org.example.api.service.AccountService;
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

    @GetMapping("/account/{id}")
    public Optional<Account> account (@PathVariable Integer id){
        return account.findById(id);
    }


}
