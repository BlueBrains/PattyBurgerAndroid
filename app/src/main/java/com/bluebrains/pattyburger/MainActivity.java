package com.bluebrains.pattyburger;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    private static String LOG_TAG = MainActivity.class.getName();
    private List<Restaurant> mPhotoList = new ArrayList<Restaurant>();
    private RecyclerView mRecyclerView;
    private BurgerRecyclerViewAdapter burgerRecyclerViewAdapter;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ProccessRes proccessRes = new ProccessRes();
        proccessRes.execute();
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class ProccessRes extends GetResJsonData {

        public ProccessRes() {
            super();
        }

        public void execute(){
            ProccessData proccessData = new ProccessData();
            proccessData.execute();
        }

        public class ProccessData extends DownloadJsonData{
            protected void onPreExecute() {
                super.onPreExecute();
                // Showing progress dialog
                pDialog = new ProgressDialog(MainActivity.this);
                pDialog.setMessage("Please wait...");
                pDialog.setCancelable(false);
                pDialog.show();

            }
            protected void onPostExecute(String webData){
                super.onPostExecute(webData);
                if (pDialog.isShowing())
                    pDialog.dismiss();
                BurgerRecyclerViewAdapter burgerRecyclerViewAdapter =
                        new BurgerRecyclerViewAdapter(MainActivity.this,getmRestaurants());
                mRecyclerView.setAdapter(burgerRecyclerViewAdapter);

            }

        }
    }
}
