package com.bluebrains.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Molham on 3/30/2015.
 */
public class Restaurant implements Parcelable{

    private String mName;
    private String mLocation;
    private String mType;
    private String mImage;
    private String mDescription;
    private String mPhone;
    private int mID;
    private int mCategoryNum;
    private boolean mDeliverable;
    private double mRating;
    private double mPriceRange;
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

    public Restaurant(String name, String location, String type, String image, String description, int id, double rating, double lat, double lng, String phone, boolean deliverable, double priceRange) {
        this.mName = name;
        this.mLocation = location;
        this.mType = type;
        this.mImage = image;
        this.mDescription = description;
        this.mID = id;
        this.mRating=rating;
        this.mPhone = phone;
        this.mDeliverable = deliverable;
        this.mLat = lat;
        this.mLng = lng;
        this.mPriceRange = priceRange;
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

    public boolean ismDeliverable() {
        return mDeliverable;
    }

    public void setmDeliverable(boolean mDeliverable) {
        this.mDeliverable = mDeliverable;
    }

    public String getmPhone() {
        return mPhone;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public double getmPriceRange() {
        return mPriceRange;
    }

    public void setmPriceRange(double mPriceRange) {
        this.mPriceRange = mPriceRange;
    }

    public int getmCategoryNum() {
        return mCategoryNum;
    }

    public void setmCategoryNum(int mCategoryNum) {
        this.mCategoryNum = mCategoryNum;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mLocation);
        dest.writeString(mType);
        dest.writeString(mImage);
        dest.writeString(mDescription);
        dest.writeString(mPhone);
        dest.writeInt(mID);
        dest.writeDouble(mRating);
        dest.writeDouble(mLat);
        dest.writeDouble(mLng);
        dest.writeDouble(mLat);
        dest.writeDouble(mPriceRange);
        dest.writeInt(mDeliverable ? 1 : 0);
    }
    public static final Parcelable.Creator<Restaurant> CREATOR = new Parcelable.Creator<Restaurant>() {
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    private Restaurant(Parcel in) {
        mName = in.readString();
        mLocation = in.readString();
        mType = in.readString();
        mImage = in.readString();
        mDescription = in.readString();
        mPhone = in.readString();
        mID = in.readInt();
        mRating = in.readDouble();
        mLat = in.readDouble();
        mLng = in.readDouble();
        mLat = in.readDouble();
        mPriceRange = in.readDouble();
        mDeliverable = in.readInt() == 1 ? true : false;
    }
    @Override
    public int describeContents() {
        return 0;
    }
}

