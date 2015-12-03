package com.zapp.library.smg.core.model;

import com.zapp.library.smg.core.util.ValidationUtils;

/**
 * The currency amount.
 */
public class CurrencyAmount implements IValidatable {

    /**
     * The default currency code.
     */
    public static final String POUNDS = "GBP";

    /**
     * The currency value.
     */
    private Long value;

    /**
     * The currency code.
     */
    private String currencyCode;

    /**
     * The default constructor.
     *
     * @param value        The amount value (the last two digits are the fractions, e.g £2.30 -> 230).
     * @param currencyCode The currency code of the amount. If given as null, defaults to GBP.
     */
    public CurrencyAmount(final Long value, final String currencyCode) throws ZappModelValidationException {
        this.value = value;
        this.currencyCode = currencyCode == null ? POUNDS : currencyCode;
        validate();
    }

    public Long getValue() {
        return value;
    }

    public void setValue(final Long value) {
        this.value = value;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(final String currencyCode) {
        this.currencyCode = currencyCode;
    }

    /**
     * Add the given currency amount to the current one. (Called currencyAmountByAddingCurrencyAmount on iOS)
     *
     * @param currencyAmount The currency amount to be added.
     * @return the result of currency amount after adding.
     */
    public CurrencyAmount add(final CurrencyAmount currencyAmount) {
        if (currencyCode == null) {
            throw new IllegalArgumentException("currencyCode == null");
        }
        if (currencyAmount == null) {
            throw new IllegalArgumentException("currencyAmount == null");
        }
        if (!currencyCode.equals(currencyAmount.getCurrencyCode())) {
            throw new IllegalArgumentException("currencyCode != currencyAmount.currencyCode");
        }
        value += currencyAmount.value;
        return this;
    }

    /**
     * Subtracts the given currency amount from the current one. (Called currencyAmountBySubtractingCurrencyAmount on iOS)
     *
     * @param currencyAmount The currency amount to be subtracted.
     * @return the result of currency amount after subtraction.
     */
    public CurrencyAmount subtract(final CurrencyAmount currencyAmount) {
        if (currencyCode == null) {
            throw new IllegalArgumentException("currencyCode == null");
        }
        if (currencyAmount == null) {
            throw new IllegalArgumentException("currencyAmount == null");
        }
        if (!currencyCode.equals(currencyAmount.getCurrencyCode())) {
            throw new IllegalArgumentException("currencyCode != currencyAmount.currencyCode");
        }
        value -= currencyAmount.value;
        return this;
    }

    /**
     * Multiply the current currency amount with the given quantity. (Called currencyAmountByMultiplyingByQuantity on iOS)
     * @param quantity The quantity to multiply with.
     * @return the result of currency amount after multiplication.
     */
    public CurrencyAmount multiplyByQuantity(final Integer quantity) {
        if (quantity == null) {
            throw new IllegalArgumentException("quantity == null");
        }
        value *= quantity;
        return this;
    }

    /**
     * Display the amount as string. (Called displayString on iOS)
     *
     * @return The {@link String} representation of the amount.
     */
    @Override
    public String toString() {
        if (value == null) {
            return null;
        }
        final String displayString;
        if (POUNDS.equals(currencyCode)) {
            final String sign = value >= 0 ? "" : "-";
            final long poundsValue = Math.abs(value / 100);
            final long penceValue = Math.abs(value % 100);
            if (poundsValue != 0) {
                displayString = String.format("%s\u00a3%d.%02d", sign, poundsValue, penceValue);
            } else {
                displayString = String.format("%s%dp", sign, penceValue);
            }
        } else {
            throw new IllegalArgumentException("currencyCode not supported");
        }
        return displayString;
    }

    @Override
    public final void validate() throws ZappModelValidationException {
        ValidationUtils.require(value, "CurrencyAmount.value");
        ValidationUtils.require(currencyCode, "CurrencyAmount.currencyCode");
    }
}
