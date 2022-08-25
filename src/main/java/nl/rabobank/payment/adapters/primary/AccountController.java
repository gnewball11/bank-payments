package nl.rabobank.payment.adapters.primary;


import nl.rabobank.payment.core.dtos.TransactionDTO;
import nl.rabobank.payment.core.dtos.TransactionDetailsDTO;
import nl.rabobank.payment.core.dtos.TransferDTO;
import nl.rabobank.payment.core.entities.Account;
import nl.rabobank.payment.core.entities.Transaction;
import nl.rabobank.payment.core.exceptions.TransactionException;
import nl.rabobank.payment.core.ports.IAccountService;
import nl.rabobank.payment.core.services.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
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
