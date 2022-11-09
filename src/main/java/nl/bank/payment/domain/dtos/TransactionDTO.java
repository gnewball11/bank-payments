package nl.bank.payment.domain.dtos;

import lombok.Data;
import nl.bank.payment.domain.entities.CardType;

@Data
public class TransactionDTO {
    private double amount;
    private CardType paymentMethod;
}
