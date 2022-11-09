package nl.bank.payment.adapters.repositories;

import nl.bank.payment.domain.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository  extends JpaRepository<Account, Long> {}