package nl.bank.payment.domain.services.account;


import nl.bank.payment.domain.dtos.TransactionDTO;
import nl.bank.payment.domain.dtos.TransferDTO;
import nl.bank.payment.domain.entities.Account;
import nl.bank.payment.domain.entities.CardType;
import nl.bank.payment.domain.entities.Transaction;
import nl.bank.payment.domain.entities.TransactionType;
import nl.bank.payment.domain.exceptions.TransactionException;
import nl.bank.payment.adapters.repositories.AccountRepository;
import nl.bank.payment.adapters.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class AccountServiceImpl implements IAccountService {


    private Map<CardType, Function<Transaction, Transaction>> feeRules;

    public AccountServiceImpl(){
        feeRules = Map.of(CardType.CREDIT, (transaction) -> {transaction.setFee(transaction.getAmount() * .01); return transaction;},
                          CardType.DEBIT,  (transaction) -> {transaction.setFee(0); return transaction;}
                        );
    }

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;


    public List<Account> getAccounts(){
        return accountRepository.findAll();
    }

    @Override
    public List<Transaction> getTransactionsByAccount(Long account_number) {
        return transactionRepository.findByOriginAccountId(account_number);
    }

    @Transactional
    public Transaction transfer(Long fromAccountNumber, TransferDTO transferDTO){


        Account originAccount = accountRepository.findById(fromAccountNumber).orElseThrow(() -> new TransactionException("The account: " + fromAccountNumber + "was not found"));
        Account destinationAccount = accountRepository.findById(transferDTO.getToAccountNumber()).orElseThrow(() -> new TransactionException("The account: " + transferDTO.getToAccountNumber() + "was not found"));


        Transaction transaction = new Transaction();
        transaction.setAmount(transferDTO.getAmount());
        transaction.setPaymentCard(originAccount.getCardBasedOnPaymentMethod(transferDTO.getPaymentMethod()).orElseThrow(() -> new TransactionException("Your account does not support " + transferDTO.getPaymentMethod() + " payments")));

        feeRules.get(transferDTO.getPaymentMethod()).apply(transaction);

        double originAccountNewBalance = originAccount.getBalance() - (transaction.getAmount() + transaction.getFee());
        if(originAccountNewBalance < 0){
            throw new TransactionException("Your current balance: " + originAccount.getBalance() + " is not enough for the requested amount: "+ originAccountNewBalance +" to be withdrawn ");
        }


        transaction.setTime(OffsetDateTime.now());
        transaction.setBalanceBeforeTransaction(originAccount.getBalance());
        transaction.setType(TransactionType.TRANSFER);
        transaction.setNewBalance(originAccountNewBalance);
        transaction.setOriginAccount(originAccount);
        transaction.setDestinationAccount(destinationAccount);

        originAccount.setBalance(originAccountNewBalance);
        accountRepository.save(originAccount);

        destinationAccount.setBalance(destinationAccount.getBalance() + transaction.getAmount());
        accountRepository.save(destinationAccount);

        transactionRepository.save(transaction);

        return transaction;
    }
    @Transactional
    public Transaction withdrawal(Long fromAccountNumber, TransactionDTO transactionDTO){

        Account account = accountRepository.findById(fromAccountNumber).orElseThrow(() -> new TransactionException("The account: " + fromAccountNumber + "was not found"));

        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setPaymentCard(account.getCardBasedOnPaymentMethod(transactionDTO.getPaymentMethod()).orElseThrow(() -> new TransactionException("Your account does not support " + transactionDTO.getPaymentMethod() + " payments")));

        feeRules.get(transactionDTO.getPaymentMethod()).apply(transaction);

        double newBalance = account.getBalance() - (transaction.getAmount() + transaction.getFee());
        if(newBalance < 0){
            throw new TransactionException("Your current balance: " + account.getBalance() + " is not enough for the requested amount: "+ newBalance +" to be withdrawn ");
        }

        transaction.setTime(OffsetDateTime.now());
        transaction.setBalanceBeforeTransaction(account.getBalance());
        transaction.setType(TransactionType.WITHDRAWAL);
        transaction.setNewBalance(newBalance);

        transaction.setOriginAccount(account);

        account.setBalance(newBalance);
        accountRepository.save(account);

        transactionRepository.save(transaction);

        return transaction;
    }


}
