package com.zapp.smg.moviestore.model.backend;

import com.google.gson.annotations.SerializedName;

import com.zapp.smg.moviestore.model.Movie;

/**
 * Created by msagi on 08/12/2015.
 */
public class GetCatalogueResponse {

    @SerializedName("movies")
    Movie[] mMovies;

    public Movie[] getMovies() {
        return mMovies;
    }
}
