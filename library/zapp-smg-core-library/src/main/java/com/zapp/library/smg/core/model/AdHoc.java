package com.zapp.library.smg.core.model;

/**
 * Ad hoc data structure.
 * @author msagi
 */
public class AdHoc {

    /**
     * Key (max length is 255 characters)
     */
    private String key;

    /**
     * Value (max length is 255 characters)
     */
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }
}
