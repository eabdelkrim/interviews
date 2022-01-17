package fr.ing.interview.bankaccountkata.model;

public enum TransactionType {
	DEPOSIT("deposit"),
	WITHDRAWAL("withdrawal");
	
	String type;

	TransactionType(String type) {
		this.type = type;
	}
}