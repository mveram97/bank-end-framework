package org.example.api.service;

import org.example.api.data.entity.Account;
import org.example.api.data.entity.Card;
import org.example.api.data.repository.AccountRepository;
import org.example.apicalls.dto.AccountDTO;
import org.example.apicalls.dto.CardDTO;
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


  public Account convertAccountDtoToEntity(AccountDTO dto) {
    Account account = new Account();
    account.setAccountType(dto.getAccountType());
    account.setIsBlocked(dto.getIsBlocked());
    account.setIsInDebt(dto.getIsInDebt());
    account.setAmount(dto.getAmount());
    account.setCreationDate(dto.getCreationDate());
    account.setExpirationDate(dto.getExpirationDate());

    List<Card> cards = Collections.emptyList();

    if (dto.getCards() == null) {
      dto.setCards(Collections.emptyList()); // Lo manejamos como una lista vacía
    }
    else {
      for (CardDTO card : dto.getCards()) {
        cards.add(cardService.convertCardDtoToEntity(card, account));
      }
    }
    account.setCards(cards);

    // Puedes agregar más conversiones si el DTO tiene más campos
    return account;
  }
}
