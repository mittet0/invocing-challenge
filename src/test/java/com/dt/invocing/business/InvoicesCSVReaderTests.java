package com.dt.invocing.business;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.dt.invocing.BaseTest;
import com.dt.invocing.model.CreditNote;
import com.dt.invocing.model.CurrencyType;
import com.dt.invocing.model.Customer;
import com.dt.invocing.model.DebitNote;
import com.dt.invocing.model.Document;
import com.dt.invocing.model.Invoice;

public class InvoicesCSVReaderTests extends BaseTest {

    @Test
    public void testNotCSVFile() {
        InvalidInputData thrown = Assertions
                .assertThrows(InvalidInputData.class, () -> {

                    DocumentReader reader = new InvoicesCSVReader();

                    ClassLoader classLoader = getClass().getClassLoader();
                    File file = new File(classLoader
                            .getResource(INVALID_FILE_TXT).getFile());

                    reader.readCustomerInvoices(new FileInputStream(file),
                            new InputValidator());

                }, "InvalidCSVFileContentException was expected.");
        Assertions.assertEquals(
                "Ivalid csv content. See nested exceptions for more details.",
                thrown.getMessage());

    }

    @Test
    public void testValidCSVFileAndValidData() throws FileNotFoundException {

        DocumentReader reader = new InvoicesCSVReader();

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(DATA_CSV).getFile());

        List<Customer> customers = reader.readCustomerInvoices(
                new FileInputStream(file), new InputValidator());

        List<Customer> expectedResult = new ArrayList<>();
        Document d1 = new Invoice(1000000257, 400, CurrencyType.USD);
        Document d2 = new Invoice(1000000258, 900, CurrencyType.EUR);
        Document d3 = new Invoice(1000000259, 1300, CurrencyType.GBP);
        Document d4 = new CreditNote(1000000260, 100, CurrencyType.EUR,
                1000000257);
        Document d5 = new DebitNote(1000000261, 50, CurrencyType.GBP,
                1000000257);
        Document d6 = new CreditNote(1000000262, 200, CurrencyType.USD,
                1000000258);
        Document d7 = new DebitNote(1000000263, 100, CurrencyType.EUR,
                1000000259);
        Document d8 = new Invoice(1000000264, 1600, CurrencyType.EUR);
        Customer v1 = new Customer(123456789, "Vendor 1",
                Arrays.asList(d1, d4, d5, d8));
        Customer v2 = new Customer(987654321, "Vendor 2",
                Arrays.asList(d2, d6));
        Customer v3 = new Customer(123465123, "Vendor 3",
                Arrays.asList(d3, d7));
        expectedResult.add(v1);
        expectedResult.add(v2);
        expectedResult.add(v3);

        Assertions.assertTrue(equalLists(expectedResult, customers),
                "Unable to read data from CSV file.");

    }
    @Test
    public void testInvalidCurrency() throws FileNotFoundException {

        InvalidInputData thrown = Assertions
                .assertThrows(InvalidInputData.class, () -> {

                    DocumentReader reader = new InvoicesCSVReader();

                    ClassLoader classLoader = getClass().getClassLoader();
                    File file = new File(classLoader
                            .getResource(INVALID_CURRENCY).getFile());

                    reader.readCustomerInvoices(new FileInputStream(file),
                            new InputValidator());

                }, "InvalidCSVFileContentException was expected.");
        Assertions.assertEquals(
                "Ivalid csv content. See nested exceptions for more details.",
                thrown.getMessage());
        Assertions.assertEquals(
                "Invalid currency provided: GBR. Supported ones are: EUR, GBP, USD",
                thrown.getExceptions().get(0).getMessage());
    }

    @Test
    public void testInvalidParent() throws FileNotFoundException {

        InvalidInputData thrown = Assertions
                .assertThrows(InvalidInputData.class, () -> {

                    DocumentReader reader = new InvoicesCSVReader();

                    ClassLoader classLoader = getClass().getClassLoader();
                    File file = new File(
                            classLoader.getResource(INVALID_PARENT).getFile());

                    reader.readCustomerInvoices(new FileInputStream(file),
                            new InputValidator());

                }, "InvalidCSVFileContentException was expected.");
        Assertions.assertEquals(
                "Ivalid csv content. See nested exceptions for more details.",
                thrown.getMessage());
        Assertions.assertEquals(
                "A Document is referencing non existing parent with number 1000002222",
                thrown.getExceptions().get(0).getMessage());
    }
    @Test
    public void testInvaliNumber() throws FileNotFoundException {

        InvalidInputData thrown = Assertions
                .assertThrows(InvalidInputData.class, () -> {

                    DocumentReader reader = new InvoicesCSVReader();

                    ClassLoader classLoader = getClass().getClassLoader();
                    File file = new File(
                            classLoader.getResource(INVALID_PARENT).getFile());

                    reader.readCustomerInvoices(new FileInputStream(file),
                            new InputValidator());

                }, "InvalidCSVFileContentException was expected.");
        Assertions.assertEquals(
                "Ivalid csv content. See nested exceptions for more details.",
                thrown.getMessage());
        Assertions.assertEquals(
                "A Document is referencing non existing parent with number 1000002222",
                thrown.getExceptions().get(0).getMessage());
    }

    public boolean equalLists(List<Customer> first, List<Customer> second) {
        if (first == null && second == null) {
            return true;
        }

        if ((first == null && second != null) || first != null && second == null
                || first.size() != second.size()) {
            return false;
        }

        List<Customer> firstSorted = first.stream().sorted()
                .collect(Collectors.toList());
        List<Customer> secondSorted = second.stream().sorted()
                .collect(Collectors.toList());

        return firstSorted.equals(secondSorted);
    }
}
