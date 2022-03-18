/**
 *
 */
package com.dt.invocing.business;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.dt.invocing.model.CreditNote;
import com.dt.invocing.model.CurrencyExchangeRates;
import com.dt.invocing.model.CurrencyType;
import com.dt.invocing.model.Customer;
import com.dt.invocing.model.DebitNote;
import com.dt.invocing.model.Document;
import com.dt.invocing.model.ExchangeRates;
import com.dt.invocing.model.Invoice;

/**
 * @author dgangov
 *
 */
public class DocumentCalculatorTests {

    private static final String VENDOR3_NAME = "Vendor3";
    private static final String VENDOR2_NAME = "Vendor2";
    private static final String VENDOR1_NAME = "Vendor1";

    @Test
    public void testCalculateTotalWithEURDefaultTargetUSD() {
        Document invoice = new Invoice(1, 150.5, CurrencyType.EUR, 0);
        Document creditNote = new CreditNote(2, 24.3, CurrencyType.GBP, 1);
        Document debitNote = new DebitNote(3, 75.64, CurrencyType.USD, 1);

        Customer vendor1 = new Customer(123123, VENDOR1_NAME,
                Arrays.asList(creditNote, invoice, debitNote));

        Map<CurrencyType, Double> rates = new HashMap<>();
        rates.put(CurrencyType.EUR, 1.0);
        rates.put(CurrencyType.GBP, 0.84302512);
        rates.put(CurrencyType.USD, 1.1050684);

        ExchangeRates exchangeRates = new CurrencyExchangeRates(rates);
        CalculationService documentCalculator = new DocumentCalculator();
        BigDecimal totalForVendor1 = documentCalculator
                .calculateTotal(exchangeRates, vendor1, CurrencyType.USD);

        Assertions.assertEquals(
                BigDecimal.valueOf(210.10).setScale(2, RoundingMode.HALF_UP),
                totalForVendor1,
                "Calculated exchanged amount does not match the expected one ");
    }

    @Test
    public void testCalculateTotalWithGBRDefaultTargetEUR() {
        Document invoice = new Invoice(1, 100.5, CurrencyType.EUR, 0);
        Document creditNote = new CreditNote(2, 20, CurrencyType.GBP, 1);
        Document debitNote = new DebitNote(3, 46.6, CurrencyType.USD, 1);

        Customer vendor1 = new Customer(123123, VENDOR1_NAME,
                Arrays.asList(creditNote, invoice, debitNote));

        Map<CurrencyType, Double> rates = new HashMap<>();
        rates.put(CurrencyType.GBP, 1.0);
        rates.put(CurrencyType.EUR, 1.19);
        rates.put(CurrencyType.USD, 1.32);

        ExchangeRates exchangeRates = new CurrencyExchangeRates(rates);
        CalculationService documentCalculator = new DocumentCalculator();
        BigDecimal totalForVendor1 = documentCalculator
                .calculateTotal(exchangeRates, vendor1, CurrencyType.EUR);

        Assertions.assertEquals(
                BigDecimal.valueOf(118.71).setScale(2, RoundingMode.HALF_UP),
                totalForVendor1,
                "Calculated exchanged amount does not match the expected one ");
    }

    @Test
    public void testCalculateTotalWithUSDDefaultTargetGBRMultipleCustomers() {
        Document invoice = new Invoice(1, 150.5, CurrencyType.EUR, 0);
        Document creditNote = new CreditNote(2, 24.3, CurrencyType.GBP, 1);
        Document debitNote = new DebitNote(3, 75.64, CurrencyType.USD, 1);

        Customer vendor1 = new Customer(123123, VENDOR1_NAME,
                Arrays.asList(creditNote, invoice, debitNote));
        invoice = new Invoice(1, 236.52, CurrencyType.USD, 0);
        creditNote = new CreditNote(2, 73.34, CurrencyType.EUR, 1);

        Customer vendor2 = new Customer(123124, VENDOR2_NAME,
                Arrays.asList(creditNote, invoice));
        invoice = new Invoice(1, 50.5, CurrencyType.USD, 0);
        debitNote = new DebitNote(3, 5.89, CurrencyType.GBP, 1);
        Customer vendor3 = new Customer(123125, VENDOR3_NAME,
                Arrays.asList(invoice, debitNote));

        Map<CurrencyType, Double> rates = new HashMap<>();
        rates.put(CurrencyType.USD, 1.0);
        rates.put(CurrencyType.EUR, 0.9044606);
        rates.put(CurrencyType.GBP, 0.76285853);

        ExchangeRates exchangeRates = new CurrencyExchangeRates(rates);
        CalculationService documentCalculator = new DocumentCalculator();
        BigDecimal totalForAllVendors = documentCalculator.calculateTotal(
                exchangeRates, Arrays.asList(vendor1, vendor2, vendor3),
                CurrencyType.GBP);

        Assertions.assertEquals(
                BigDecimal.valueOf(323.32).setScale(2, RoundingMode.HALF_UP),
                totalForAllVendors,
                "Calculated exchanged amount does not match the expected one ");
    }
}
