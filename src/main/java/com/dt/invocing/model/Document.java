package com.dt.invocing.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author dgangov
 *
 */
public abstract class Document {

	protected long number;

	protected BigDecimal amount;

	protected CurrencyType currency;

	public abstract BigDecimal calculateAmount();

	public BigDecimal calculateExchangedAmount(ExchangeRates exchangeRates,
			CurrencyType targetCurrency) {
		if (targetCurrency.equals(this.currency)) {
			return calculateAmount();
		} else {
			Double currentDocumentRate = exchangeRates.getCurrencyRates()
					.get(this.currency);
			assert currentDocumentRate != null;
			Double targetRate = exchangeRates.getCurrencyRates()
					.get(targetCurrency);
			assert targetRate != null;
			return BigDecimal
					.valueOf((calculateAmount().doubleValue()
							/ currentDocumentRate.doubleValue())
							* targetRate.doubleValue())
					.setScale(2, RoundingMode.HALF_UP);
			// .divide(BigDecimal.valueOf(currentDocumentRate.doubleValue()))
			// .multiply(BigDecimal.valueOf(targetRate.doubleValue()));
		}
	}

	public long getNumber() {
		return number;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public CurrencyType getCurrency() {
		return currency;
	}

}
