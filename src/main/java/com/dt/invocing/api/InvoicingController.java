package com.dt.invocing.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.dt.invocing.business.CalculationService;
import com.dt.invocing.business.DocumentReader;
import com.dt.invocing.business.InputValidator;
import com.dt.invocing.model.CurrencyExchangeRates;
import com.dt.invocing.model.CurrencyType;
import com.dt.invocing.model.Customer;
import com.dt.invocing.model.ExchangeRates;

@RestController
public class InvoicingController {

    Logger logger = LoggerFactory.getLogger(InvoicingController.class);

    private CalculationService calculationService;
    private DocumentReader documentReader;

    public InvoicingController(CalculationService calculationService,
            DocumentReader documentReader) {
        this.calculationService = calculationService;
        this.documentReader = documentReader;
    }

    @GetMapping("/")
    public String index() {
        return "Invocing app is currently under development! Stay tuned for updates.";
    }

    @PostMapping(value = "/sum", consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE})
    public InvoicesSumDTO sum(
            @RequestPart(value = "exchangeRates", required = true) @NonNull Map<CurrencyType, Double> excahngeRates,
            @RequestPart(value = "targetCurrency", required = true) @NonNull CurrencyType targetCurrency,
            @RequestPart(value = "document", required = true) @NonNull MultipartFile file,
            @RequestParam(value = "customerVat", required = false) Long customerVat)
                    throws IOException {
        BigDecimal totalInvoiceSum = null;
        InputValidator validator = new InputValidator();
        ExchangeRates rates = new CurrencyExchangeRates(excahngeRates);
        validator.validateExchangeRate(rates, targetCurrency);
        List<Customer> customers = documentReader
                .readCustomerInvoices(file.getInputStream(), validator);
        if (customerVat != null) {
            Customer customer = customers.stream()
                    .filter(c -> c.getVat() == customerVat.longValue())
                    .findFirst().orElse(null);
            if (customer == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid customer vat filter provided. No such customer data available");
            }
            totalInvoiceSum = calculationService.calculateTotal(rates, customer,
                    targetCurrency);
            return new InvoicesSumDTO(totalInvoiceSum.doubleValue(),
                    targetCurrency, customer.transformToDto());
        }
        totalInvoiceSum = calculationService.calculateTotal(rates, customers,
                targetCurrency);
        List<CustomerDTO> customersDto = new ArrayList<>();
        customers.stream().forEach(c -> customersDto.add(c.transformToDto()));
        return new InvoicesSumDTO(totalInvoiceSum.doubleValue(), targetCurrency,
                customersDto);
    }

}
