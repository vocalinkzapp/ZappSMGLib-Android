package com.zapp.library.smg.core.model;

import java.io.Serializable;

/**
 * Enables validation and serialization support for all child classes.
 */
public interface IValidatable extends Serializable {

    /**
     * Execute validation.
     * @throws ZappModelValidationException If validation fails.
     */
    void validate() throws ZappModelValidationException;
}
