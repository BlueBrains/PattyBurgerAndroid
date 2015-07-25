package com.bluebrains.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bluebrains.adapter.BurgerRecyclerViewAdapter;
import com.bluebrains.pattyburger.R;
import com.bluebrains.receiver.PusherReciver;


public class MainActivity extends ActionBarActivity implements FragmentDrawer.FragmentDrawerListener, FragmentDrawer.OnFragmentInteractionListener, FragmentRestaurants.OnFragmentInteractionListener, FragmentRestaurantTab.OnFragmentInteractionListener, FragmentMeal.OnFragmentInteractionListener, FragmentCart.OnFragmentInteractionListener, FragmentMap.OnFragmentInteractionListener, FragmentRestaurantDetails.OnFragmentInteractionListener{
    private static String LOG_TAG = MainActivity.class.getName();
    private RecyclerView mRecyclerView;
    private BurgerRecyclerViewAdapter burgerRecyclerViewAdapter;
    private ProgressDialog pDialog;
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {

            @Override
            public void onBackStackChanged() {
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.container_body);
                if (f != null){
                    updateTitleAndDrawer (f);
                }

            }
        });
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        displayView(0);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.quit)
                    .setMessage(R.string.really_quit)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            startService(new Intent(this,PusherReciver.class));
        }else if (id == R.id.action_cart) {
            FragmentCart fragment = new FragmentCart();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            getSupportActionBar().setTitle(R.string.title_cart_list);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }
    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new FragmentRestaurants();
                title = getString(R.string.title_restaurants);
                break;
            case 1:
                fragment = new FragmentCart();
                title = getString(R.string.title_cart_list);
                break;
            case 2:
                //fragment = new MessagesFragment();
                //title = getString(R.string.title_messages);
                break;
            case 3:
                //fragment = new MessagesFragment();
                //title = getString(R.string.title_messages);
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }
    private void updateTitleAndDrawer (Fragment fragment){
        String fragClassName = fragment.getClass().getName();

        if (fragClassName.equals(FragmentRestaurants.class.getName())){
            getSupportActionBar().setTitle (R.string.title_restaurants);
            //set selected item position, etc
        }else if (fragClassName.equals(FragmentRestaurantTab.class.getName())){
            getSupportActionBar().setTitle (R.string.title_restaurant_menu);
            //set selected item position, etc
        }else if (fragClassName.equals(FragmentMeal.class.getName())){
            getSupportActionBar().setTitle (R.string.title_meal_description);
            //set selected item position, etc
        }else if (fragClassName.equals(FragmentCart.class.getName())) {
            getSupportActionBar().setTitle(R.string.title_cart_list);
            //set selected item position, etc
        }else if (fragClassName.equals(FragmentCart.class.getName())){
            getSupportActionBar().setTitle (R.string.title_restaurant_location);
        }
    }
}
