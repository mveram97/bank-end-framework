package org.example.api.data.controllers;

import org.example.api.data.entity.Card;
import org.example.api.service.CardService;
import org.example.api.service.CustomerService;
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

  @GetMapping("/card")
  public List<Card> card() {
    return card.findAll();
  }

  @GetMapping("/card/{id}")
  public Optional<Card> card(@PathVariable Integer id) {
    return card.findById(id);
  }
}
