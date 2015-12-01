package com.zapp.library.smg.core.model;

import com.zapp.library.smg.core.util.ValidationUtils;

/**
 * Abstract base lite payment request model class.
 * @author msagi
 */
public abstract class LitePaymentRequest implements IValidatable {

    /**
     * The total amount for which the merchant would like to get the payment made by the customer.
     */
    protected Long totalAmount;

    /**
     * A Zapp provided data after the merchant is successfully registered.
     */
    protected String merchantId;

    /**
     * A unique ID for the merchant that represents a purchase. Conditional: the merchant could choose not to generate this ID and let Zapp gateway generate it (this
     * should
     * be decided as part of merchant registration.
     */
    protected String rtpId;

    /**
     * Key - value pair information that the merchant can use in order for Zapp to relay back the same during payment notification. Optional.
     */
    protected AdHoc adHoc;

    public LitePaymentRequest(final Long totalAmount, final String merchantId, final String rtpId, final AdHoc adHoc)
            throws ZappModelValidationException {
        this.totalAmount = totalAmount;
        this.merchantId = merchantId;
        this.rtpId = rtpId;
        this.adHoc = adHoc;
        validate();
    }

    @Override
    public void validate() throws ZappModelValidationException {
        ValidationUtils.require(totalAmount, "totalAmount");
        ValidationUtils.require(merchantId, "merchantId");
    }
}
