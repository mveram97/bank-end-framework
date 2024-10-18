package org.example.api.service;

import org.example.api.data.entity.Account;
import org.example.api.data.entity.Card;
import org.example.api.data.entity.Customer;
import org.example.api.data.repository.AccountRepository;
import org.example.apicalls.dto.AccountDTO;
import org.example.apicalls.dto.CardDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

  // Método para convertir AccountDTO a Account
  public Account convertAccountDtoToEntity(AccountDTO dto) {
    // Check if user already exists
    Optional<Account> account = accountRepository.findById(dto.getAccountId());
    if (account.isPresent())
      return account.get();

    Account newAccount = new Account();
    newAccount.setAccountType(dto.getAccountType());
    newAccount.setIsBlocked(dto.getIsBlocked());
    newAccount.setIsInDebt(dto.getIsInDebt());
    newAccount.setAmount(dto.getAmount());
    newAccount.setCreationDate(dto.getCreationDate());
    newAccount.setExpirationDate(dto.getExpirationDate());

    List<Card> cards = new ArrayList<>();
    for (CardDTO cardDTO : dto.getCards()) {
      cards.add(cardService.convertCardDtoToEntity(cardDTO, newAccount));
    }
    newAccount.setCards(cards);

    // Puedes agregar más conversiones si el DTO tiene más campos
    return newAccount;
  }
}
