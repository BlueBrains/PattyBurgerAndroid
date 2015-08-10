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

import com.bluebrains.activity.FragmentMeal;
import com.bluebrains.activity.FragmentRestaurantTab;
import com.bluebrains.model.Meal;
import com.bluebrains.pattyburger.R;
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
        mealViewHolder.rating.setRating((float)mealItem.getmRating());
        mealViewHolder.description.setText(mealItem.getmDescription());
    }

    @Override
    public MealViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.meal,null);
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
        protected RatingBar rating;
        protected TextView description;

        public MealViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.thumbnail = (ImageView)view.findViewById(R.id.thumbnail);
            this.name = (TextView)view.findViewById(R.id.name);
            this.rating = (RatingBar)view.findViewById(R.id.body_ratingBar);
            this.description = (TextView)view.findViewById(R.id.description);
        }

        @Override
        public void onClick(View v) {
            Log.d("HI", "Hi there! " + mMealList.get(getPosition()).getmName());
//            Intent intent = new Intent(mContext,MealDescription.class);
//            intent.putExtra(RestaurantTab.Item, mMealList.get(getPosition()));
//            mContext.startActivity(intent);
            Bundle args = new Bundle();
            args.putParcelable(FragmentRestaurantTab.Item, mMealList.get(getPosition()));
            if (mContext instanceof FragmentActivity) {
                // We can get the fragment manager
                FragmentActivity activity = (FragmentActivity)(mContext);
                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                FragmentMeal fragment = new FragmentMeal();
                fragment.setArguments(args);
                transaction.replace(R.id.container_body, fragment);
                transaction.addToBackStack(FragmentMeal.class.getSimpleName());
                transaction.commit();
                ((ActionBarActivity)mContext).getSupportActionBar().setTitle(R.string.title_meal_description);
            }
        }
    }

}
