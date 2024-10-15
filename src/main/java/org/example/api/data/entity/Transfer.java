package org.example.api.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Data
public class Transfer {
    private Integer customerId;
    private Integer customerAccountId;
    private Integer receiverId;
    private Double amount;
    private Date operationDate;
}
