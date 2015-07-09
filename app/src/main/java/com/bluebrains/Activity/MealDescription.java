package com.bluebrains.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bluebrains.pattyburger.CartList;
import com.bluebrains.pattyburger.Controller;
import com.bluebrains.pattyburger.Meal;
import com.bluebrains.pattyburger.R;
import com.bluebrains.pattyburger.RestaurantTab;
import com.squareup.picasso.Picasso;


public class MealDescription extends ActionBarActivity {

    private TextView mMealName;
    private TextView mMealDescription;
    private TextView mMealPrice;
    private ImageView mMealImage;
    private Button mAddToCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_description);
        final Controller mController =(Controller) getApplicationContext();

        mMealName = (TextView)findViewById(R.id.mealName);
        mMealDescription = (TextView)findViewById(R.id.mealDescription);
        mMealPrice = (TextView)findViewById(R.id.mealPrice);
        mMealImage = (ImageView)findViewById(R.id.mealImage);
        mAddToCart = (Button)findViewById(R.id.cartButton);
        final Meal currentMeal = (Meal) getIntent().getParcelableExtra(RestaurantTab.Item);
        mMealName.setText(currentMeal.getmName());
        mMealDescription.setText(currentMeal.getmDescription());
        mMealPrice.setText("Price\r\n"+currentMeal.getmPrice()+" s.p");
        Picasso.with(getApplicationContext()).load(currentMeal.getmImage())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(mMealImage);

        mAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"Not Supported yet",Toast.LENGTH_LONG).show();
                if(mController.getModelCart().checkItemInCart(currentMeal)){
                    Toast.makeText(getApplicationContext(), "You have already added this meal!", Toast.LENGTH_LONG).show();
                }else {
                    mController.getModelCart().setItem(currentMeal);
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_meal_description, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if (id == R.id.action_cart) {
            Intent intent = new Intent(MealDescription.this, CartList.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
