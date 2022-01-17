package fr.ing.interview.bankaccountkata.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.ing.interview.bankaccountkata.model.Transaction;
import fr.ing.interview.bankaccountkata.repository.TransactionRepository;

@Component
public class TransactionService {
		
	@Autowired
	private TransactionRepository transactionRepository;
	
	public Optional<Transaction> findById(Long transactionId){
		return transactionRepository.findById(transactionId);		
	}
	
	public List<Transaction> findAll(){
		return transactionRepository.findAll();
	}

	public void save(Transaction transaction) {
		transactionRepository.save(transaction);
		
	}
}
