package org.example.api.data.request;

import lombok.Data;

import java.util.Date;

@Data
public class CardRequest {
    private String type;
    private Date date;
    private int accountId;
}
