package com.zapp.smg.moviestore.service;

/**
 * Created by msagi on 14/12/2015.
 */
public class BackendAPIError<T extends Throwable> {

    private final T mError;

    public BackendAPIError(T error) {
        mError = error;
    }

    public T getError() {
        return mError;
    }
}
