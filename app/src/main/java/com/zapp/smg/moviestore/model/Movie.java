package com.zapp.smg.moviestore.model;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

/**
 * Model object for movies.
 * @author msagi
 */
public class Movie {

    @SerializedName("id")
    private UUID mId;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("runningTime")
    private Integer mRunningTime;

    @SerializedName("rating")
    private Double mRating;

    @SerializedName("numberOfRatings")
    private Integer mNumberOfRatings;

    @SerializedName("description")
    private String mShortDescription;

    @SerializedName("price")
    private Integer mPrice;

    @SerializedName("packshotImageUrl")
    private String mPackshotImageUrl;

    @SerializedName("wallpaperImageUrl")
    private String mWallpaperImageUrl;

    public UUID getId() {
        return mId;
    }

    public void setId(final UUID id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(final String title) {
        mTitle = title;
    }

    public Integer getRunningTime() {
        return mRunningTime;
    }

    public void setRunningTime(final Integer runningTime) {
        mRunningTime = runningTime;
    }

    public Double getRating() {
        return mRating;
    }

    public void setRating(final Double rating) {
        mRating = rating;
    }

    public Integer getNumberOfRatings() {
        return mNumberOfRatings;
    }

    public void setNumberOfRatings(final Integer numberOfRatings) {
        mNumberOfRatings = numberOfRatings;
    }

    public String getShortDescription() {
        return mShortDescription;
    }

    public void setShortDescription(final String shortDescription) {
        mShortDescription = shortDescription;
    }

    public Integer getPrice() {
        return mPrice;
    }

    public void setPrice(final Integer price) {
        mPrice = price;
    }

    public String getPackshotImageUrl() {
        return mPackshotImageUrl;
    }

    public void setPackshotImageUrl(final String packshotImageUrl) {
        mPackshotImageUrl = packshotImageUrl;
    }

    public String getWallpaperImageUrl() {
        return mWallpaperImageUrl;
    }

    public void setWallpaperImageUrl(final String wallpaperImageUrl) {
        mWallpaperImageUrl = wallpaperImageUrl;
    }
}
