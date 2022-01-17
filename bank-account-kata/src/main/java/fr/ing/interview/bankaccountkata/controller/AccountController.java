package fr.ing.interview.bankaccountkata.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.ing.interview.bankaccountkata.exception.AccountNotFoundException;
import fr.ing.interview.bankaccountkata.exception.InsufficientDepositException;
import fr.ing.interview.bankaccountkata.exception.InsufficientFundException;
import fr.ing.interview.bankaccountkata.model.Account;
import fr.ing.interview.bankaccountkata.model.Transaction;
import fr.ing.interview.bankaccountkata.service.AccountService;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
	
	@Autowired
	private AccountService accountService;
	
	@GetMapping(value="/{accountId}")
	public Account retrieveAccount(@PathVariable Long accountId) throws AccountNotFoundException {
		Optional<Account> accountOptional = accountService.findById(accountId);
		if(!accountOptional.isPresent()) {
			throw new AccountNotFoundException("Account not found");
		}
		
		return accountOptional.get();
	}
	
	@GetMapping(value="")
	public List<Account> retrieveAccounts() {	
		return accountService.findAll();
	}
	
	@GetMapping(value="/{accountId}/transactions")
	public List<Transaction> retrieveAccountTransactions(@PathVariable Long accountId) throws AccountNotFoundException {	
		Optional<Account> accountOptional = accountService.findById(accountId);
		if(!accountOptional.isPresent()) {
			throw new AccountNotFoundException("Account not found");
		}
		
		return accountOptional.get().getTransactions();
	}
	
	@PostMapping(value = "/{accountId}/deposit")
	public List<Transaction> deposit(@PathVariable Long accountId, @RequestBody Transaction transaction) throws InsufficientDepositException, AccountNotFoundException {	
		accountService.deposit(accountId, transaction.getAmount());
		Optional<Account> accountOptional = accountService.findById(accountId);
		if(!accountOptional.isPresent()) {
			throw new AccountNotFoundException("Account not found");
		}
			
		return accountOptional.get().getTransactions();

	}
	
	@PostMapping(value = "/{accountId}/withdrawal")
	public List<Transaction> withdrawal(@PathVariable Long accountId, @RequestBody Transaction transaction) throws InsufficientDepositException, InsufficientFundException, AccountNotFoundException {	
		accountService.withdrawal(accountId, transaction.getAmount());
		Optional<Account> accountOptional = accountService.findById(accountId);
		if(!accountOptional.isPresent()) {
			throw new AccountNotFoundException("Account not found");
		}
		
		return accountOptional.get().getTransactions();
	}

}
