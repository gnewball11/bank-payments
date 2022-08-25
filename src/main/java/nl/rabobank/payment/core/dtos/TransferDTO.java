package nl.rabobank.payment.core.dtos;

import lombok.Builder;
import lombok.Data;

@Data
public class TransferDTO extends TransactionDTO{
    private Long ToAccountNumber;
}
