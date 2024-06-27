package org.ebanking.ebanking_backend.dtos;

import lombok.Data;
import org.ebanking.ebanking_backend.enums.AccountStatus;

import java.util.Date;

@Data
public class CurrentBankAccountDTO extends BankAccountDTO{
    private String id;
    private Date createdAt;
    private double balance;
    private String currency;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private double overDraft;
}
