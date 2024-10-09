package org.example.api.data.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    private String accountType;
    private Boolean isBlocked;
    private Boolean isInDebt;
    private Double amount;
    private Date creationDate;
    private Date expirationDate;

    @ManyToOne
    @JoinColumn(name = "customerId", nullable = false)
    private Customer customer;  // N accounts - 1 customer

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Card> cards;    // 1 account - N cards
}
