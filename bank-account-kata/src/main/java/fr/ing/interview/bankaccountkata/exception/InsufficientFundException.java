package fr.ing.interview.bankaccountkata.exception;

@SuppressWarnings("serial")
public class InsufficientFundException extends Exception {

	public InsufficientFundException(String message) {
		super(message);
	}

}
