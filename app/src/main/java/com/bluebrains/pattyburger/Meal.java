package com.bluebrains.pattyburger;

/**
 * Created by Molham on 4/2/2015.
 */
public class Meal {
    private String mName;
    private String mType;
    private double mPrice;
    private String mDescription;
    private String mImage;

    public Meal(String mName, String mType, double mPrice, String mDescription, String mImage) {
        this.mName = mName;
        this.mType = mType;
        this.mPrice = mPrice;
        this.mDescription = mDescription;
        this.mImage = mImage;
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

    public void setmPrice(int mPrice) {
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
}
