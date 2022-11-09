package nl.bank.payment.domain.services.account;

import nl.bank.payment.domain.dtos.TransactionDTO;
import nl.bank.payment.domain.dtos.TransferDTO;
import nl.bank.payment.domain.entities.Account;
import nl.bank.payment.domain.entities.Transaction;

import java.util.List;

public interface IAccountService {

    List<Account> getAccounts();

    List<Transaction> getTransactionsByAccount(Long account_number);
    Transaction transfer(Long fromAccountNumber, TransferDTO transferDTO);

    Transaction withdrawal(Long fromAccountNumber, TransactionDTO transactionDTO);
}
