package org.example.apicalls.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    @JsonProperty("account_id")
    private Integer accountId;

    @JsonProperty("account_type")
    private String accountType;

    @JsonProperty("is_blocked")
    private Boolean isBlocked;

    @JsonProperty("is_in_debt")
    private Boolean isInDebt;

    @JsonProperty("amount")
    private Double amount;

    @JsonProperty("creation_date")
    private Date creationDate;

    @JsonProperty("expiration_date")
    private Date expirationDate;

    @JsonProperty("customer_id")
    private Integer customerId;

    @JsonProperty("cards")
    private List<CardDTO> cards;

    @JsonProperty("originating_transfers")
    private List<TransferDTO> originatingTransfers;

    @JsonProperty("receiving_transfers")
    private List<TransferDTO> receivingTransfers;
}
