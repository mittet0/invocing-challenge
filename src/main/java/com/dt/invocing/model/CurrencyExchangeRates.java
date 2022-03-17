/**
 * 
 */
package com.dt.invocing.model;

import java.util.Map;

/**
 * @author dgangov
 *
 */
public class CurrencyExchangeRates implements ExchangeRates {

	private Map<CurrencyType, Double> curencyRates;

	public Map<CurrencyType, Double> getCurrencyRates() {
		return curencyRates;
	}

	/**
	 * @param defailtCurrency
	 * @param curencyRates
	 */
	public CurrencyExchangeRates(Map<CurrencyType, Double> curencyRates) {
		super();
		this.curencyRates = curencyRates;
	}
}
