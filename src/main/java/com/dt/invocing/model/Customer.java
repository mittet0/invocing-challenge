/**
 *
 */
package com.dt.invocing.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.dt.invocing.api.CustomerDTO;

/**
 * @author dgangov
 *
 */
public class Customer implements Comparable<Customer> {

    private long vat;

    private String name;

    private List<Document> documents;

    public long getVat() {
        return vat;
    }

    public void setVat(long vat) {
        this.vat = vat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    /**
     * @param vat
     * @param name
     * @param documents
     */
    public Customer(long vat, String name, List<Document> documents) {
        super();
        this.vat = vat;
        this.name = name;
        this.documents = documents;
    }
    public Customer(long vat, String name, Document document) {
        super();
        this.vat = vat;
        this.name = name;
        this.documents = new ArrayList<>();
        documents.add(document);
    }

    @Override
    public int hashCode() {
        return Objects.hash(documents, name, vat);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Customer other = (Customer) obj;
        return Objects.equals(documents, other.documents)
                && Objects.equals(name, other.name) && vat == other.vat;
    }

    @Override
    public String toString() {
        return "Customer [vat=" + vat + ", name=" + name + ", documents="
                + documents + "]";
    }

    @Override
    public int compareTo(Customer o) {
        if (this.vat < o.vat) {
            return -1;
        } else if (this.vat > o.vat) {
            return 1;
        } ;
        return 0;
    }

    public CustomerDTO transformToDto() {
        return new CustomerDTO(this.getVat(), this.getName());

    }

}
