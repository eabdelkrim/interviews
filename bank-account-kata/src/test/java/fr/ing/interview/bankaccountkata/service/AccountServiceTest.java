package fr.ing.interview.bankaccountkata.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.ing.interview.bankaccountkata.exception.AccountNotFoundException;
import fr.ing.interview.bankaccountkata.exception.InsufficientDepositException;
import fr.ing.interview.bankaccountkata.exception.InsufficientFundException;
import fr.ing.interview.bankaccountkata.model.Account;
import fr.ing.interview.bankaccountkata.model.Transaction;
import fr.ing.interview.bankaccountkata.model.TransactionType;
import fr.ing.interview.bankaccountkata.repository.AccountRepository;
import fr.ing.interview.bankaccountkata.repository.TransactionRepository;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

	@InjectMocks
	AccountService accountService;

	@Mock
	AccountRepository accountRepository;

	@Mock
	TransactionRepository transactionRepository;

	@Test
	public void deposit_should_accept_deposit_superior_to_centime() throws InsufficientDepositException, AccountNotFoundException {
		Account account = new Account(BigDecimal.valueOf(1000));
		account.setId(1000L);
		List<Transaction> transactions = null;
		when(accountService.findById(1000L)).thenReturn(Optional.of(account));
		BigDecimal currentAccountBalance = account.getBalance();
		transactions = accountService.deposit(account.getId(), BigDecimal.valueOf(1000));
		assertEquals(transactions.get(transactions.size() - 1).getAmount().intValue(), 1000);
		assertEquals(transactions.get(transactions.size() - 1).getTransactionType(), TransactionType.DEPOSIT);
		assertEquals(transactions.get(transactions.size() - 1).getAccount().getId(), account.getId());
		assertEquals(transactions.get(transactions.size() - 1).getAccount().getBalance(), currentAccountBalance.add(BigDecimal.valueOf(1000)));
	}

	@Test
	public void deposit_should_throw_InsufficientDepositException() {
		assertThrows(InsufficientDepositException.class, () -> {
			Account account = new Account(BigDecimal.valueOf(1000));
			account.setId(1000L);
			accountService.deposit(account.getId(), BigDecimal.valueOf(0.01));
		});
	}

	@Test
	public void deposit_should_withdrawal() throws InsufficientFundException, AccountNotFoundException {
		Account account = new Account(BigDecimal.valueOf(2000));
		account.setId(1000L);
		List<Transaction> transactions = null;
		when(accountService.findById(1000L)).thenReturn(Optional.of(account));
		BigDecimal currentAccountBalance = account.getBalance();
		transactions = accountService.withdrawal(account.getId(), BigDecimal.valueOf(1000));
		assertEquals(transactions.get(transactions.size() - 1).getAmount().intValue(), 1000);
		assertEquals(transactions.get(transactions.size() - 1).getTransactionType(), TransactionType.WITHDRAWAL);
		assertEquals(transactions.get(transactions.size() - 1).getAccount().getId(), account.getId());
		assertEquals(transactions.get(transactions.size() - 1).getAccount().getBalance(),
				currentAccountBalance.add(BigDecimal.valueOf(1000).negate()));
	}

	@Test
	public void withdrawal_should_throw_InsufficientFundException() {
		assertThrows(InsufficientFundException.class, () -> {
			Account account = new Account(BigDecimal.valueOf(1000));
			account.setId(1000L);
			when(accountService.findById(1000L)).thenReturn(Optional.of(account));
			accountService.withdrawal(account.getId(), BigDecimal.valueOf(1001));
		});
	}

}
