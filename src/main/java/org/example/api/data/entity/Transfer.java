package org.example.api.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transferId;

    @Column(nullable = false)
    private Double transferAmount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CurrencyType currencyType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransferStatus transferStatus;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)   // fecha y hora
    private Date transferDate;

    @JsonIgnore
    @ManyToOne      // originAccount -> FK a Account
    @JoinColumn(name = "originAccountId", nullable = false)
    private Account originAccount;  // 1 originAccount - N transfers

    @JsonIgnore
    @ManyToOne      // receivingAccount -> FK a Account
    @JoinColumn(name = "receivingAccountId", nullable = false)
    private Account receivingAccount;  // 1 receivingAccount - N transfers

    // Definición del enumerado CurrencyType
    public enum CurrencyType {
        USD,
        EUR
    }

    // Definición del enumerado TransferStatus
    public enum TransferStatus {
        PENDING,
        FAILED,
        SUCCESSFUL
    }

    // Constructor
    public Transfer() {
        this.transferDate = new Date(); // Inicializa con la fecha actual del sistema
        this.transferStatus = TransferStatus.PENDING; // Estado inicial pendiente por defecto
    }
}
