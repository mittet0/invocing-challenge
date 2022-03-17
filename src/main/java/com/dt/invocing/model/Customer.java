/**
 * 
 */
package com.dt.invocing.model;

import java.util.List;

/**
 * @author dgangov
 *
 */
public class Customer {

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
}
