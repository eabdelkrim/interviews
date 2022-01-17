package fr.ing.interview.bankaccountkata.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Account {
	@Id
	@GeneratedValue
	private long id;
	
	public BigDecimal balance;
	
	@OneToMany(mappedBy = "account", fetch=FetchType.EAGER)
	@JsonIgnore
	private List<Transaction> transactions;

	public Account() {
		
	}
	
	public Account(BigDecimal balance) {
		super();
		this.balance = balance;
		this.transactions = new ArrayList<>();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}
}
