package com.zapp.library.smg.mcom.model;

import com.zapp.library.smg.core.model.AdHoc;
import com.zapp.library.smg.core.model.LitePaymentRequest;
import com.zapp.library.smg.core.model.ZappModelValidationException;
import com.zapp.library.smg.core.util.ValidationUtils;

/**
 * Mcom lite payment request model class.
 *
 * @author msagi
 */
public class LiteMcomPaymentRequest extends LitePaymentRequest {

    private String merchantCallbackUrl;

    public LiteMcomPaymentRequest(final Long totalAmount, final String merchantId, final String merchantCallbackUrl, final String rtpId, final AdHoc adHoc)
            throws ZappModelValidationException {
        super(totalAmount, merchantId, rtpId, adHoc);
        this.merchantCallbackUrl = merchantCallbackUrl;
        ValidationUtils.require(merchantCallbackUrl, "merchantCallbackUrl");
    }
}
