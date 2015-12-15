package com.zapp.smg.moviestore.mock.service.zapp.smg;

import com.google.gson.annotations.SerializedName;

/**
 * Base Zapp Small Merchant Gateway API error model object.
 * @author msagi
 */
public class ZappSMGAPIError {

    @SerializedName("errorCode")
    private Integer mErrorCode;

    @SerializedName("errorMessage")
    private String mErrorMessage;

    public Integer getErrorCode() {
        return mErrorCode;
    }

    public void setErrorCode(final Integer errorCode) {
        mErrorCode = errorCode;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        mErrorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return String.format("Zapp Small Merchant Gateway API error: errorCode: %d, errorMessage: %s", mErrorCode, mErrorMessage);
    }
}
