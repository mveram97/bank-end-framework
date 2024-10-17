package org.example.apicalls.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class TransferDTO {
    @JsonProperty("transfer_id")
    private Integer transferId;

    @JsonProperty("transfer_amount")
    private Double transferAmount;

    @JsonProperty("currency_type")
    private String currencyType;

    @JsonProperty("transfer_status")
    private String transferStatus;

    @JsonProperty("transfer_date")
    private Date transferDate;

    @JsonProperty("origin_account_id")
    private Integer originAccountId;

    @JsonProperty("receiving_account_id")
    private Integer receivingAccountId;
}
