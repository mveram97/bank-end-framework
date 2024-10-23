package org.example.api.data.repository;

import org.example.api.data.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Integer> {

    Optional<Transfer> findByTransferId(Integer transferId); // find by transferId

    List<Transfer> findByOriginAccount_AccountId(Integer originAccountId); // find transfers by originAccountId

    List<Transfer> findByReceivingAccount_AccountId(Integer receivingAccountId); // find transfers by receivingAccountId

    List<Transfer> findByTransferStatus(Transfer.TransferStatus transferStatus); // find transfers by status

    @Modifying
    @Transactional
    void deleteByReceivingAccount_AccountId(Integer receivingAccountId);

    @Modifying
    @Transactional
    void deleteByOriginAccount_AccountId(Integer originAccountId);
}
