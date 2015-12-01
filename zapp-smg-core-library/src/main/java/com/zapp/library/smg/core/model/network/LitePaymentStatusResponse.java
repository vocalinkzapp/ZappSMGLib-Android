package com.zapp.library.smg.core.model.network;

import com.zapp.library.smg.core.model.AdHoc;
import com.zapp.library.smg.core.model.IValidatable;
import com.zapp.library.smg.core.model.LitePaymentStatus;
import com.zapp.library.smg.core.model.LitePaymentStatusDescription;
import com.zapp.library.smg.core.model.LiteSettlementType;
import com.zapp.library.smg.core.model.ZappModelValidationException;
import com.zapp.library.smg.core.util.ValidationUtils;

import android.util.Log;

/**
 * Payment status model object for Lite merchant gateway.
 * @author msagi
 */
public class LitePaymentStatusResponse implements IValidatable {

    /**
     * Tag for logging.
     */
    private static final String TAG = LitePaymentStatusResponse.class.getSimpleName();

    /**
     * The payment status.
     * @see #getPaymentStatus()
     */
    private Integer txnStatus;

    /**
     * Additional description about the payment status. Optional.
     * @see #getPaymentStatusDescription()
     */
    private Integer txnStatusDesc;

    /**
     * This value is the same as the {@link com.zapp.library.smg.core.model.LitePaymentRequest#totalAmount}. Optional.
     */
    private Long settlementAmount;

    /**
     * The payment reference data provided by the CFI. Optional.
     */
    private String settlementPaymentRef;

    /**
     * Additional information for the Merchant about the channel the CFI used to make the payment (e.g. ONUS or FPS). Optional.
     * @see #getSettlementType()
     */
    private Integer settlementType;

    /**
     * This value is the same as the {@link com.zapp.library.smg.core.model.LitePaymentRequest#adHoc}. Optional.
     */
    private AdHoc adHoc;

    public Integer getTxnStatus() {
        return txnStatus;
    }

    public void setTxnStatus(final Integer txnStatus) {
        this.txnStatus = txnStatus;
    }

    public Integer getTxnStatusDesc() {
        return txnStatusDesc;
    }

    public void setTxnStatusDesc(final Integer txnStatusDesc) {
        this.txnStatusDesc = txnStatusDesc;
    }

    public Long getSettlementAmount() {
        return settlementAmount;
    }

    public void setSettlementAmount(final Long settlementAmount) {
        this.settlementAmount = settlementAmount;
    }

    public String getSettlementPaymentRef() {
        return settlementPaymentRef;
    }

    public void setSettlementPaymentRef(final String settlementPaymentRef) {
        this.settlementPaymentRef = settlementPaymentRef;
    }

    public void setSettlementType(final Integer settlementType) {
        this.settlementType = settlementType;
    }

    public AdHoc getAdHoc() {
        return adHoc;
    }

    public void setAdHoc(final AdHoc adHoc) {
        this.adHoc = adHoc;
    }

    /**
     * Return the value of the txnStatus field as {@link LitePaymentStatus} instance.
     * @return The status value as {@link LitePaymentStatus} or null if the status value is not set or invalid.
     */
    public LitePaymentStatus getPaymentStatus() {
        if (txnStatus == null) {
            return null;
        }

        switch (txnStatus) {
            case 0:
                return LitePaymentStatus.AUTHORISED;
            case 1:
                return LitePaymentStatus.DECLINED;
            case 2:
                return LitePaymentStatus.INCOMPLETE;
            case 3:
                return LitePaymentStatus.IN_PROGRESS;
            case 4:
                return LitePaymentStatus.PAYMENT_ENQUIRY_FAILED;
            case 5:
                return LitePaymentStatus.AWAITING_SETTLEMENT;
            default:
                Log.w(TAG, String.format("Unknown payment status: %d", txnStatus));
                return null;
        }
    }

    /**
     * Return the value of the txnStatusDesc field as {@link LitePaymentStatusDescription} instance.
     * @return The status description value as {@link LitePaymentStatusDescription} or null if the status description value is not set or invalid.
     */
    public LitePaymentStatusDescription getPaymentStatusDescription() {
        if (txnStatusDesc == null) {
            return null;
        }

        switch (txnStatusDesc) {
            case 0:
                return LitePaymentStatusDescription.DECLINED;
            case 1:
                return LitePaymentStatusDescription.LATE_AUTHORISED;
            case 2:
                return LitePaymentStatusDescription.LATE_DECLINED;
            case 3:
                return LitePaymentStatusDescription.PROCESSING_ERROR;
            case 4:
                return LitePaymentStatusDescription.TRANSACTION_ERROR;
            default:
                Log.w(TAG, String.format("Unknown payment status description: %d", txnStatusDesc));
                return null;
        }
    }

    /**
     * Return the value of the settlementType field as {@link LiteSettlementType} instance.
     * @return The settlement type value as {@link LiteSettlementType} or null if the settlement type value is not set or invalid.
     */
    public LiteSettlementType getSettlementType() {
        if (settlementType == null) {
            return null;
        }

        switch(settlementType) {
            case 0:
                return LiteSettlementType.ONUS;
            case 1:
                return LiteSettlementType.FPS_SIP;
            case 2:
                return LiteSettlementType.FPS_FDP;
            default:
                Log.w(TAG, String.format("Unknown settlement type: %d", settlementType));
                return null;
        }
    }

    @Override
    public void validate() throws ZappModelValidationException {
        ValidationUtils.require(txnStatus, "txnStatus");
    }
}
