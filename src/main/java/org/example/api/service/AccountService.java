package org.example.api.service;

import org.example.api.data.entity.Account;
import org.example.api.data.entity.Card;
import org.example.api.data.repository.AccountRepository;
import org.example.apicalls..Account;
import org.example.apicalls..Card;
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


  public Account convertAccountToEntity(Account ) {
    Account account = new Account();
    account.setAccountType(.getAccountType());
    account.setIsBlocked(.getIsBlocked());
    account.setIsInDebt(.getIsInDebt());
    account.setAmount(.getAmount());
    account.setCreationDate(.getCreationDate());
    account.setExpirationDate(.getExpirationDate());

    List<Card> cards = Collections.emptyList();

    if (.getCards() == null) {
      .setCards(Collections.emptyList()); // Lo manejamos como una lista vacía
    }
    else {
      for (Card card : .getCards()) {
        cards.add(cardService.convertCardToEntity(card, account));
      }
    }
    account.setCards(cards);

    // Puedes agregar más conversiones si el  tiene más campos
    return account;
  }
}
