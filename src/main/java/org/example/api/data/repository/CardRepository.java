package org.example.api.data.repository;

import org.example.api.data.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {
    Optional<Card> findByCardId(Integer cardId);

    List<Card> findByAccount_AccountId(Integer accountId);
    List<Card> findByAccount_Customer_CustomerId(Integer customerId);
    void deleteByAccount_AccountId(Integer accountId);
}
