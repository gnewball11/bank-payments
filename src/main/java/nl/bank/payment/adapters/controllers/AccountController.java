package nl.bank.payment.adapters.controllers;


import nl.bank.payment.domain.dtos.TransactionDTO;
import nl.bank.payment.domain.dtos.TransferDTO;
import nl.bank.payment.domain.entities.Account;
import nl.bank.payment.domain.dtos.TransactionDetailsDTO;
import nl.bank.payment.domain.entities.Transaction;
import nl.bank.payment.domain.exceptions.TransactionException;
import nl.bank.payment.domain.services.account.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("bank/api/v1")
public class AccountController {



    @Autowired
    IAccountService accountService;

    @GetMapping(value = "/accounts")
    public List<Account> getAccounts() {
        return accountService.getAccounts();
    }

    @GetMapping(value = "/accounts/{accountNumber}/transactions")
    public ResponseEntity<List<TransactionDetailsDTO>> getTransactions(@PathVariable("accountNumber") Long accountNumber) {

        return ResponseEntity.ok(accountService.getTransactionsByAccount(accountNumber)
                                                .stream()
                                                .map(transaction -> TransactionDetailsDTO.builder()
                                                                                        .paymentCard(transaction.getPaymentCard())
                                                                                        .fee(transaction.getFee())
                                                                                        .type(transaction.getType())
                                                                                        .time(transaction.getTime())
                                                                                        .amount(transaction.getAmount())
                                                                                        .balanceBeforeTransaction(transaction.getBalanceBeforeTransaction())
                                                                                        .newBalance(transaction.getNewBalance())
                                                                                        .toAccount(transaction.getDestinationAccount().getId())
                                                                                        .build())
                                                .collect(Collectors.toList()));
    }

    @PostMapping(value = "/accounts/{accountNumber}/withdraw")
    public ResponseEntity<TransactionDetailsDTO> withdraw(@PathVariable("accountNumber") Long accountNumber, @RequestBody TransactionDTO transactionDTO ) {
        try {
            Transaction transaction = accountService.withdrawal(accountNumber, transactionDTO);

           return ResponseEntity.ok(TransactionDetailsDTO.builder()
                                                        .fee(transaction.getFee())
                                                        .newBalance(transaction.getNewBalance())
                                                        .paymentCard(transaction.getPaymentCard())
                                                        .type(transaction.getType())
                                                        .time(transaction.getTime())
                                                        .amount(transaction.getAmount())
                                                        .balanceBeforeTransaction(transaction.getBalanceBeforeTransaction())
                                                        .build());
        }
        catch (TransactionException exc) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exc.getMessage(), exc);
        }
    }

    @PostMapping(value = "/accounts/{accountNumber}/transfer")
    public ResponseEntity<TransactionDetailsDTO> transfer(@PathVariable("accountNumber") Long accountNumber, @RequestBody TransferDTO transferDTO ) {
        try {
            Transaction transaction = accountService.transfer(accountNumber, transferDTO);

            return ResponseEntity.ok(TransactionDetailsDTO.builder()
                    .fee(transaction.getFee())
                    .newBalance(transaction.getNewBalance())
                    .paymentCard(transaction.getPaymentCard())
                    .type(transaction.getType())
                    .toAccount(transaction.getDestinationAccount().getId())
                    .time(transaction.getTime())
                    .amount(transaction.getAmount())
                    .balanceBeforeTransaction(transaction.getBalanceBeforeTransaction())
                    .build());
        }
        catch (TransactionException exc) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exc.getMessage(), exc);
        }
    }
}
