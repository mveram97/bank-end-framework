package org.example.api.data.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity

public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;

    private String type;
    private Long number;
    private int cvc;
    private Date expirationDate;

    @ManyToOne
    @JoinColumn(name = "accountId", nullable = false)  // no puede existir una tarjeta sin  una cuenta asociada
    private Account account;  // N accounts - 1 customer


}
