package org.ebanking.ebanking_backend.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ebanking.ebanking_backend.dtos.AccountHistoryDTO;
import org.ebanking.ebanking_backend.dtos.AccountOperationDTO;
import org.ebanking.ebanking_backend.dtos.BankAccountDTO;
import org.ebanking.ebanking_backend.exceptions.BankAccountNotFoundException;
import org.ebanking.ebanking_backend.services.BankAccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class BankAccountRestController {
    private BankAccountService bankAccountService;

    @GetMapping("/accounts/{accountId}")
    public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(accountId);
    }

    @GetMapping("/accounts")
    public List<BankAccountDTO> listAccounts(){
        return bankAccountService.listBankAccount();
    }

    @GetMapping("/accounts/{accountId}/operations")
    public List<AccountOperationDTO> getHistory(@PathVariable String accountId){
        return bankAccountService.accountHistory(accountId);
    }

    @GetMapping("/accounts/{accountId}/pageOperations") //@RequestParam permet d'envoyer une requete qvec parametre ; 5 = afficher les 5 premieres operations
    public AccountHistoryDTO getAccountHistory(@PathVariable String accountId,
                                                     @RequestParam(name="page",defaultValue="0")int page,
                                                     @RequestParam(name="page",defaultValue="5")int size) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistory(accountId,page,size);
    }

    //il reste faire les operations de debit et credit (saveDebit et SaveDebit et virement)
}
