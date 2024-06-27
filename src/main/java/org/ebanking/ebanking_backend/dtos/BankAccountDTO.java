package org.ebanking.ebanking_backend.dtos;

import jakarta.persistence.*;
import lombok.Data;
import org.ebanking.ebanking_backend.entities.AccountOperation;
import org.ebanking.ebanking_backend.entities.Customer;
import org.ebanking.ebanking_backend.enums.AccountStatus;

import java.util.Date;

@Data
public class BankAccountDTO {
    private String type;
}
