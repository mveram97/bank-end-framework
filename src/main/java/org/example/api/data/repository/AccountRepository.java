package org.example.api.data.repository;

import org.example.api.data.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
  Optional<Account> findByAccountId(Integer accountId);

  List<Account> findByCustomer_CustomerId(Integer customerId);

  @Modifying
  @Transactional
  void deleteByCustomer_CustomerId(Integer customerId);
}
