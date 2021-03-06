package com.bluebrains.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Molham on 4/2/2015.
 */
public class Meal implements Parcelable {
    private Integer mID;
    private String mName;
    private String mType;
    private double mPrice;
    private double mTime;
    private double mRating;
    private String mDescription;
    private String mImage;
    private Integer mResID;
    private ArrayList<String> mSpecs;
    public Meal(){

    }
    public Meal(Integer id,String mName, String mType, double mPrice, double mTime, String mDescription, String mImage, double rating) {
        this.mID = id;
        this.mName = mName;
        this.mType = mType;
        this.mPrice = mPrice;
        this.mTime = mTime;
        this.mDescription = mDescription;
        this.mImage = mImage;
        this.mRating = rating;
        mSpecs = new ArrayList<>();
    }

    public ArrayList<String> getmSpecs() {
        return mSpecs;
    }

    public void setmSpecs(ArrayList<String> mSpecs) {
        this.mSpecs = mSpecs;
    }

    public Integer getmResID() {
        return mResID;
    }

    public void setmResID(Integer mResID) {
        this.mResID = mResID;
    }

    public Integer getmID() {
        return mID;
    }

    public void setmID(Integer mID) {
        this.mID = mID;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    public double getmPrice() {
        return mPrice;
    }

    public void setmPrice(double mPrice) {
        this.mPrice = mPrice;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }

    public double getmTime() {

        return mTime;
    }

    public void setmTime(double mTime) {
        this.mTime = mTime;
    }

    public double getmRating() {
        return mRating;
    }

    public void setmRating(double mRating) {
        this.mRating = mRating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mType);
        dest.writeString(mDescription);
        dest.writeString(mImage);
        dest.writeDouble(mPrice);
        dest.writeDouble(mTime);
    }

    public static final Parcelable.Creator<Meal> CREATOR = new Parcelable.Creator<Meal>() {
        public Meal createFromParcel(Parcel in) {
            return new Meal(in);
        }

        public Meal[] newArray(int size) {
            return new Meal[size];
        }
    };

    private Meal(Parcel in) {
        mName = in.readString();
        mType = in.readString();
        mDescription = in.readString();
        mImage = in.readString();
        mPrice = in.readDouble();
        mTime = in.readDouble();
    }
}
