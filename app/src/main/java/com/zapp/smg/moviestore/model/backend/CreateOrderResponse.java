package com.zapp.smg.moviestore.model.backend;

import com.google.gson.annotations.SerializedName;

import com.zapp.library.smg.mcom.model.LiteMcomTransaction;
import com.zapp.smg.moviestore.model.Movie;
import com.zapp.smg.moviestore.model.PaymentMethod;

import java.util.UUID;

/**
 * Response to the 'create order' request from the Merchant Backend. It delivers the payment method the order to be created with.
 *
 * @author msagi
 */
public class CreateOrderResponse {

    @SerializedName("transactionId")
    private UUID mTransactionId;

    @SerializedName("pbbaTransaction")
    private LiteMcomTransaction mPayByBankAppTransaction;

    @SerializedName("paymentMethod")
    private PaymentMethod mPaymentMethod;

    @SerializedName("movie")
    private Movie mMovie;

    public UUID getTransactionId() {
        return mTransactionId;
    }

    public void setTransactionId(final UUID transactionId) {
        mTransactionId = transactionId;
    }

    public LiteMcomTransaction getPayByBankAppTransaction() {
        return mPayByBankAppTransaction;
    }

    public void setPayByBankAppTransaction(final LiteMcomTransaction payByBankAppTransaction) {
        mPayByBankAppTransaction = payByBankAppTransaction;
    }

    public PaymentMethod getPaymentMethod() {
        return mPaymentMethod;
    }

    public void setPaymentMethod(final PaymentMethod paymentMethod) {
        mPaymentMethod = paymentMethod;
    }

    public Movie getMovie() {
        return mMovie;
    }

    public void setMovie(final Movie movie) {
        mMovie = movie;
    }
}
