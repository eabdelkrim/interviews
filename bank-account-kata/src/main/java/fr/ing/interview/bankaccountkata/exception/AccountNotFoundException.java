package fr.ing.interview.bankaccountkata.exception;

@SuppressWarnings("serial")
public class AccountNotFoundException extends Exception {

	public AccountNotFoundException(String message) {
		super(message);
	}

}
