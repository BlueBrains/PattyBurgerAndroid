package com.bluebrains.model;

import java.util.ArrayList;

/**
 * Created by Molham on 5/16/2015.
 */
public class ModelCart {

    private ArrayList<CartItem> cart = new ArrayList<CartItem>();
    private double mTotalCoast =0;
    private int mResId;
    private int mUserId;

    public int getmResId() {
        return mResId;
    }

    public void setmResId(int mResId) {
        this.mResId = mResId;
    }

    public int getmUserId() {
        return mUserId;
    }

    public void setmUserId(int mUserId) {
        this.mUserId = mUserId;
    }

    public double getmTotalCoast() {
        return mTotalCoast;
    }

    public void setmTotalCoast(double mTotalCoast) {
        this.mTotalCoast = mTotalCoast;
    }

    public Meal getItem(int pPosition) {
        return cart.get(pPosition);
    }

    public void setItem(Meal meal) {
        CartItem temp = new CartItem(meal);
        cart.add(temp);
    }

    public int getCartSize() {
        return cart.size();
    }

    public ArrayList<CartItem> getCart() {
        return cart;
    }

    public boolean checkItemInCart(Meal meal) {
        CartItem temp = new CartItem(meal);
        return cart.contains(temp);
    }
}
