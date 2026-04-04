package org.example.springsecurity.handlers.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.springsecurity.handlers.IDemoService;
import org.example.springsecurity.responses.CurrencyResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.example.springsecurity.utils.ConverterUntil.objectToString;
import static org.example.springsecurity.utils.CurrencyUtils.roundByCurrency;

@Slf4j
@Service
public class DemoServiceImpl implements IDemoService {
    @Override
    public CurrencyResponse getExchangeRateSpot() {
        CurrencyResponse response = new CurrencyResponse();

        // Mock data sát thực tế: giá bán chuyển khoản USD/VND
        BigDecimal rate = new BigDecimal("26154.00");
        BigDecimal tolerance = new BigDecimal("12");    // FX_TOLERANCE = 1,000,000 VND
        BigDecimal transferAmount = new BigDecimal("200000000"); // 200,000,000 VND

        log.debug("FX_TOLERANCE: [{}]", tolerance);

        String[] currencyPair = "USD/VND".split("/");
        String foreignCurrency = currencyPair[0]; // USD
        String domesticCurrency = currencyPair[1]; // VND
        log.debug("CURRENCY PAIR: foreignCurrency=[{}], domesticCurrency=[{}]",
                foreignCurrency, domesticCurrency);

        // Giá trị ngoại tệ (USD) = transferAmount(VND) / rate
        BigDecimal netAmountFx = roundByCurrency(
                transferAmount.divide(rate, 10, RoundingMode.DOWN),
                foreignCurrency
        );
        log.info("CALCULATED NET AMOUNT FX: transferAmount [{}], rate [{}], netAmountFx [{}]", transferAmount, rate, netAmountFx);

        // Giá trị quy đổi (VND) = rate * netAmountFx
        BigDecimal amountQuoteVnd = roundByCurrency(
                rate.multiply(netAmountFx),
                domesticCurrency
        );
        log.info("CALCULATED AMOUNT QUOTE VND: rate [{}], netAmountFx [{}], amountQuote Vnd [{}]", rate, netAmountFx, amountQuoteVnd);

        // X = amountQuoteVnd - transferAmount
        BigDecimal amount = amountQuoteVnd.subtract(transferAmount);
        log.debug("TOLERANCE CHECK: amount [{}], tolerance [{}]", amount, tolerance);

        String errorCode = "";

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            errorCode = "IBS.1.58.068.2.8126";
            log.warn("TH1 - AMOUNT QUOTE LESS THAN TRANSFER AMOUNT: amount = [{}], errorCode [{}]", amount, errorCode);

        } else if (amount.compareTo(BigDecimal.ZERO) > 0 && amount.compareTo(tolerance) >= 0) {
            errorCode = "IBS.1.58.068.2.8127";
            log.warn("TH2 - EXCEEDED TOLERANCE: amount [{}], tolerance [{}], errorCode [{}]", amount, tolerance, errorCode);

        } else {
            log.info("EXACT MATCH: amountQuote Vnd [{}] == transferAmount [{}]", amountQuoteVnd, transferAmount);
        }

        response.setRate(rate);
        response.setAmount(netAmountFx);
        response.setAmountQuote(amountQuoteVnd);
        response.setErrorCode(errorCode);

        log.info("END GET EXCHANGE RATE SPOT: response [{}]", objectToString(response));
        return response;
    }
}
