package org.example.springsecurity.handlers;

import org.example.springsecurity.responses.CurrencyResponse;

public interface IDemoService {
    CurrencyResponse getExchangeRateSpot();
}
