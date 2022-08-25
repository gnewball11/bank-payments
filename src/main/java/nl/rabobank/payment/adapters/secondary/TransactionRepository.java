package nl.rabobank.payment.adapters.secondary;

import nl.rabobank.payment.core.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository  extends JpaRepository<Transaction, Long>{

    List<Transaction> findByOriginAccountId(Long accountId);

}