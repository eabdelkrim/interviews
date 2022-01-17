package fr.ing.interview.bankaccountkata.exception;

@SuppressWarnings("serial")
public class InsufficientDepositException extends Exception {

	public InsufficientDepositException(String message) {
		super(message);
	}

}
