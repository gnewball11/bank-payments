package nl.bank.payment.unit.service;

import nl.bank.payment.domain.dtos.TransactionDTO;
import nl.bank.payment.domain.dtos.TransferDTO;
import nl.bank.payment.domain.entities.Account;
import nl.bank.payment.domain.entities.Transaction;
import nl.bank.payment.adapters.repositories.AccountRepository;
import nl.bank.payment.adapters.repositories.TransactionRepository;
import nl.bank.payment.domain.entities.Card;
import nl.bank.payment.domain.entities.CardType;
import nl.bank.payment.domain.exceptions.TransactionException;
import nl.bank.payment.domain.services.account.IAccountService;
import nl.bank.payment.domain.services.account.AccountServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class AccountServiceImplTest {


    @TestConfiguration
    static class EmployeeServiceImplTestContextConfiguration {

        @Bean
        public IAccountService accountService() {
            return new AccountServiceImpl();
        }
    }

    @Autowired
    private IAccountService accountService;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private TransactionRepository transactionRepository;

    final static Long ORIGIN_ACCOUNT_ID = 8594858238548595L;
    final static Long DESTINATION_ACCOUNT_ID = 9123748938473392L;

    @Before
    public void setUp() {
        Account account = new Account();
        account.setId(ORIGIN_ACCOUNT_ID);
        account.setBalance(10000);

        Card card = new Card();
        card.setType(CardType.CREDIT);
        account.setCreditCard(card);
        Optional<Account> originAccount = Optional.of(account);

        Account account1 = new Account();
        account1.setId(DESTINATION_ACCOUNT_ID);
        account1.setBalance(25000);

        Card card2 = new Card();
        card.setType(CardType.DEBIT);
        account1.setCreditCard(card2);

        Optional<Account> destinationAccount = Optional.of(account1);

        Mockito.when(accountRepository.findById(DESTINATION_ACCOUNT_ID))
                .thenReturn(destinationAccount);

        Mockito.when(accountRepository.findById(ORIGIN_ACCOUNT_ID))
                .thenReturn(originAccount);
    }

    @Test(expected = TransactionException.class)
    public void testTransactionExceptionWhenWithdrawMakesBalanceNegative() {
       TransactionDTO transactionDTO = new TransactionDTO();
       transactionDTO.setAmount(13000);
       transactionDTO.setPaymentMethod(CardType.CREDIT);
       accountService.withdrawal(ORIGIN_ACCOUNT_ID, transactionDTO);
    }

    @Test(expected = TransactionException.class)
    public void testTransactionExceptionWhenPaymentNotSupported() {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAmount(700);
        transactionDTO.setPaymentMethod(CardType.DEBIT);
        accountService.withdrawal(ORIGIN_ACCOUNT_ID, transactionDTO);
    }


    @Test(expected = TransactionException.class)
    public void testTransactionExceptionWhenAccountNotFound() {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAmount(700);
        transactionDTO.setPaymentMethod(CardType.DEBIT);
        accountService.withdrawal(342354234234L, transactionDTO);
    }

    @Test
    public void testSuccessfulWithdraw() {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAmount(700);
        transactionDTO.setPaymentMethod(CardType.CREDIT);
        Transaction transaction = accountService.withdrawal(ORIGIN_ACCOUNT_ID, transactionDTO);

        assertThat(transaction.getNewBalance()).isEqualTo(9293);
    }


    @Test
    public void testSuccessfulTransfer() {
        TransferDTO transferDTO = new TransferDTO();
        transferDTO.setAmount(700);
        transferDTO.setPaymentMethod(CardType.CREDIT);
        transferDTO.setToAccountNumber(DESTINATION_ACCOUNT_ID);
        Transaction transaction = accountService.transfer(ORIGIN_ACCOUNT_ID, transferDTO);



        assertThat(transaction.getNewBalance()).isEqualTo(9293);
        assertThat(transaction.getDestinationAccount().getBalance()).isEqualTo(25700);
    }
}
