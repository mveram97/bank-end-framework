package org.example.api.data.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.example.api.data.entity.Account;
import org.example.api.data.entity.Transfer;
import lombok.Data;
import java.util.Date;

@Data
public class TransferRequest {

    @Column(nullable = false)
    private Double transferAmount;
    private Integer originAccountId;  // 1 originAccount - N transfers
    private Integer receivingAccountId;  // 1 receivingAccount - N transfers


}
