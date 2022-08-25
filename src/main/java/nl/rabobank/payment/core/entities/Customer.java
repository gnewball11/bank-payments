package nl.rabobank.payment.core.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "customer")
@Data
public class Customer {

    @Id
    private Long id;
    private String name;
    @Column(name = "last_name")
    private String lastName;
    private String email;

}
