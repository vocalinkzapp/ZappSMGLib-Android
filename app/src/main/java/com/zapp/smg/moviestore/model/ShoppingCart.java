package com.zapp.smg.moviestore.model;

/**
 * Model object for shopping cart.
 *
 * @author msagi
 */
public class ShoppingCart {

    private Movie mMovie;

    public Movie getMovie() {
        return mMovie;
    }

    public void setMovie(final Movie movie) {
        mMovie = movie;
    }

    /**
     * Get the total amount of the shopping cart.
     * @return Zero if the shopping cart is empty or the total amount of the shopping cart otherwise.
     */
    public long getTotalAmount() {
        if (mMovie == null) {
            return 0L;
        }
        return mMovie.getPrice();
    }
}
