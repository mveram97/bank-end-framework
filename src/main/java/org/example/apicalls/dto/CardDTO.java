package org.example.apicalls.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class CardDTO {
    @JsonProperty("cardId")
    private Integer cardId;

    @JsonProperty("type")
    private String type;

    @JsonProperty("number")
    private Long number;

    @JsonProperty("cvc")
    private int cvc;

    @JsonProperty("expirationDate")
    private Date expirationDate;

    @JsonProperty("accountId")
    private Integer accountId;
}
