package org.example.apicalls.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class CardDTO {
    @JsonProperty("card_id")
    private Integer cardId;

    @JsonProperty("type")
    private String type;

    @JsonProperty("number")
    private Long number;

    @JsonProperty("expiration_date")
    private Date expirationDate;

    @JsonProperty("account_id")
    private Integer accountId;
}
