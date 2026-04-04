package org.example.springsecurity.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyResponse {
    private BigDecimal rate;
    private BigDecimal amount;
    private BigDecimal amountQuote;
    private String errorCode;
}
