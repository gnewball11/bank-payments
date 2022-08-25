package nl.rabobank.payment.core.ports;

import nl.rabobank.payment.core.dtos.TransactionDTO;
import nl.rabobank.payment.core.dtos.TransferDTO;
import nl.rabobank.payment.core.entities.Account;
import nl.rabobank.payment.core.entities.Transaction;

import java.util.List;

public interface IAccountService {

    List<Account> getAccounts();

    List<Transaction> getTransactionsByAccount(Long account_number);
    Transaction transfer(Long fromAccountNumber, TransferDTO transferDTO);

    Transaction withdrawal(Long fromAccountNumber, TransactionDTO transactionDTO);
}
