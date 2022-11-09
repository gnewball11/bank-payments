package nl.bank.payment.domain.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.OffsetDateTime;
@Entity
@Table(name = "transaction")
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "origin_account_id")
    private Account originAccount;
    @ManyToOne
    @JoinColumn(name = "destination_account_id")
    private Account destinationAccount;
    @OneToOne
    @JoinColumn(name = "card_id")
    private Card paymentCard;
    @Column(name = "balance_before_transaction")
    private double balanceBeforeTransaction;
    private double amount;
    private double fee;
    @Column(name = "new_balance")
    private double newBalance;
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    @Column(name = "time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime time;
}
