package com.dt.invocing.business;

import java.io.InputStream;
import java.util.List;

import com.dt.invocing.model.Customer;

public interface DocumentReader {

    public List<Customer> readCustomerInvoices(InputStream input,
            InputValidator validator);

}
