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
    @JsonProperty("accountId")
    private Integer accountId;

    @JsonProperty("accountType")
    private String accountType;

    @JsonProperty("isBlocked")
    private Boolean isBlocked;

    @JsonProperty("isInDebt")
    private Boolean isInDebt;

    @JsonProperty("amount")
    private Double amount;

    @JsonProperty("creationDate")
    private Date creationDate;

    @JsonProperty("expirationDate")
    private Date expirationDate;

    @JsonProperty("customerId")
    private Integer customerId;

    @JsonProperty("cards")
    private List<CardDTO> cards;

    @JsonProperty("originatingTransfers")
    private List<TransferDTO> originatingTransfers;

    @JsonProperty("receivingTransfers")
    private List<TransferDTO> receivingTransfers;
}
