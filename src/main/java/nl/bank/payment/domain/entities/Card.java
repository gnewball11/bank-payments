package nl.bank.payment.domain.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "card")
@Data
public class Card {

    @Id
    private Long id;
    @Enumerated(EnumType.STRING)
    private CardType type;
    @Column(name = "issue_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime issueDate;
    @Column(name = "expiration_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime expirationDate;
}
