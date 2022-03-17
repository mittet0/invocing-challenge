package com.dt.invocing.business;

import java.math.BigDecimal;
import java.util.List;

import com.dt.invocing.model.CurrencyType;
import com.dt.invocing.model.Customer;
import com.dt.invocing.model.Document;
import com.dt.invocing.model.ExchangeRates;

public class DocumentCalculator implements CalculationService {

	@Override
	public BigDecimal calculateTotal(ExchangeRates excahngeRates,
			Customer customer, CurrencyType targetCurrency) {
		BigDecimal result = BigDecimal.ZERO;
		for (Document document : customer.getDocuments()) {
			result = result.add(document.calculateExchangedAmount(excahngeRates,
					targetCurrency));
		}
		return result;
	}

	@Override
	public BigDecimal calculateTotal(ExchangeRates excahngeRates,
			List<Customer> customers, CurrencyType targetCurrency) {
		BigDecimal result = BigDecimal.valueOf(0);
		for (Customer customer : customers) {
			result = result.add(
					calculateTotal(excahngeRates, customer, targetCurrency));
		}
		return result;
	}
}
