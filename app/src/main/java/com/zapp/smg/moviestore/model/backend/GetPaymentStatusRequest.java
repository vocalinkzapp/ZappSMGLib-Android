package com.zapp.smg.moviestore.model.backend;

import android.support.annotation.NonNull;

import java.util.UUID;

/**
 * Event to trigger 'get payment status' feature. It delivers the transaction id to get the status for.
 */
public class GetPaymentStatusRequest {
    private final UUID mTransactionId;

    public GetPaymentStatusRequest(@NonNull final UUID transactionId) {
        mTransactionId = transactionId;
    }

    public UUID getTransactionId() {
        return mTransactionId;
    }
}
