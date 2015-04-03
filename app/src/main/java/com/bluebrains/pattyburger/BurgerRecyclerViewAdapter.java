package com.bluebrains.pattyburger;

import android.content.Context;
import android.content.Intent;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

        public RestaurantViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.thumbnail = (ImageView)view.findViewById(R.id.thumbnail);
            this.name = (TextView)view.findViewById(R.id.name);
        }

        @Override
        public void onClick(View v) {
            Log.d("HI", "Hi there! " + mResList.get(getPosition()).getmName());
            Intent intent = new Intent(mContext,RestaurantMeals.class);
            intent.putExtra(RES_ID,mResList.get(getPosition()).getmID());
            mContext.startActivity(intent);
        }
    }

}

