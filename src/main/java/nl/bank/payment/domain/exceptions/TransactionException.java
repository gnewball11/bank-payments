package nl.bank.payment.domain.exceptions;

public class TransactionException extends RuntimeException {
    public TransactionException(String errorMessage) {
        super(errorMessage);
    }
}