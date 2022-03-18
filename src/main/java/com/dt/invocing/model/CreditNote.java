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
public class CreditNote extends Document {

    public CreditNote(long number, double amount, CurrencyType currency,
            long parentNumber) {
        super();
        this.amount = BigDecimal.valueOf(amount).setScale(2,
                RoundingMode.HALF_UP);
        this.number = number;
        this.currency = currency;
        this.parentNumber = parentNumber;

    }

    @Override
    public BigDecimal calculateAmount() {
        return amount.negate();
    }
}
