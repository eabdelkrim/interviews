package fr.ing.interview.bankaccountkata.vaadin.model;

import java.math.BigDecimal;

public class TransactionModel {
	private String transactionType;
	private BigDecimal amount;
	private String transactionDate;
	
	public TransactionModel(String transactionType, BigDecimal amount, String transactionDate) {
		super();
		this.transactionType = transactionType;
		this.amount = amount;
		this.transactionDate = transactionDate;
	}
	
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
}
