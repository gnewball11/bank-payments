package nl.rabobank.payment.core.exceptions;

public class TransactionException extends RuntimeException {
    public TransactionException(String errorMessage) {
        super(errorMessage);
    }
}