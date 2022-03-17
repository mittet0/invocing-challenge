/**
 * 
 */
package com.dt.invocing.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author dgangov
 *
 */
public class DebitNote extends Document {

	public DebitNote(long number, double amount, CurrencyType currency) {
		super();
		this.amount = BigDecimal.valueOf(amount).setScale(2,
				RoundingMode.HALF_UP);
		this.number = number;
		this.currency = currency;
	}

	@Override
	public BigDecimal calculateAmount() {
		return amount;
	}

}
