package fr.ing.interview.bankaccountkata.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Transaction {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "TRANSACTION_DATE")
	private Date date;
	
	@Enumerated(EnumType.ORDINAL)
	private TransactionType transactionType;
	
	
	
	private BigDecimal amount;
	
	private BigDecimal currentBalance;
	
	@ManyToOne
	@JoinColumn(name = "ACCOUNT_ID")
	//@JsonIgnore
	private Account account;
	
	@Transient
	private String transactionTypeLabel;

	public Transaction() {
		
	}
	
	public Transaction(BigDecimal amount) {
		super();
		this.amount = amount;
	}
	
	
	public Transaction(Date date, TransactionType transactionType, BigDecimal amount,
			BigDecimal currentBalance, Account account) {
		super();
		this.date = date;
		this.transactionType = transactionType;
		this.amount = amount;
		this.currentBalance = currentBalance;
		this.account = account;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(BigDecimal currentBalance) {
		this.currentBalance = currentBalance;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String getTransactionTypeLabel() {
		return transactionType.type;
	}	
	
	public void setTransactionTypeLabel() {
		transactionTypeLabel = transactionType.type;
	}
}
