package com.bluebrains.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.bluebrains.pattyburger.BurgerRecyclerViewAdapter;
import com.bluebrains.pattyburger.CartList;
import com.bluebrains.pattyburger.R;
import com.bluebrains.pattyburger.RestaurantTab;


public class RestaurantMenu extends ActionBarActivity implements RestaurantTab.OnFragmentInteractionListener {

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private static String LOG_TAG = RestaurantMenu.class.getName();

    Toolbar toolbar;
    //ViewPagerAdapter adapter;
    //com.example.android.common.view.SlidingTabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_menu);
        //toolbar = (Toolbar) findViewById(R.id.tool_bar);
        //setSupportActionBar(toolbar);
//        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        int id = getIntent().getIntExtra(BurgerRecyclerViewAdapter.RES_ID,-1);
//        ProccessMeals proccessMeals = new ProccessMeals(id);
//        proccessMeals.execute();


        int restaurantId = getIntent().getIntExtra(BurgerRecyclerViewAdapter.RES_ID,-1);
        int restaurantTabNum = 2;
        Bundle args = new Bundle();
        args.putInt(RestaurantTab.ARG_PARAM1, restaurantId);
        args.putInt(RestaurantTab.ARG_PARAM2, restaurantTabNum);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            RestaurantTab fragment = new RestaurantTab();
            fragment.setArguments(args);
            transaction.replace(R.id.tabs_fragment, fragment);
            transaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_restaurant_meals, menu);
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
            Intent intent = new Intent(RestaurantMenu.this, CartList.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}
