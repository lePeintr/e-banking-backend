package org.ebanking.ebanking_backend.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ebanking.ebanking_backend.dtos.*;
import org.ebanking.ebanking_backend.entities.*;
import org.ebanking.ebanking_backend.enums.AccountStatus;
import org.ebanking.ebanking_backend.enums.OperationType;
import org.ebanking.ebanking_backend.exceptions.BalanceNotSufficientException;
import org.ebanking.ebanking_backend.exceptions.BankAccountNotFoundException;
import org.ebanking.ebanking_backend.exceptions.CustomerNotFoundException;
import org.ebanking.ebanking_backend.mappers.BankAccountMapperImpl;
import org.ebanking.ebanking_backend.repositories.AccountOperationRepository;
import org.ebanking.ebanking_backend.repositories.BankAccountRepository;
import org.ebanking.ebanking_backend.repositories.CustomerRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor //Annotation Lombock =  Fais l'injection de dependance par constructeur
@Slf4j// Lombock qui remplace la creation de l'ojet log Logger log= LoggerFactory.getLogger(this.getClass().getName());
public class BankAccountServiceImpl implements BankAccountService {

    //@Autowired
    private CustomerRepository customerRepository;
    //@Autowired
    private BankAccountRepository  bankAccountRepository;
    //@Autowired
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl dtoMapper;

    /*public BankAccountServiceImpl(CustomerRepository customerRepository, BankAccountRepository bankAccountRepository, AccountOperationRepository accountOperationRepository) {
        this.customerRepository = customerRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.accountOperationRepository = accountOperationRepository;
    }*/

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving new customer");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer =  customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
       Customer customer = customerRepository.findById(customerId).orElse(null);
       if(customer==null)
        throw  new CustomerNotFoundException("Customer not found");
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setCustomer(customer);
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setBalance(initialBalance);
        currentAccount.setCurrency("euros");
        currentAccount.setStatus(AccountStatus.ACTIVATED);
        currentAccount.setCreatedAt(new Date(2024,06,05));
        currentAccount.setOverDraft(overDraft);
        CurrentAccount savedBankAccount =  bankAccountRepository.save(currentAccount);
        return  dtoMapper.fromCurrentBankAccount(savedBankAccount);
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if(customer==null)
            throw  new CustomerNotFoundException("Customer not found");
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setCustomer(customer);
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setBalance(initialBalance);
        savingAccount.setCurrency("euros");
        savingAccount.setStatus(AccountStatus.ACTIVATED);
        savingAccount.setCreatedAt(new Date(2024,06,05));
        savingAccount.setInterestRate(interestRate);
        SavingAccount savedBankAccount =  bankAccountRepository.save(savingAccount);
        return  dtoMapper.fromSavingBankAccount(savedBankAccount);
    }


    @Override
    public List<CustomerDTO> listCustomers() {
       List<Customer> customers = customerRepository.findAll();
      List<CustomerDTO> customerDTOS= customers.stream()
              .map(customer->dtoMapper.fromCustomer(customer))
              .collect(Collectors.toList());
       /* List<CustomerDTO> customerDTOS = new ArrayList<CustomerDTO>();
        for(Customer customer:customers){
            CustomerDTO customerDTO = dtoMapper.fromCustomer(customer);
            customerDTOS.add(customerDTO);
        }*/
        return customerDTOS;
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
       BankAccount bankAccount= bankAccountRepository.findById(accountId)
               .orElseThrow(()-> new BankAccountNotFoundException("Bank account not found"));
       if(bankAccount instanceof SavingAccount){
           SavingAccount savingAccount = (SavingAccount) bankAccount;
           return dtoMapper.fromSavingBankAccount(savingAccount);
       }else{
           CurrentAccount currentAccount = (CurrentAccount) bankAccount;
           return dtoMapper.fromCurrentBankAccount(currentAccount);
       }

    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount= bankAccountRepository.findById(accountId)
                .orElseThrow(()-> new BankAccountNotFoundException("Bank account not found"));
        if(bankAccount.getBalance() < amount)
            throw new BalanceNotSufficientException("Balance not sufficient");
        //Pour debiter un compte en plus de modifier le solde du compte on cree une operation de type debit dans le compte
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setDescription(description);
        accountOperation.setAmount(amount);
        accountOperation.setOperationDate(new java.util.Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount= bankAccountRepository.findById(accountId)
                .orElseThrow(()-> new BankAccountNotFoundException("Bank account not found"));
        //Pour crediter un compte en plus de modifier le solde du compte on cree une operation de type credit dans le compte
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setDescription(description);
        accountOperation.setAmount(amount);
        accountOperation.setOperationDate(new java.util.Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfert(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
            debit(accountIdSource,amount,"Transfer to "+accountIdDestination);
            credit(accountIdDestination,amount,"credit from "+accountIdSource);
    }

    @Override
    public List<BankAccountDTO> listBankAccount() {
       List<BankAccount> bankAccounts = bankAccountRepository.findAll();
       List<BankAccountDTO> bankAccountDTOS =  bankAccounts.stream()
                .map(bankAccount->{
                    if(bankAccount instanceof SavingAccount){
                        SavingAccount savingAccount = (SavingAccount) bankAccount;
                        return dtoMapper.fromSavingBankAccount(savingAccount);
                    }else{
                        CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                        return dtoMapper.fromCurrentBankAccount(currentAccount);
                    }
                }).collect(Collectors.toList());
         return bankAccountDTOS;
    }

    @Override
    public CustomerDTO getCustomer(Long customerID) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerID)
                .orElseThrow(()->new CustomerNotFoundException("Customer not Found") );
        return dtoMapper.fromCustomer(customer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Saving new customer");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer =  customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public void deleteCustomer(Long customerId){
        customerRepository.deleteById(customerId);
    }

    @Override
    public List<AccountOperationDTO> accountHistory(String accountId){
        List<AccountOperation> accountOperations=  accountOperationRepository.findByBankAccountId(accountId);
        List<AccountOperationDTO> accountOperationDTOS = accountOperations.stream().map(op ->
            dtoMapper.fromAccountOperation(op)
        ).collect(Collectors.toList());
        return accountOperationDTOS;
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElse(null);
        if(bankAccount == null)
            throw new BankAccountNotFoundException("Account not Found");
        Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId, PageRequest.of(page,size));
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        List<AccountOperationDTO>accountOperationDTOS =  accountOperations.getContent().stream().map(op->dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
        accountHistoryDTO.setAccountOperationDTOs(accountOperationDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());

        return accountHistoryDTO;
    }
}
