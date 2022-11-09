package nl.bank.payment.adapters.repositories;

import nl.bank.payment.domain.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository  extends JpaRepository<Transaction, Long>{

    List<Transaction> findByOriginAccountId(Long accountId);

}