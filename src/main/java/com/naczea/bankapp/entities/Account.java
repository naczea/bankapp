package com.naczea.bankapp.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message ="The field [number-Account] is required")
    @Column(length = 6)
    private Long number;

    @NotNull(message = "The field [type-Account] is required")
    @Enumerated(EnumType.STRING)
    private AccountType type;

    @NotNull(message ="The field [state-Account] is required")
    private Boolean state;

    @NotNull(message ="The field [openingBalance-Account] is required")
    private BigDecimal openingBalance;

    @ManyToOne
    private Client client;
}
