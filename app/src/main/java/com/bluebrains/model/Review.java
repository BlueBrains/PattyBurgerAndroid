package com.bluebrains.model;

/**
 * Created by Molham on 7/28/2015.
 */
public class Review {
    private String mReview;
    private String mUserName;
    private String mImage;
    private double mRating;

    public Review(String mReview, String mUserName, double rating) {
        this.mReview = mReview;
        this.mUserName = mUserName;
        this.mRating = rating;
    }

    public String getmReview() {
        return mReview;
    }

    public void setmReview(String mReview) {
        this.mReview = mReview;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }

    public double getmRating() {
        return mRating;
    }

    public void setmRating(double mRating) {
        this.mRating = mRating;
    }
}
