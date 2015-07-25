package com.bluebrains.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.bluebrains.pattyburger.R;

public class LandingActivity extends ActionBarActivity {

    Handler mHandler;
    Runnable mNextActivityCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        mHandler = new Handler();
        mNextActivityCallback = new Runnable() {
            @Override
            public void run() {
                // Intent to jump to the next activity
                Intent intent= new Intent(LandingActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // so the splash activity goes away
            }
        };
        mHandler.postDelayed(mNextActivityCallback, 2000L);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            mHandler.removeCallbacks(mNextActivityCallback);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_landing, menu);
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
}