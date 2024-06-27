package org.ebanking.ebanking_backend.dtos;

import lombok.Data;
import org.ebanking.ebanking_backend.enums.AccountStatus;

import java.util.Date;

@Data
public class SavingBankAccountDTO extends BankAccountDTO{
    private String id;
    private Date createdAt;
    private double balance;
    private String currency;
    private AccountStatus status;
    private CustomerDTO customerDTO;
   // private String customerName;  si j'ai besaoin juste s'un attribut de l'objet DTO je peux j'ajouter sans ajouter tout l'objet
    private double interestRate;
}
