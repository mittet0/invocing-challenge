package com.dt.invocing.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DocumentTests {

    @Test
    public void testEURTargetSameAsCurrentOne() {
        Document invoice = new Invoice(1, 100.5, CurrencyType.EUR);
        Map<CurrencyType, Double> rates = new HashMap<>();
        rates.put(CurrencyType.GBR, 1.0);
        rates.put(CurrencyType.EUR, 1.19);
        rates.put(CurrencyType.USD, 1.32);

        ExchangeRates exchangeRates = new CurrencyExchangeRates(rates);
        BigDecimal result = invoice.calculateExchangedAmount(exchangeRates, CurrencyType.EUR);

        Assertions.assertEquals(BigDecimal.valueOf(100.50).setScale(2, RoundingMode.HALF_UP), result,
                "Calculated exchanged amount does not match the expected one ");
    }

    @Test
    public void testGBRTargetAndDefaultOne() {
        Document invoice = new Invoice(1, 100, CurrencyType.EUR);
        Map<CurrencyType, Double> rates = new HashMap<>();
        rates.put(CurrencyType.GBR, 1.0);
        rates.put(CurrencyType.EUR, 1.1918059);
        rates.put(CurrencyType.USD, 1.3177745);

        ExchangeRates exchangeRates = new CurrencyExchangeRates(rates);
        BigDecimal result = invoice.calculateExchangedAmount(exchangeRates, CurrencyType.GBR);
        BigDecimal expected = BigDecimal.valueOf(83.91);

        Assertions.assertEquals(expected, result, "Calculated exchanged amount does not match the expected one ");
    }

    @Test
    public void testGBRTargetEURCurrentUSDDefalt() {
        Document invoice = new Invoice(1, 1500.23, CurrencyType.EUR);
        Map<CurrencyType, Double> rates = new HashMap<>();
        rates.put(CurrencyType.USD, 1.0);
        rates.put(CurrencyType.GBR, 0.7587277);
        rates.put(CurrencyType.EUR, 0.90594942);

        ExchangeRates exchangeRates = new CurrencyExchangeRates(rates);
        BigDecimal result = invoice.calculateExchangedAmount(exchangeRates, CurrencyType.GBR);
        BigDecimal expected = BigDecimal.valueOf(1256.43);

        Assertions.assertEquals(expected, result, "Calculated exchanged amount does not match the expected one");
    }

}
