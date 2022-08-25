package nl.rabobank.payment.core.dtos;

import lombok.Builder;
import lombok.Data;
import nl.rabobank.payment.core.entities.CardType;

@Data
public class TransactionDTO {
    private double amount;
    private CardType paymentMethod;
}
