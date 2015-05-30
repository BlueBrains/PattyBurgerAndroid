package com.bluebrains.pattyburger;

import java.util.ArrayList;

/**
 * Created by Molham on 5/16/2015.
 */
public class ModelCart {

    private ArrayList<CartItem> cart = new ArrayList<CartItem>();


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
