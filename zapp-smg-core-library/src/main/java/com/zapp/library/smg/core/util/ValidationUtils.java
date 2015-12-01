package com.zapp.library.smg.core.util;

import com.zapp.library.smg.core.model.ZappModelValidationException;

import java.util.regex.Pattern;

/**
 * Utility class for Zapp core model validation.
 *
 * @author msagi
 */
public class ValidationUtils {

    /**
     * The email address validation pattern as per Android Utils package.
     */
    public static final Pattern EMAIL_ADDRESS
            = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    /**
     * The amount validation pattern for 'only digits with max 14 integers and 2 fractions'.
     */
    public static final Pattern AMOUNT_14_2 = Pattern.compile("^[0-9]{1,14}(\\.[0-9]{2})?$");

    /**
     * Check if the given string value is set.
     *
     * @param s         The value to check.
     * @param fieldName The field name under checking.
     * @throws ZappModelValidationException If given value is null or empty string.
     */
    public static void require(final String s, final String fieldName) throws ZappModelValidationException {
        if (s == null || s.isEmpty()) {
            throw new ZappModelValidationException("Missing required field: " + fieldName);
        }
    }

    /**
     * Check if the given object is set.
     *
     * @param o         The object to check.
     * @param fieldName The field name under checking.
     * @throws ZappModelValidationException If given object is null.
     */
    public static void require(final Object o, final String fieldName) throws ZappModelValidationException {
        if (o == null) {
            throw new ZappModelValidationException("Missing required field: " + fieldName);
        }
    }

    /**
     * Check if the given condition is true.
     *
     * @param condition The condition to check.
     * @param message   The message of the model validation exception to set.
     * @throws ZappModelValidationException If condition is false.
     */
    public static void require(final boolean condition, final String message) throws ZappModelValidationException {
        if (!condition) {
            throw new ZappModelValidationException(message);
        }
    }

    /**
     * Require if the given parameter is within the given range.
     *
     * @param o       The object to check.
     * @param range   The range to check if the object is in.
     * @param message The message of the model validation exception to set.
     * @param <T>     The generic type.
     * @throws ZappModelValidationException If object is not in range.
     */
    public static <T> void requireIn(final T o, final T[] range, final String message) throws ZappModelValidationException {
        if (o == null) {
            throw new IllegalArgumentException("o == null");
        }
        if (range == null || range.length == 0) {
            throw new IllegalArgumentException("range == null || isEmpty");
        }
        if (isIn(o, range)) {
            return;
        }
        throw new ZappModelValidationException(message);
    }

    /**
     * Check if the given parameter is within the given range.
     *
     * @param o     The object to check.
     * @param range The range to check if the object is in.
     * @param <T>   The generic type.
     * @return True if the given object is in given range, false otherwise.
     */
    public static <T> boolean isIn(final T o, final T[] range) {
        if (o == null) {
            return false;
        }
        if (range == null || range.length == 0) {
            return false;
        }
        for (final T rangeObject : range) {
            if (o.equals(rangeObject)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the given parameter combination is in the given combination range.
     *
     * @param combination The parameter combination to check.
     * @param range       The combination range to check.
     * @throws ZappModelValidationException If combination is not in range.
     */
    public static void requireCombinationIn(final Object[] combination, final Object[]... range) throws ZappModelValidationException {
        if (combination == null) {
            throw new IllegalArgumentException("combination == null");
        }
        if (range == null || range.length == 0) {
            throw new IllegalArgumentException("range == null || isEmpty");
        }
        for (int rangeIndex = 0; rangeIndex < range.length; rangeIndex++) {
            final Object[] rangeCombination = range[rangeIndex];
            if (combination.length != rangeCombination.length) {
                throw new IllegalArgumentException("Invalid range dimension at index " + rangeIndex);
            }
            boolean isPerfectMatch = true;
            for (int index = 0; index < combination.length; index++) {
                if (!combination[index].equals(rangeCombination[index])) {
                    isPerfectMatch = false;
                    break;
                }
            }

            if (isPerfectMatch) {
                return;
            }
        }
        throw new ZappModelValidationException("Not in range: " + combination);
    }

    /**
     * Check if the given parameter is a valid email address.
     *
     * @param email   The email address to check.
     * @param message The message of the model validation exception to set.
     * @throws ZappModelValidationException If the parameter is not a valid email.
     */
    public static void requireValidEmail(final String email, final String message) throws ZappModelValidationException {
        if (!EMAIL_ADDRESS.matcher(email).matches()) {
            throw new ZappModelValidationException(message);
        }
    }

    /**
     * Check if the given string value is null.
     *
     * @param o         The object to check.
     * @param fieldName The field name under checking.
     * @throws ZappModelValidationException If given value is not null.
     */
    public static void requireNull(final Object o, final String fieldName) throws ZappModelValidationException {
        if (o != null) {
            throw new ZappModelValidationException("Field must be absent: " + fieldName);
        }
    }

    /**
     * Check if the given parameter is a valid 14.2 amount.
     *
     * @param amount  The amount to check.
     * @param message The message of the model validation exception to set.
     * @throws ZappModelValidationException If the parameter is not a valid amount.
     */
    public static void requireValidAmount_14_2(final String amount, final String message) throws ZappModelValidationException {
        if (!AMOUNT_14_2.matcher(amount).matches()) {
            throw new ZappModelValidationException(message);
        }
    }
}
