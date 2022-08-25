package nl.rabobank.payment.core.dtos;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import nl.rabobank.payment.core.entities.Card;
import nl.rabobank.payment.core.entities.TransactionType;

import java.time.OffsetDateTime;

@Data
@Builder
public class TransactionDetailsDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long toAccount;
    private Card paymentCard;
    private TransactionType type;
    private double balanceBeforeTransaction;
    private double amount;
    private double fee;
    private double newBalance;
    private OffsetDateTime time;
}
