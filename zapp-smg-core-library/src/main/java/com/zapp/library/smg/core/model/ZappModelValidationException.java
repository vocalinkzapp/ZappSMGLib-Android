package com.zapp.library.smg.core.model;

/**
 * Custom exception class for model validation.
 * @author msagi
 */
public class ZappModelValidationException extends Exception {

    /**
     * Create new instance with given message.
     * @param message The message of the exception.
     */
    public ZappModelValidationException(final String message) {
        super(message);
    }
}
