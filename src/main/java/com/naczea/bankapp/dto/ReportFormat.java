package com.naczea.bankapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportFormat {
    Date date;
    String clientName;
    Long numberAccount;
    String type;
    BigDecimal inicialBalance;
    Boolean state;
    BigDecimal value;
    BigDecimal finalBalance;
}
