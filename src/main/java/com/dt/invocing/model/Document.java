package com.dt.invocing.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * @author dgangov
 *
 */
public abstract class Document implements Comparable<Document> {

    protected long number;

    protected BigDecimal amount;

    protected CurrencyType currency;

    protected long parentNumber;

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

    public long getParentNumber() {
        return parentNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency, number, parentNumber);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Document other = (Document) obj;
        return Objects.equals(amount, other.amount)
                && currency == other.currency && number == other.number
                && parentNumber == other.parentNumber;
    }

    @Override
    public String toString() {
        return "Document [number=" + number + ", amount=" + amount
                + ", currency=" + currency + ", parentNumber=" + parentNumber
                + "]";
    }

    @Override
    public int compareTo(Document o) {
        if (this.number < o.number) {
            return -1;
        } else if (this.number > o.number) {
            return 1;
        } ;
        return 0;
    }

}
