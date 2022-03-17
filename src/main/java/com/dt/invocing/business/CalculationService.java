/**
 * 
 */
package com.dt.invocing.business;

import java.math.BigDecimal;
import java.util.List;

import com.dt.invocing.model.CurrencyType;
import com.dt.invocing.model.Customer;
import com.dt.invocing.model.ExchangeRates;

/**
 * @author dgangov
 *
 */
public interface CalculationService {

	public BigDecimal calculateTotal(ExchangeRates excahngeRates,
			Customer customer, CurrencyType targetCurrency);

	public BigDecimal calculateTotal(ExchangeRates excahngeRates,
			List<Customer> customers, CurrencyType targetCurrency);

}
