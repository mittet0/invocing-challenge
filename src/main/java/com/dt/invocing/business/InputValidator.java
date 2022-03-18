package com.dt.invocing.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.dt.invocing.model.CurrencyType;
import com.dt.invocing.model.ExchangeRates;

public class InputValidator {
    public static final String TOTAL_HEADER = "Total";
    public static final String CURRENCY_HEADER = "Currency";
    public static final String PARENT_DOCUMENT = "Parent document";
    public static final String TYPE_HEADER = "Type";
    public static final String DOCUMENT_NUMBER = "Document number";
    public static final String VAT_NUMBER = "Vat number";
    public static final String CUSTOMER = "Customer";

    public Long validateParent(String parentNumberString, Long parentNumber) {
        if (!isBlankString(parentNumberString)) {
            parentNumber = Long.valueOf(parentNumberString);

        }
        return parentNumber;
    }

    public Double validateTotal(List<ValidationException> exceptions,
            String totalString, Double total) {
        try {
            if (isBlankString(totalString)) {
                exceptions.add(new ValidationException(
                        String.format("Blank value not allowed for column %s",
                                TOTAL_HEADER)));
            }
            total = Double.valueOf(totalString);
        } catch (NumberFormatException e) {
            exceptions.add(new ValidationException(e.getMessage(), e));
        }
        return total;
    }

    public CurrencyType validateCurrency(List<ValidationException> exceptions,
            String currencyString, CurrencyType currency) {
        try {
            if (isBlankString(currencyString)) {
                exceptions.add(new ValidationException(
                        String.format("Blank value not allowed for column %s",
                                CURRENCY_HEADER)));
            }
            currency = CurrencyType.valueOf(currencyString);

        } catch (Exception e) {
            exceptions.add(new ValidationException(String.format(
                    "Invalid currency provided: %s. Supported ones are: %s, %s, %s",
                    currencyString, CurrencyType.EUR, CurrencyType.GBP,
                    CurrencyType.USD), e));
        }
        return currency;
    }

    public Integer validateType(List<ValidationException> exceptions,
            String typeString, Integer type) {
        try {
            if (isBlankString(typeString)) {
                exceptions.add(new ValidationException(String.format(
                        "Blank value not allowed for column %s", TYPE_HEADER)));
            }
            type = Integer.valueOf(typeString);
            if (type.intValue() < 1 || type.intValue() > 3) {
                exceptions.add(new ValidationException(String
                        .format("Not supported type provided %s", typeString)));
            }
        } catch (NumberFormatException e) {
            exceptions.add(new ValidationException(e.getMessage(), e));
        }
        return type;
    }

    public Long validateDocumentNumber(List<ValidationException> exceptions,
            String numberString, Long number) {
        try {
            if (isBlankString(numberString)) {
                exceptions.add(new ValidationException(
                        String.format("Blank value not allowed for column %s",
                                DOCUMENT_NUMBER)));
            }
            number = Long.valueOf(numberString);

        } catch (NumberFormatException e) {
            exceptions.add(new ValidationException(e.getMessage(), e));
        }
        return number;
    }

    public Long validateVat(List<ValidationException> exceptions,
            String vatString, Long vat) {
        try {
            if (isBlankString(vatString)) {
                exceptions.add(new ValidationException(String.format(
                        "Blank value not allowed for column %s", VAT_NUMBER)));
            }
            vat = Long.valueOf(vatString);
        } catch (NumberFormatException e) {
            exceptions.add(new ValidationException(e.getMessage(), e));
        }
        return vat;
    }

    public void validateCustomerName(List<ValidationException> exceptions,
            String customerName) {
        if (isBlankString(customerName)) {
            exceptions.add(new ValidationException(String.format(
                    "Blank value not allowed for column %s", CUSTOMER)));
        }
    }

    public void validateParents(Map<Long, Long> childParentsRelations,
            List<ValidationException> exceptions) {
        for (Long parentId : childParentsRelations.values()) {
            if (parentId != 0 && !childParentsRelations.containsKey(parentId)) {
                exceptions.add(new ValidationException(String.format(
                        "A Document is referencing non existing parent with number %s",
                        parentId)));
            }
        }
    }

    public void validateExchangeRate(ExchangeRates rates,
            CurrencyType targetCurrency) {
        List<ValidationException> exceptions = new ArrayList<>();
        if (rates.getCurrencyRates().keySet().size() < 2) {
            exceptions.add(new ValidationException(
                    "At least two exchange rates must be provided"));
        }
        Optional<Double> defaultRate = rates.getCurrencyRates().values()
                .stream().filter(d -> (d == 1.0)).findFirst();
        if (defaultRate.isEmpty()) {
            exceptions.add(new ValidationException(
                    "At least one default(1.0) exchange rate must be provided"));
        }
        if (!rates.getCurrencyRates().containsKey(targetCurrency)) {
            exceptions.add(new ValidationException(
                    "Exchange rates do not contain entry for target currency."));
        }
        if (!exceptions.isEmpty()) {
            throw new InvalidInputData(exceptions);
        }
    }

    public boolean isBlankString(String string) {
        return string == null || string.isBlank();
    }

}
