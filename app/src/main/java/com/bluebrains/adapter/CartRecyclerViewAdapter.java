package com.bluebrains.adapter;

/**
 * Created by Molham on 5/16/2015.
 */

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bluebrains.app.Controller;
import com.bluebrains.model.CartItem;
import com.bluebrains.pattyburger.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartRecyclerViewAdapter extends  RecyclerView.Adapter<CartRecyclerViewAdapter.ItemViewHolder>{
    private List<CartItem> mItemList;
    private Context mContext;
    private Controller mController;
    private final String LOG_TAG = CartRecyclerViewAdapter.class.getName();
//    private int VIEW_TYPE_CELL = 0;
//    private int VIEW_TYPE_FOOTER = 1;



    public CartRecyclerViewAdapter(Context context, List<CartItem> itemList) {
        this.mItemList = itemList;
        this.mContext = context;
        this.mController = (Controller)context.getApplicationContext();
    }

    @Override
    public void onBindViewHolder(ItemViewHolder itemViewHolder, int i) {
        //if(itemViewHolder instanceof  CellView)
        CartItem item = mItemList.get(i);
        Log.d(LOG_TAG, "Processing " + item.getmName() + " ---> " + Integer.toString(i));
        Picasso.with(mContext).load(item.getmImage())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(itemViewHolder.thumbnail);
        itemViewHolder.itemTitle.setText(item.getmName());
        itemViewHolder.itemPrice.setText(item.getmPrice()+" s.p");
        itemViewHolder.totalPrice.setText(item.getmPrice() * item.getmCount() + " s.p");
        itemViewHolder.counter.setText(item.getmCount()+"");

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view=null;
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_item,null);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public int getItemCount() {
        return (null!= mItemList ? mItemList.size()  : 0);
    }

//    @Override
//    public int getItemViewType(int position) {
//        return 0;//(position == mItemList.size()) ? VIEW_TYPE_FOOTER : VIEW_TYPE_CELL;
//    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener{
        protected ImageView thumbnail;
        protected TextView itemTitle;
        protected TextView itemPrice;
        protected TextView totalPrice;
        protected TextView counter;
        protected ImageView incButton;
        protected ImageView decButton;
        protected ImageView deleteButton;

        public ItemViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.thumbnail = (ImageView)view.findViewById(R.id.thumbnail);
            this.itemTitle = (TextView)view.findViewById(R.id.item_title);
            this.itemPrice = (TextView)view.findViewById(R.id.item_price);
            this.totalPrice = (TextView)view.findViewById(R.id.total_cart_price);
            this.counter = (TextView)view.findViewById(R.id.counter);
            this.incButton = (ImageView)view.findViewById(R.id.add_button);
            this.decButton = (ImageView)view.findViewById(R.id.minus_button);
            this.deleteButton = (ImageView)view.findViewById(R.id.delete_item);

            deleteButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    double totalCoast = mController.getModelCart().getmTotalCoast();
                    double itemTotalCoast = (mItemList.get(getPosition()).getmPrice()*mItemList.get(getPosition()).getmCount());
                    mController.getModelCart().setmTotalCoast(totalCoast - itemTotalCoast);
                    mItemList.remove(getPosition());
                    CartRecyclerViewAdapter.this.notifyDataSetChanged();
                    if(mContext instanceof FragmentActivity){
                        FragmentActivity activity = (FragmentActivity)(mContext);
                        TextView orderCoast = (TextView)activity.findViewById(R.id.total_order_coast);
                        orderCoast.setText("Your order coast is: "+mController.getModelCart().getmTotalCoast()+"");
                    }
                    Toast.makeText(mContext, "Item deleted successfully", Toast.LENGTH_SHORT).show();
                }
            });

            incButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    double totalCoast = mController.getModelCart().getmTotalCoast();
                    double itemCoast = (mItemList.get(getPosition()).getmPrice());
                    mController.getModelCart().setmTotalCoast(totalCoast + itemCoast);
                    CartItem current= mItemList.get(getPosition());
                    current.incCounter();
                    counter.setText(current.getmCount()+"");
                    totalPrice.setText(current.getmPrice()*current.getmCount()+" s.p");
                    if(mContext instanceof FragmentActivity){
                        FragmentActivity activity = (FragmentActivity)(mContext);
                        TextView orderCoast = (TextView)activity.findViewById(R.id.total_order_coast);
                        orderCoast.setText("Your order coast is: "+mController.getModelCart().getmTotalCoast()+"");
                    }
                }
            });

            decButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CartItem current= mItemList.get(getPosition());
                    if(current.getmCount()>0){
                        double totalCoast = mController.getModelCart().getmTotalCoast();
                        double itemCoast = (mItemList.get(getPosition()).getmPrice());
                        mController.getModelCart().setmTotalCoast(totalCoast - itemCoast);
                        current.decCounter();
                        counter.setText(current.getmCount()+"");
                        totalPrice.setText(current.getmPrice()*current.getmCount()+" s.p");
                    }
                    if(mContext instanceof FragmentActivity){
                        FragmentActivity activity = (FragmentActivity)(mContext);
                        TextView orderCoast = (TextView)activity.findViewById(R.id.total_order_coast);
                        orderCoast.setText("Your order coast is: "+mController.getModelCart().getmTotalCoast()+"");
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            Log.d("HI", "Hi there! " + mItemList.get(getPosition()).getmName());
        }
    }
}
