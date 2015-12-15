package com.zapp.smg.moviestore.model.backend;

import com.zapp.smg.moviestore.model.PaymentMethod;

import android.support.annotation.NonNull;

/**
 * Request to trigger 'create order' feature in the Merchant Backend. It delivers the payment method the order to be created with.
 *
 * @author msagi
 */
public class CreateOrderRequest {

    private final PaymentMethod mPaymentMethod;

    public CreateOrderRequest(@NonNull final PaymentMethod paymentMethod) {
        mPaymentMethod = paymentMethod;
    }

    public PaymentMethod getPaymentMethod() {
        return mPaymentMethod;
    }
}
