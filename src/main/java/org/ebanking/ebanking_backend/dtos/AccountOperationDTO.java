package org.ebanking.ebanking_backend.dtos;

import jakarta.persistence.*;
import lombok.Data;
import org.ebanking.ebanking_backend.entities.BankAccount;
import org.ebanking.ebanking_backend.enums.OperationType;

import java.util.Date;

@Data
public class AccountOperationDTO {
    private Long id;
    private Date operationDate;
    private  double amount;
    private OperationType type;
    private String description;
}
