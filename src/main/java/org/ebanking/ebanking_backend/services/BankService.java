package org.ebanking.ebanking_backend.services;

import org.ebanking.ebanking_backend.entities.BankAccount;
import org.ebanking.ebanking_backend.entities.CurrentAccount;
import org.ebanking.ebanking_backend.entities.SavingAccount;
import org.ebanking.ebanking_backend.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BankService {
    @Autowired
    BankAccountRepository bankAccountRepository;

    public  void consulter(){
        BankAccount bankAccount = bankAccountRepository.findById("499de9c3-4020-497a-ae98-3b799a8deec1").orElse(null);
        if (bankAccount != null) {
            System.out.println(bankAccount.getId());
            System.out.println(bankAccount.getBalance());
            System.out.println(bankAccount.getStatus());
            System.out.println(bankAccount.getCreatedAt());
            System.out.println(bankAccount.getCustomer().getName());
            System.out.println(bankAccount.getClass().getSimpleName());
            if (bankAccount instanceof CurrentAccount) {
                System.out.println("Over Draft=>" + ((CurrentAccount) bankAccount).getOverDraft());
            } else if (bankAccount instanceof SavingAccount)
                System.out.println("Rate=>" + ((SavingAccount) bankAccount).getInterestRate());

            bankAccount.getAccountOperationList().forEach(op -> {
                System.out.println(op.getType() + "\t" + op.getOperationDate() + "\t" + op.getAmount());
            });
        }
    }
}
