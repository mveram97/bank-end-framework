package org.example.api.data.repository;

import org.example.api.data.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Integer> {

    Optional<Transfer> findByTransferId(Integer transferId); // find by transferId

    List<Transfer> findByOriginAccountId(Integer originAccountId); // find transfers by originAccountId

    List<Transfer> findByReceivingAccountId(Integer receivingAccountId); // find transfers by receivingAccountId

    List<Transfer> findByTransferStatus(Transfer.TransferStatus transferStatus); // find transfers by status
}
