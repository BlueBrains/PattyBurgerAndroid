package com.bluebrains.model;

/**
 * Created by Molham on 5/17/2015.
 */
public class CartItem extends Meal {
    private int mCount;

    public CartItem(Meal meal){
        super(meal.getmName(), meal.getmType(), meal.getmPrice(), meal.getmTime(), meal.getmDescription(), meal.getmImage(), meal.getmRating());
        this.mCount = 1;
    }

    public int getmCount() {
        return mCount;
    }

    public void setmCount(int mCount) {
        this.mCount = mCount;
    }

    public void incCounter(){
        mCount++;
    }
    public void decCounter(){
        if(mCount>0)mCount--;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CartItem cartIetm = (CartItem) o;

        if (getmName() != cartIetm.getmName()) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return mCount;
    }
}
