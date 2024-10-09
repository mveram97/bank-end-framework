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

    @ManyToMany(mappedBy = "card", cascade = CascadeType.ALL)
    private List<Card> cards;


}
