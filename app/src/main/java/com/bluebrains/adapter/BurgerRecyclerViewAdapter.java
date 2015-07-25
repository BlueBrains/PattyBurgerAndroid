package com.bluebrains.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bluebrains.Activity.FragmentMap;
import com.bluebrains.Activity.FragmentRestaurantDetails;
import com.bluebrains.model.Restaurant;
import com.bluebrains.pattyburger.R;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Molham on 3/30/2015.
 */

public class BurgerRecyclerViewAdapter extends RecyclerView.Adapter<BurgerRecyclerViewAdapter.RestaurantViewHolder>{
    private List<Restaurant> mResList;
    private Context mContext;
    private final String LOG_TAG = BurgerRecyclerViewAdapter.class.getName();
    public final static String RES_ID = "RES_ID";


    public BurgerRecyclerViewAdapter(Context context, List<Restaurant> resList) {
        this.mResList = resList;
        this.mContext = context;
    }

    @Override
    public void onBindViewHolder(RestaurantViewHolder resViewHolder, int i) {
        Restaurant resItem = mResList.get(i);
        Log.d(LOG_TAG, "Processing " + resItem.getmName() + " ---> " + Integer.toString(i));
        Picasso.with(mContext).load(resItem.getmImage())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(resViewHolder.thumbnail);
        resViewHolder.name.setText(resItem.getmName());
        resViewHolder.description.setText(resItem.getmDescription());
        resViewHolder.rating.setRating((float)resItem.getmRating());
    }

    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.restaurant,null);
        RestaurantViewHolder restaurantViewHolder = new RestaurantViewHolder(view);
        return restaurantViewHolder;
    }

    @Override
    public int getItemCount() {
        return (null!= mResList ? mResList.size() : 0);
    }

    class RestaurantViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener{
        protected ImageView thumbnail;
        protected TextView name;
        protected  TextView description;
        protected RatingBar rating;
        protected double lat;
        protected double lng;
        protected ImageView MapButton;

        public RestaurantViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.thumbnail = (ImageView)view.findViewById(R.id.thumbnail);
            this.name = (TextView)view.findViewById(R.id.name);
            this.description = (TextView)view.findViewById(R.id.description);
            this.rating=(RatingBar)view.findViewById(R.id.body_ratingBar);
            this.MapButton=(ImageView)view.findViewById(R.id.map);
            MapButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle args = new Bundle();
                    lat=mResList.get(getPosition()).getmLat();
                    lng=mResList.get(getPosition()).getmLng();
                    args.putDouble(FragmentMap.LAT, lat);
                    args.putDouble(FragmentMap.LNG, lng);
                    if (mContext instanceof FragmentActivity) {
                        // We can get the fragment manager
                        FragmentActivity activity = (FragmentActivity)(mContext);
                        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                        FragmentMap fragment = new FragmentMap();
                        fragment.setArguments(args);
                        transaction.replace(R.id.container_body, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        ((ActionBarActivity)mContext).getSupportActionBar().setTitle(R.string.title_restaurant_location);
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            Log.d("HI", "Hi there! " + mResList.get(getPosition()).getmName());
            Bundle args = new Bundle();
            args.putParcelable(FragmentRestaurantDetails.ARG_RES,mResList.get(getPosition()));
            if (mContext instanceof FragmentActivity) {
                // We can get the fragment manager
                FragmentActivity activity = (FragmentActivity)(mContext);
                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                FragmentRestaurantDetails fragment = new FragmentRestaurantDetails();
                fragment.setArguments(args);
                transaction.replace(R.id.container_body, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                ((ActionBarActivity)mContext).getSupportActionBar().setTitle(R.string.title_restaurant_details);
            }
        }
    }

}

