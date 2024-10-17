package org.example.apicalls.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class TransferDTO {
    @JsonProperty("transferId")
    private Integer transferId;

    @JsonProperty("transferAmount")
    private Double transferAmount;

    @JsonProperty("currencyType")
    private String currencyType;

    @JsonProperty("transferStatus")
    private String transferStatus;

    @JsonProperty("transferDate")
    private Date transferDate;

    @JsonProperty("originAccountId")
    private Integer originAccountId;

    @JsonProperty("receivingAccountId")
    private Integer receivingAccountId;
}
