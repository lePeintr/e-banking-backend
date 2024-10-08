package org.ebanking.ebanking_backend.services;

import org.ebanking.ebanking_backend.dtos.*;
import org.ebanking.ebanking_backend.entities.BankAccount;
import org.ebanking.ebanking_backend.entities.CurrentAccount;
import org.ebanking.ebanking_backend.entities.Customer;
import org.ebanking.ebanking_backend.entities.SavingAccount;
import org.ebanking.ebanking_backend.exceptions.BalanceNotSufficientException;
import org.ebanking.ebanking_backend.exceptions.BankAccountNotFoundException;
import org.ebanking.ebanking_backend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
   SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    List<CustomerDTO> listCustomers();
    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;
    void debit(String accountId, double amount, String description ) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void credit(String accountId, double amount, String description ) throws BankAccountNotFoundException;
    void transfert(String accountSource, String accountDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;
    List<BankAccountDTO> listBankAccount();
    CustomerDTO getCustomer(Long CustomerID) throws CustomerNotFoundException;

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long customerId);

    List<AccountOperationDTO> accountHistory(String accountId);

    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;

    List<CustomerDTO> listSearchCustomers(String keyword);
}
