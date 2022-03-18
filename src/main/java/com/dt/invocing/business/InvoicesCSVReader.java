package com.dt.invocing.business;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import com.dt.invocing.model.CreditNote;
import com.dt.invocing.model.CurrencyType;
import com.dt.invocing.model.Customer;
import com.dt.invocing.model.DebitNote;
import com.dt.invocing.model.Document;
import com.dt.invocing.model.Invoice;

@Service
public class InvoicesCSVReader implements DocumentReader {

    @Override
    public List<Customer> readCustomerInvoices(InputStream input,
            InputValidator validator) {
        Map<Long, Customer> customers = new HashMap<>();
        Map<Long, Long> childParentsRelations = new HashMap<>();
        List<ValidationException> exceptions = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(
                new InputStreamReader(input, "UTF-8"));
                CSVParser csvParser = new CSVParser(fileReader,
                        CSVFormat.Builder.create()
                        .setHeader(InputValidator.CUSTOMER,
                                InputValidator.VAT_NUMBER,
                                InputValidator.DOCUMENT_NUMBER,
                                InputValidator.TYPE_HEADER,
                                InputValidator.PARENT_DOCUMENT,
                                InputValidator.CURRENCY_HEADER,
                                InputValidator.TOTAL_HEADER)
                        .setSkipHeaderRecord(true)
                        .setIgnoreHeaderCase(true).setTrim(true)
                        .setNullString("0").build())) {
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {

                String customerName = csvRecord.get(InputValidator.CUSTOMER);
                String vatString = csvRecord.get(InputValidator.VAT_NUMBER);
                String numberString = csvRecord
                        .get(InputValidator.DOCUMENT_NUMBER);
                String typeString = csvRecord.get(InputValidator.TYPE_HEADER);
                String parentNumberString = csvRecord
                        .get(InputValidator.PARENT_DOCUMENT);
                String currencyString = csvRecord
                        .get(InputValidator.CURRENCY_HEADER);
                String totalString = csvRecord.get(InputValidator.TOTAL_HEADER);

                Long vat = 0l;
                Long number = 0l;
                Integer type = 0;
                CurrencyType currency = null;
                Double total = 0d;
                Long parentNumber = 0l;

                validator.validateCustomerName(exceptions, customerName);
                vat = validator.validateVat(exceptions, vatString, vat);
                number = validator.validateDocumentNumber(exceptions,
                        numberString, number);

                type = validator.validateType(exceptions, typeString, type);
                currency = validator.validateCurrency(exceptions,
                        currencyString, currency);
                total = validator.validateTotal(exceptions, totalString, total);

                parentNumber = validator.validateParent(parentNumberString,
                        parentNumber);

                if (!exceptions.isEmpty()) {
                    throw new InvalidInputData(exceptions);
                }
                Document document = null;
                switch (type.intValue()) {
                    case 1 :
                        document = new Invoice(number, total, currency,
                                parentNumber);
                        break;
                    case 2 :
                        document = new CreditNote(number, total, currency,
                                parentNumber);
                        break;
                    case 3 :
                        document = new DebitNote(number, total, currency,
                                parentNumber);
                        break;
                    default :
                        break;
                }
                childParentsRelations.put(document.getNumber(),
                        document.getParentNumber());
                if (customers.containsKey(vat)) {
                    customers.get(vat).getDocuments().add(document);
                } else {
                    customers.put(vat,
                            new Customer(vat, customerName, document));
                }

            }
            if (customers.isEmpty()) {
                exceptions.add(new ValidationException(
                        "No csv records found in the provided data. Possible reasons: Empty content or not csv format"));
            }
            validator.validateParents(childParentsRelations, exceptions);
        } catch (IOException | IllegalStateException
                | IllegalArgumentException e) {
            exceptions.add(new ValidationException(
                    "Invalid content provided: " + e.getMessage()));
        }
        if (!exceptions.isEmpty()) {
            throw new InvalidInputData(exceptions);
        }
        return customers.values().stream().collect(Collectors.toList());

    }

}
