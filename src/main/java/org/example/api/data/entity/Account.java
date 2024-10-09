package org.example.api.data.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Generated;

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
    @JoinColumn(name = "customerId", nullable = false)  // no puede existir una cuenta sin  un cliente asociado
    private Customer customer;  // N accounts - 1 customer

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL) // si se elimina una cuenta se eliminan sus tarjetas
    private List<Account> cards;    // 1 account - N cards

    //
}
