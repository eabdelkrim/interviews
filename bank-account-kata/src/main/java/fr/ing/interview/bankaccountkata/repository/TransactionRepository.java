package fr.ing.interview.bankaccountkata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.ing.interview.bankaccountkata.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>  {

}
