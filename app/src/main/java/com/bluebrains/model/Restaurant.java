package com.bluebrains.model;

/**
 * Created by Molham on 3/30/2015.
 */
public class Restaurant {
    private String mName;
    private String mLocation;
    private String mType;
    private String mImage;
    private String mLink;
    private String mDescription;
    private int mID;
    private double mRating;
    private double mLat;
    private double mLng;
    public double getmLat() {
        return mLat;
    }

    public void setmLat(double mLat) {
        this.mLat = mLat;
    }

    public double getmLng() {
        return mLng;
    }

    public void setmLng(double mLng) {
        this.mLng = mLng;
    }


    public double getmRating() {
        return mRating;
    }

    public void setmRating(float mRating) {
        this.mRating = mRating;
    }


    //private List<Meal> mMeals;

    public Restaurant(String mName, String mLocation, String mType, String mImage, String mLink, String mDescription, int mID,double mRating) {
        this.mName = mName;
        this.mLocation = mLocation;
        this.mType = mType;
        this.mImage = mImage;
        this.mLink = mLink;
        this.mDescription = mDescription;
        this.mID = mID;
        this.mRating=mRating;
    }
    public Restaurant(String mName, String mLocation, String mType, String mImage, String mLink, String mDescription, int mID,double mRating,double mLat,double mLng) {
        this.mName = mName;
        this.mLocation = mLocation;
        this.mType = mType;
        this.mImage = mImage;
        this.mLink = mLink;
        this.mDescription = mDescription;
        this.mID = mID;
        this.mRating=mRating;
        this.mLat=mLat;
        this.mLng=mLng;
    }
    public String getmName() {
        return mName;
    }

    public String getmLocation() {
        return mLocation;
    }

    public String getmType() {
        return mType;
    }

    public String getmImage() {
        return mImage;
    }

    public String getmLink() {
        return mLink;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }

    public void setmLink(String mLink) {
        this.mLink = mLink;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public int getmID() {
        return mID;
    }

    public void setmID(int mID) {
        this.mID = mID;
    }
}
