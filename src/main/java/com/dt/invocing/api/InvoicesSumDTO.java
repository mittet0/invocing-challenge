package com.dt.invocing.api;

import java.util.ArrayList;
import java.util.List;

import com.dt.invocing.model.CurrencyType;

public class InvoicesSumDTO {
    private Double documentsSum;
    private CurrencyType currency;
    private List<CustomerDTO> customers;

    /**
     * @param documentsSum
     * @param currency
     * @param customers
     */
    public InvoicesSumDTO(Double documentsSum, CurrencyType currency,
            List<CustomerDTO> customers) {
        super();
        this.documentsSum = documentsSum;
        this.currency = currency;
        this.customers = new ArrayList<>();
        this.customers.addAll(customers);
    }
    public InvoicesSumDTO(Double documentsSum, CurrencyType currency,
            CustomerDTO customer) {
        super();
        this.documentsSum = documentsSum;
        this.currency = currency;
        this.customers = new ArrayList<>();
        this.customers.add(customer);
    }

    public Double getDocumentsSum() {
        return documentsSum;
    }
    public void setDocumentsSum(Double documentsSum) {
        this.documentsSum = documentsSum;
    }
    public CurrencyType getCurrency() {
        return currency;
    }
    public void setCurrency(CurrencyType currency) {
        this.currency = currency;
    }
    public List<CustomerDTO> getCustomers() {
        return customers;
    }
    public void setCustomers(List<CustomerDTO> customers) {
        this.customers = customers;
    }
}
