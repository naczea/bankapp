package com.naczea.bankapp.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "The field [dateTime-Movement] is required")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTime;

    @NotNull(message = "The field [type-Movement] is required")
    @Enumerated(EnumType.STRING)
    private AccountType type;

    @NotNull(message = "The field [balance-Movement] is required")
    private BigDecimal balance;

    @NotNull(message = "The field [value-Movement] is required")
    private BigDecimal value;

    @ManyToOne
    private Account account;
}
