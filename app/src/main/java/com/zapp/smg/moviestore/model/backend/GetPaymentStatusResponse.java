package com.zapp.smg.moviestore.model.backend;

import com.zapp.smg.moviestore.model.Movie;

import java.util.UUID;

/**
 * Created by msagi on 10/12/2015.
 */
public class GetPaymentStatusResponse {

    public enum Status {
        IN_PROGRESS,
        AUTHORISED,
        DECLINED,
        UNKNOWN,
    }

    /**
     * The transaction id relates to the payment.
     */
    private UUID mTransactionId;

    /**
     * The status of the payment.
     */
    private Status mStatus = Status.UNKNOWN;

    /**
     * The movie is about to be purchased. Required only if status is authorised or declined, optional otherwise.
     */
    private Movie mMovie;

    public UUID getTransactionId() {
        return mTransactionId;
    }

    public void setTransactionId(final UUID transactionId) {
        mTransactionId = transactionId;
    }

    public Status getStatus() {
        return mStatus;
    }

    public void setStatus(final Status status) {
        mStatus = status;
    }

    public Movie getMovie() {
        return mMovie;
    }

    public void setMovie(final Movie movie) {
        mMovie = movie;
    }
}
