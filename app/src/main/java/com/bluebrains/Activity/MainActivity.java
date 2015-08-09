package com.bluebrains.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;

import com.bluebrains.adapter.BurgerRecyclerViewAdapter;
import com.bluebrains.common.logger.Log;
import com.bluebrains.helper.SQLiteHandler;
import com.bluebrains.helper.SessionManager;
import com.bluebrains.pattyburger.R;


public class MainActivity extends ActionBarActivity implements FragmentDrawer.FragmentDrawerListener,
        FragmentDrawer.OnFragmentInteractionListener,
        FragmentRestaurants.OnFragmentInteractionListener,
        FragmentRestaurantTab.OnFragmentInteractionListener,
        FragmentMeal.OnFragmentInteractionListener,
        FragmentCart.OnFragmentInteractionListener,
        FragmentMap.OnFragmentInteractionListener,
        FragmentRestaurantDetails.OnFragmentInteractionListener,
        FragmentRegistration.OnFragmentInteractionListener,
        FragmentOrderDetails.OnFragmentInteractionListener,
        FragmentSubmitOrder.OnFragmentInteractionListener,
        FragmentWriteReview.OnFragmentInteractionListener,
        FragmentReadReviews.OnFragmentInteractionListener,
        FragmentNear.OnFragmentInteractionListener,FragmentRestaurantsarch.OnFragmentInteractionListener,
        AdapterView.OnItemSelectedListener
        {

    private static String LOG_TAG = MainActivity.class.getName();
    private RecyclerView mRecyclerView;
    private BurgerRecyclerViewAdapter burgerRecyclerViewAdapter;
    private ProgressDialog pDialog;
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private int type=0;

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
                if (f != null) {
                    updateTitleAndDrawer(f);
                }
            }
        });
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        Intent intent = getIntent();
        if(!intent.hasExtra("com.parse.Data"))
            displayView(0);
        else{
            FragmentWriteReview fragment = FragmentWriteReview.newInstance(intent.getStringExtra("com.parse.Data"));
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.addToBackStack(FragmentWriteReview.class.getSimpleName());
            fragmentTransaction.commit();
            getSupportActionBar().setTitle(R.string.title_write_review);
        }
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
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.container_body);
            if (f.getClass().getName().equals(FragmentOrderDetails.class.getName())) {
                super.onBackPressed();
//                getSupportFragmentManager().beginTransaction()
//                        .remove(getSupportFragmentManager()
//                                .findFragmentByTag(FragmentRegistration.class.getSimpleName()))
//                        .add(getSupportFragmentManager().
//                                findFragmentByTag(FragmentCart.class.getSimpleName()),
//                                FragmentCart.class.getSimpleName())
//                .commit();
            }else{
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                type=0;
                // Whatever you want to happen when the first item gets selected
                break;
            case 1:
                type=1;
                // Whatever you want to happen when the second item gets selected
                break;

        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
//            startService(new Intent(this,PusherReciver.class));
            SQLiteHandler db;
            db = new SQLiteHandler(getApplicationContext());
            db.deleteUsers();
            SessionManager session = new SessionManager(this);
            session.logoutSession();
        }else if (id == R.id.action_cart) {
            FragmentCart fragment = new FragmentCart();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.addToBackStack(FragmentCart.class.getSimpleName());
            fragmentTransaction.commit();
            getSupportActionBar().setTitle(R.string.title_cart_list);
        }else if(id == R.id.action_near) {
            FragmentActivity activity = this;
            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
            FragmentNear fragment = new FragmentNear();
            transaction.replace(R.id.container_body, fragment);
            transaction.addToBackStack(FragmentMap.class.getSimpleName());
            transaction.commit();
            this.getSupportActionBar().setTitle(R.string.title_restaurant_location);
        }
        else if(id == R.id.action_search_res) {

            AlertDialog.Builder alert = new AlertDialog.Builder(this);


            alert.setMessage("Search For Restaurants");
            alert.setTitle("Search");

            LayoutInflater inflater = LayoutInflater.from(this);
            View dialogview = inflater.inflate(R.layout.dialoglayout, null);
            final EditText edittext= (EditText)dialogview.findViewById(R.id.edittext);//new EditText(this);
            Spinner spinner;
            String[]paths = {"Restaurant name", "Restaurant address"};
            spinner = (Spinner)dialogview.findViewById(R.id.spinner1);
            ArrayAdapter<String>adapter = new ArrayAdapter<String>(MainActivity.this,
                    android.R.layout.simple_spinner_item,paths);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
            alert.setView(dialogview);

            alert.setPositiveButton("Search", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //What ever you want to do with the value

                    String YouEditTextValue = edittext.getText().toString();
                   // Toast.makeText(getApplication(),YouEditTextValue+" amer",Toast.LENGTH_LONG).show();
                  //  Toast.makeText(getApplication(),"type= "+type,Toast.LENGTH_LONG).show();

// set Fragmentclass Arguments
                    Fragment fragment = new FragmentRestaurantsarch();
                    if (fragment != null) {
                        Bundle bundle = new Bundle();
                        bundle.putString("param1", YouEditTextValue);
                        bundle.putInt("param2", type);
                        FragmentManager fragmentManager = getSupportFragmentManager();

                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragment.setArguments(bundle);
                        fragmentTransaction.replace(R.id.container_body, fragment);
                        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
                        fragmentTransaction.commit();

                        // set the toolbar title
                        getSupportActionBar().setTitle("search");
                    }


                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // what ever you want to do with No option.
                }
            });

            alert.show();
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
            fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
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
