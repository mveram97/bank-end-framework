package org.example.api.service;

import org.example.api.data.entity.Account;
import org.example.api.data.entity.Card;
import org.example.api.data.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
  @Autowired private CardService cardService;
  @Autowired private AccountRepository accountRepository;

  public List<Account> findAll() {
    return accountRepository.findAll();
  }

  public Optional<Account> findById(Integer accountId) {
    return accountRepository.findById(accountId);
  }

  public Account save(Account account) {
    return accountRepository.save(account);
  }

  public void deleteById(Integer accountId) {
    accountRepository.deleteById(accountId);
  }

  public List<Account> findByCustomer(Integer customerId) {return accountRepository.findByCustomer_CustomerId(customerId);}

  public boolean checkAccountInDebt(Account account){
    return account.getAmount() < 0;
  }


  public Account convertAccountToEntity(Account acc) {
    Account account = new Account();
    account.setAccountType(acc.getAccountType());
    account.setIsBlocked(acc.getIsBlocked());
    account.setIsInDebt(acc.getIsInDebt());
    account.setAmount(acc.getAmount());
    account.setCreationDate(acc.getCreationDate());
    account.setExpirationDate(acc.getExpirationDate());

    List<Card> cards = Collections.emptyList();

    if (acc.getCards() == null) {
      acc.setCards(Collections.emptyList()); // Lo manejamos como una lista vacía
    }
    else {
      for (Card card : acc.getCards()) {
        cards.add(cardService.convertCardToEntity(card, account));
      }
    }
    account.setCards(cards);

    // Puedes agregar más conversiones si el  tiene más campos
    return account;
  }
}
