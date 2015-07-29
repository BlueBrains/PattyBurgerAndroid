package com.bluebrains.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bluebrains.app.AppConfig;
import com.bluebrains.model.Review;
import com.bluebrains.pattyburger.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Molham on 7/28/2015.
 */
public class ReviewRecyclerViewAdapter extends RecyclerView.Adapter<ReviewRecyclerViewAdapter.ReviewViewHolder>{
    private Context mContext;
    private final String LOG_TAG = BurgerRecyclerViewAdapter.class.getName();
    private List<Review> mReviews;
    public ReviewRecyclerViewAdapter(Context mContext, ArrayList<Review> reviews) {
        this.mContext = mContext;
        this.mReviews = reviews;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review,null);
        ReviewViewHolder reviewViewHolder = new ReviewViewHolder(view);
        return reviewViewHolder;
    }

    @Override
    public int getItemCount() {
        return null!= mReviews ? mReviews.size() : 0;
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        Review review = mReviews.get(position);
        Log.d(LOG_TAG, review.toString());
        Picasso.with(mContext).load(review.getmImage())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(holder.thumbnail);
        holder.name.setText(review.getmUserName()+" said:");
        holder.reviewText.setText(review.getmReview());
        holder.ratingBar.setRating((float)review.getmRating());
        Picasso.with(mContext).load(AppConfig.BASE_URL+"uploads/user.png")
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(holder.thumbnail);

    }

    class ReviewViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener{

        protected ImageView thumbnail;
        protected TextView name;
        protected TextView reviewText;
        protected RatingBar ratingBar;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.thumbnail = (ImageView)itemView.findViewById(R.id.thumbnail);
            this.name = (TextView)itemView.findViewById(R.id.name);
            this.reviewText = (TextView)itemView.findViewById(R.id.new_review);
            this.ratingBar = (RatingBar)itemView.findViewById(R.id.reviewer_rate);
        }

        @Override
        public void onClick(View v) {
            Log.d("HI", "Hi there! " + mReviews.get(getPosition()).getmUserName());
        }
    }
}
