package org.example.apicalls.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.api.data.entity.Account;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    @JsonProperty("accountId")
    private Integer accountId;

    @JsonProperty("accountType")
    private Account.AccountType accountType;

    @JsonProperty("isBlocked")
    private Boolean isBlocked;

    @JsonProperty("isInDebt")
    private Boolean isInDebt;

    @JsonProperty("amount")
    private Double amount;

    @JsonProperty("creationDate")
    private LocalDateTime creationDate;

    @JsonProperty("expirationDate")
    private LocalDateTime expirationDate;

    @JsonProperty("customerId")
    private Integer customerId;

    @JsonProperty("cards")
    private List<CardDTO> cards;

    @JsonProperty("originatingTransfers")
    private List<TransferDTO> originatingTransfers;

    @JsonProperty("receivingTransfers")
    private List<TransferDTO> receivingTransfers;
}
