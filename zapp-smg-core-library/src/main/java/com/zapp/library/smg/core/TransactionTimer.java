package com.zapp.library.smg.core;

import com.zapp.library.smg.core.model.LiteTransaction;

import android.os.CountDownTimer;
import android.support.annotation.NonNull;

/**
 * Transaction timer class. It sends signals to poll for payment status every 5 seconds and sends a signal on payment confirmation expiry too.
 * @author msagi
 */
public abstract class TransactionTimer extends CountDownTimer {

    /**
     * Constant for one second in milliseconds.
     */
    private static final int ONE_SECOND_IN_MS = 1000;

    /**
     * Constant for countdown interval in milliseconds.
     */
    private static final int COUNTDOWN_INTERVAL = 5 * ONE_SECOND_IN_MS;

    /**
     * Timer implementation for transactions.
     *
     * @param liteTransaction The transaction to do timing for.
     */
    public TransactionTimer(@NonNull final LiteTransaction liteTransaction) {
        super(liteTransaction.getConfirmationExpiryInterval() * ONE_SECOND_IN_MS, COUNTDOWN_INTERVAL);
    }

    @Override
    public void onTick(final long millisUntilFinished) {
        onPollForPaymentStatus();
    }

    @Override
    public void onFinish() {
        onPaymentConfirmationExpired();
    }

    /**
     * Event handler for 'poll for payment status' event.
     */
    protected abstract void onPollForPaymentStatus();

    /**
     * Event handler for 'payment confirmation expired' event.
     */
    protected abstract void onPaymentConfirmationExpired();
}
