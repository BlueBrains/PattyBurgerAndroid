package com.bluebrains.pattyburger;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;


public class RestaurantMeals extends ActionBarActivity {

    private static String LOG_TAG = RestaurantMeals.class.getName();
    private List<Meal> mMealList = new ArrayList<Meal>();
    private RecyclerView mRecyclerView;
    private MealRecyclerViewAdapter mealRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_meals);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        int id = getIntent().getIntExtra(BurgerRecyclerViewAdapter.RES_ID,-1);
        ProccessMeals proccessMeals = new ProccessMeals(id);
        proccessMeals.execute();
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
        }

        return super.onOptionsItemSelected(item);
    }

    public class ProccessMeals extends GetMealJsonData {

        public ProccessMeals(int id) {
            super(id);
        }

        public void execute(){
            ProccessData proccessData = new ProccessData();
            proccessData.execute();
        }

        public class ProccessData extends DownloadJsonData {
            protected void onPostExecute(String webData){
                super.onPostExecute(webData);
                MealRecyclerViewAdapter mealRecyclerViewAdapter = new MealRecyclerViewAdapter(RestaurantMeals.this,getmMeals());
                mRecyclerView.setAdapter(mealRecyclerViewAdapter);
            }

        }
    }
}
