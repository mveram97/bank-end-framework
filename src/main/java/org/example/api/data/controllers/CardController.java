package org.example.api.data.controllers;

import org.example.api.data.entity.Card;
import org.example.api.data.entity.Customer;
import org.example.api.service.CardService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class CardController {
    private CardService card;

    public CardController(CardService card) {
        this.card = card;
    }

    @GetMapping("api/BDcards")     // get all cards from DB
    public List<Card> card() {
        return card.findAll();
    }

    @GetMapping("api/card/{cardId}")       // get 1 card by cardId
    public Optional<Card> card(@PathVariable Integer id) {
        return card.findById(id);
    }

    @GetMapping("api/cards/{accountId}")    // get all cards by accountId
    public List<Card> cardsByAccountId(@PathVariable Integer accountId) {
        return card.findByAccountId(accountId);
    }

    @GetMapping("/api/cards")
    public List<Card> getCards(@AuthenticationPrincipal Customer userDetails) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Suponiendo que el customerId se almacena en el username
        Integer customerId = Integer.valueOf(authentication.getName()); // Ajusta esto según tu implementación

        return card.getCardsByCustomerId(customerId);
    }
}
