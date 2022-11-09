package nl.bank.payment.domain.dtos;

import lombok.Data;

@Data
public class TransferDTO extends TransactionDTO{
    private Long ToAccountNumber;
}
