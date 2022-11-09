package nl.bank.payment.domain.entities;


import lombok.Data;

import javax.persistence.*;
import java.util.Map;
import java.util.Optional;

@Entity
@Data
@Table(name = "account")
public class Account {

    @Id
    private Long id;
    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToOne
    @JoinColumn(name = "debit_card_id")
    private Card debitCard;

    @OneToOne
    @JoinColumn(name = "credit_card_id")
    private Card creditCard;

    private double balance;

    public Optional<Card> getCardBasedOnPaymentMethod(CardType paymentMethod){
        Map<CardType, Optional<Card>> cards = Map.of(CardType.CREDIT, Optional.ofNullable(creditCard), CardType.DEBIT, Optional.ofNullable(debitCard));
        return cards.get(paymentMethod);
    }
}
