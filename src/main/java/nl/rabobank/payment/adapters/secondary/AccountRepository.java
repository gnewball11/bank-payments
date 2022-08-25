package nl.rabobank.payment.adapters.secondary;

import nl.rabobank.payment.core.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository  extends JpaRepository<Account, Long> {}