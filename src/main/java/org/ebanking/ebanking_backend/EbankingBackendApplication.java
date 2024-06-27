package org.ebanking.ebanking_backend;

import org.ebanking.ebanking_backend.dtos.BankAccountDTO;
import org.ebanking.ebanking_backend.dtos.CurrentBankAccountDTO;
import org.ebanking.ebanking_backend.dtos.CustomerDTO;
import org.ebanking.ebanking_backend.dtos.SavingBankAccountDTO;
import org.ebanking.ebanking_backend.entities.*;
import org.ebanking.ebanking_backend.enums.AccountStatus;
import org.ebanking.ebanking_backend.enums.OperationType;
import org.ebanking.ebanking_backend.exceptions.BalanceNotSufficientException;
import org.ebanking.ebanking_backend.exceptions.BankAccountNotFoundException;
import org.ebanking.ebanking_backend.exceptions.CustomerNotFoundException;
import org.ebanking.ebanking_backend.repositories.AccountOperationRepository;
import org.ebanking.ebanking_backend.repositories.BankAccountRepository;
import org.ebanking.ebanking_backend.repositories.CustomerRepository;
import org.ebanking.ebanking_backend.services.BankAccountService;
import org.ebanking.ebanking_backend.services.BankService;
import org.hibernate.sql.ast.SqlTreeCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbankingBackendApplication.class, args);
	}
	@Bean
	CommandLineRunner commandLineRunner(BankAccountService bankAccountService){
		return args -> {
			//on cree les customers
			Stream.of("modric","kross","casemiro").forEach(name->{
				CustomerDTO customer = new CustomerDTO();
				customer.setName(name);
				customer.setEmail(name+"@gmail.com");
				bankAccountService.saveCustomer(customer);
			});
			// pour chaque customer on cree 1 currentAccount et un savingAccount
			bankAccountService.listCustomers().forEach(customer -> {
				try {
					bankAccountService.saveCurrentBankAccount(Math.random()*50000,20000,customer.getId());
					bankAccountService.saveSavingBankAccount(Math.random()*50000,5.5,customer.getId());

			/*//pour chaque compte on créé les opérations
					List<BankAccountDTO> bankAccounts = bankAccountService.listBankAccount();
					for(BankAccountDTO bankAccount:bankAccounts){
						for(int i=0; i<10 ;i++){
							String accountId;
							if(bankAccount instanceof SavingBankAccountDTO){
								accountId = ((SavingBankAccountDTO) bankAccount).getId();
							}
							else {
								accountId = ((CurrentBankAccountDTO) bankAccount).getId();
							}
							bankAccountService.credit(accountId,50000+Math.random()*120000,"credit");
							bankAccountService.debit(accountId,1000+Math.random()*9000,"debit");
						}

					}*/
				} catch (CustomerNotFoundException e) {
					e.printStackTrace();
				}

				/*catch (BankAccountNotFoundException | BalanceNotSufficientException e) {
					e.printStackTrace();
				}*/

			});

			List<BankAccountDTO> bankAccounts = bankAccountService.listBankAccount();
			for(BankAccountDTO bankAccount:bankAccounts){
				for(int i=0; i<10 ;i++){
					String accountId;
					if(bankAccount instanceof SavingBankAccountDTO){
						accountId = ((SavingBankAccountDTO) bankAccount).getId();
					}
					else {
						accountId = ((CurrentBankAccountDTO) bankAccount).getId();
					}
					bankAccountService.credit(accountId,50000+Math.random()*120000,"credit");
					bankAccountService.debit(accountId,1000+Math.random()*9000,"debit");
				}
			}

		};


	}
}
