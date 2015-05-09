package com.bluebrains.pattyburger;

import android.content.Context;
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
 * Created by Molham on 4/2/2015.
 */
public class MealRecyclerViewAdapter extends RecyclerView.Adapter<MealRecyclerViewAdapter.MealViewHolder>{
    private List<Meal> mMealList;
    private Context mContext;
    private final String LOG_TAG = BurgerRecyclerViewAdapter.class.getName();


    public MealRecyclerViewAdapter(Context context, List<Meal> mealList) {
        this.mMealList = mealList;
        this.mContext = context;
    }

    @Override
    public void onBindViewHolder(MealViewHolder mealViewHolder, int i) {
        Meal mealItem = mMealList.get(i);
        Log.d(LOG_TAG, "Processing " + mealItem.getmName() + " ---> " + Integer.toString(i));
        Picasso.with(mContext).load(mealItem.getmImage())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(mealViewHolder.thumbnail);
        mealViewHolder.name.setText(mealItem.getmName());
    }

    @Override
    public MealViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.restaurant,null);
        MealViewHolder mealViewHolder = new MealViewHolder(view);
        return mealViewHolder;
    }

    @Override
    public int getItemCount() {
        return (null!= mMealList ? mMealList.size() : 0);
    }

    class MealViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener{
        protected ImageView thumbnail;
        protected TextView name;

        public MealViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.thumbnail = (ImageView)view.findViewById(R.id.thumbnail);
            this.name = (TextView)view.findViewById(R.id.name);
        }

        @Override
        public void onClick(View v) {
            Log.d("HI", "Hi there! " + mMealList.get(getPosition()).getmName());
        }
    }

}