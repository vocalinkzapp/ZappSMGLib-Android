package com.zapp.library.smg.core.model;

import org.json.JSONObject;

import android.support.annotation.NonNull;

/**
 * Merchant model object for Lite merchant gateway.
 * @author msagi
 */
public class LiteMerchant implements IValidatable {

    /**
     * The unique merchant id assigned to merchant and it must be supplied in payment request message.
     */
    private String merchantId;

    /**
     * The merchant sequence which will be incremented for each added client and merchant will pass this as part of Adhoc
     * field when submitting payment request.
     */
    private String merchantClientSequence;

    /**
     * Admin password to control administrative activities.
     */
    private String passcode;

    /**
     * The name of the merchant.
     */
    private String merchantName;

    /**
     * The merchant's company registration number.
     */
    private String companyRegistrationNumber;

    /**
     * The company name of the merchant organisation.
     */
    private String companyName;

    /**
     * The company VAT number of merchant organisation.
     */
    private String companyVATNumber;

    /**
     * The company sort code of merchant organisation.
     */
    private String companySortCode;

    /**
     * The company account number of merchant organisation.
     */
    private String companyAccountNumber;

    /**
     * The company registered address of merchant organisation.
     */
    private LiteAddressDetails companyRegisteredAddress;

    /**
     * Json representation of mobile app preferences and it will be returned when merchant is adding more client as new till.
     */
    private String mobileAppPreference;

    public LiteMerchant(final String merchantId, final String merchantClientSequence, final String passcode, final String merchantName,
            final String companyRegistrationNumber,
            final String companyName, final String companyVATNumber, final String companySortCode, final String companyAccountNumber,
            final LiteAddressDetails companyRegisteredAddress, final String mobileAppPreference) throws ZappModelValidationException {
        this.merchantId = merchantId;
        this.merchantClientSequence = merchantClientSequence;
        this.passcode = passcode;
        this.merchantName = merchantName;
        this.companyRegistrationNumber = companyRegistrationNumber;
        this.companyName = companyName;
        this.companyVATNumber = companyVATNumber;
        this.companySortCode = companySortCode;
        this.companyAccountNumber = companyAccountNumber;
        this.companyRegisteredAddress = companyRegisteredAddress;
        this.mobileAppPreference = mobileAppPreference;
        validate();
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getMerchantClientSequence() {
        return merchantClientSequence;
    }

    public String getPasscode() {
        return passcode;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public String getCompanyRegistrationNumber() {
        return companyRegistrationNumber;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyVATNumber() {
        return companyVATNumber;
    }

    public String getCompanySortCode() {
        return companySortCode;
    }

    public String getCompanyAccountNumber() {
        return companyAccountNumber;
    }

    public LiteAddressDetails getCompanyRegisteredAddress() {
        return companyRegisteredAddress;
    }

    public JSONObject getMobileAppPreference() {
        try {
            return new JSONObject(mobileAppPreference);
        } catch (Exception e) {
            return new JSONObject();
        }
    }

    public void setMobileAppPreference(@NonNull final JSONObject mobileAppPreference) {
        this.mobileAppPreference = mobileAppPreference.toString();
    }

    @Override
    public void validate() throws ZappModelValidationException {

    }
}
