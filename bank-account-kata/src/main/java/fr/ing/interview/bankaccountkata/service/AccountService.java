package fr.ing.interview.bankaccountkata.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.ing.interview.bankaccountkata.exception.AccountNotFoundException;
import fr.ing.interview.bankaccountkata.exception.InsufficientDepositException;
import fr.ing.interview.bankaccountkata.exception.InsufficientFundException;
import fr.ing.interview.bankaccountkata.model.Account;
import fr.ing.interview.bankaccountkata.model.Transaction;
import fr.ing.interview.bankaccountkata.model.TransactionType;
import fr.ing.interview.bankaccountkata.repository.AccountRepository;
import fr.ing.interview.bankaccountkata.repository.TransactionRepository;

@Component
public class AccountService {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	private final Double DEPOSIT_THRESHOLD = 0.01d;

	public Optional<Account> findById(Long accountId) {
		return accountRepository.findById(accountId);
	}
	
	public List<Account> findAll() {
		return accountRepository.findAll();
	}
	
	public List<Transaction> findAccountAllTransactions(Long accountId){
		return accountRepository.findById(accountId).get().getTransactions();
	}

	public List<Transaction> withdrawal(Long accountId, BigDecimal amount) throws InsufficientFundException, AccountNotFoundException {
		Optional<Account> accountOptional = accountRepository.findById(accountId);
		Account account = null;
		if(accountOptional.isPresent()) {
			account = accountOptional.get();
		} else throw new AccountNotFoundException("Account not found");

		if (amount.doubleValue() > account.getBalance().doubleValue()) {
			throw new InsufficientFundException(
					"Withdraw money from a customer account, is allowed when no overdraft used");
		}

		return processTransaction(account, amount, TransactionType.WITHDRAWAL);
	}

	public List<Transaction> deposit(Long accountId, BigDecimal amount) throws InsufficientDepositException, AccountNotFoundException {
		if (amount.doubleValue() <= DEPOSIT_THRESHOLD) {
			throw new InsufficientDepositException(
					"Deposit money from a customer to his account, is allowed when superior to €0.01");
		}

		Optional<Account> accountOptional = accountRepository.findById(accountId);
		Account account = null;
		if(accountOptional.isPresent()) {
			account = accountOptional.get();
		} else throw new AccountNotFoundException("Account not found");

		return processTransaction(account, amount, TransactionType.DEPOSIT);

	}

	private List<Transaction> processTransaction(Account account, BigDecimal amount, TransactionType transactionType) {
		Transaction transaction = new Transaction(new Date(), transactionType, amount, account.getBalance(), account);
		BigDecimal currentBalance = account.getBalance()
				.add(transactionType.equals(TransactionType.WITHDRAWAL) ? amount.negate() : amount);
		account.setBalance(currentBalance);
		transaction.setCurrentBalance(currentBalance);
		account.getTransactions().add(transaction);
		transactionRepository.save(transaction);
		accountRepository.save(account);
		return account.getTransactions();
	}


}
