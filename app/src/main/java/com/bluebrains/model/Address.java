package com.bluebrains.model;

/**
 * Created by Molham on 7/27/2015.
 */
public class Address {
    private String mArea;
    private String mPlaceType;
    private String mStreetDetails;
    private String mHouseDetails;
    private String mBlock;
    private String mInfo;

    public Address(String mArea, String mPlaceType, String mStreetDetails, String mHouseDetails, String mBlock, String mInfo) {
        this.mArea = mArea;
        this.mPlaceType = mPlaceType;
        this.mStreetDetails = mStreetDetails;
        this.mHouseDetails = mHouseDetails;
        this.mBlock = mBlock;
        this.mInfo = mInfo;
    }

    public String getmArea() {
        return mArea;
    }

    public void setmArea(String mArea) {
        this.mArea = mArea;
    }

    public String getmPlaceType() {
        return mPlaceType;
    }

    public void setmPlaceType(String mPlaceType) {
        this.mPlaceType = mPlaceType;
    }

    public String getmStreetDetails() {
        return mStreetDetails;
    }

    public void setmStreetDetails(String mStreetDetails) {
        this.mStreetDetails = mStreetDetails;
    }

    public String getmHouseDetails() {
        return mHouseDetails;
    }

    public void setmHouseDetails(String mHouseDetails) {
        this.mHouseDetails = mHouseDetails;
    }

    public String getmBlock() {
        return mBlock;
    }

    public void setmBlock(String mBlock) {
        this.mBlock = mBlock;
    }

    public String getmInfo() {
        return mInfo;
    }

    public void setmInfo(String mInfo) {
        this.mInfo = mInfo;
    }
}