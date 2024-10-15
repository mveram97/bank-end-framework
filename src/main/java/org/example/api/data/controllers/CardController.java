package org.example.api.data.controllers;

import org.example.api.data.entity.Account;
import org.example.api.data.entity.Card;
import org.example.api.data.request.CardRequest;
import org.example.api.service.AccountService;
import org.example.api.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
public class CardController {
    @Autowired
    private AccountService accountService;

    private final CardService card;

    public CardController(CardService card) {
        this.card = card;
    }

    @GetMapping("/card")
    public List<Card> card() {
        return card.findAll();
    }

    @GetMapping("/card/{id}")
    public Optional<Card> card(@PathVariable Integer id) {
        return card.findById(id);
    }

    @GetMapping("/cards/{accountId}")
    public List<Card> cardsByAccountId(@PathVariable Integer accountId) {
        return card.findByAccountId(accountId);
    }

    @PostMapping("/api/card/new")
    public ResponseEntity<String> newCard(@RequestBody CardRequest cardRequest) {
        // Verifying correct data from user
        if (!cardRequest.getType().equals("Debit") && !cardRequest.getType().equals("Credit")){
            return ResponseEntity.badRequest().body("Fallo al crear tarjeta: Tipo de tarjeta no valido");
        }

        // Generate randomly CVC and CardNumber
        Random rnd = new Random();
        Long number = rnd.nextLong() % 10000000000000000L;
        int cvc = rnd.nextInt() % 1000;
        // We dont want negative CVC numbers
        if (cvc < 0) cvc += -1;

        // Create new card
        Card newCard = new Card();
        newCard.setType(cardRequest.getType());
        newCard.setExpirationDate(cardRequest.getDate());
        newCard.setNumber(number);
        newCard.setCvc(cvc);

        // Find account that will contain card
        Optional<Account> account = accountService.findById(cardRequest.getAccountId());

        // Verifying correct account data
        if (!account.isPresent()){
            return ResponseEntity.badRequest().body("Fallo al crear tarjeta: La cuenta no existe");
        }

        newCard.setAccount(account.get());
        card.save(newCard);

        return ResponseEntity.ok("Tarjeta creada con exito");
    }
}
