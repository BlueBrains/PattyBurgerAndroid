package com.bluebrains.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bluebrains.app.Controller;
import com.bluebrains.model.CartItem;
import com.bluebrains.pattyburger.CartRecyclerViewAdapter;
import com.bluebrains.pattyburger.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CartList extends ActionBarActivity {

    private RecyclerView mRecyclerView;
    private Button mSubmit;
    private Context mContext;
    private CartRecyclerViewAdapter mCartRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);
        final Controller mController =(Controller) getApplicationContext();
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mSubmit = (Button) findViewById(R.id.cart_order_button);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCartRecyclerViewAdapter = new CartRecyclerViewAdapter(getApplicationContext(),mController.getModelCart().getCart());
        mRecyclerView.setAdapter(mCartRecyclerViewAdapter);
        mSubmit.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   ArrayList<CartItem> cart =  mController.getModelCart().getCart();
                                                   final String USER_ID = "user_id";
                                                   final String ORDER = "order";
                                                   final String ITEM_ID = "item_id";
                                                   final String ITEM_SPEC = "item_spec";
                                                   final String ITEM_COUNT = "item_count";
                                                   JSONArray meals = new JSONArray();
                                                   JSONObject order = new JSONObject();
                                                   try{
                                                       for(CartItem item : cart) {
                                                           JSONObject jsonItem = new JSONObject();
                                                           jsonItem.put(ITEM_ID, 1);
                                                           jsonItem.put(ITEM_SPEC, "spec");
                                                           jsonItem.put(ITEM_COUNT, 12);

                                                           meals.put(jsonItem);
                                                       }
                                                       order.put(USER_ID, 1).put(ORDER, meals);
                                                   }catch (JSONException e) {
                                                       e.printStackTrace();
                                                   }
                                                   // Tag used to cancel the request
                                                   final String TAG_SUBMIT = "submit_cart";
                                                   Log.d(TAG_SUBMIT, order.toString());
                                                   String url = "http://10.0.3.2/burger_ownercp/index.php/res/order";

                                                   JsonObjectRequest orderObj = new JsonObjectRequest(Request.Method.POST,url,order,
                                                           new Response.Listener<JSONObject>(){
                                                               @Override
                                                               public void onResponse(JSONObject response) {
                                                                   Log.d(TAG_SUBMIT, response.toString());
                                                               }
                                                           }, new Response.ErrorListener(){
                                                       @Override
                                                       public void onErrorResponse(VolleyError error) {
                                                           VolleyLog.d(TAG_SUBMIT, "Error: " + error.getMessage());
                                                           //Log.d(TAG_SUBMIT, order.toString());
                                                       }
                                                   }){
                                                       /**
                                                        * Passing some request headers
                                                        * */
                                                       @Override
                                                       public Map<String, String> getHeaders() throws
                                                       AuthFailureError {
                                                           HashMap<String, String> headers = new HashMap<String, String>();
                                                           headers.put("Content-Type", "application/json");
                                                           headers.put( "charset", "utf-8");
                                                           return headers;
                                                       }
                                                   };
                                                    // Adding request to request queue
                                                   mController.getInstance().addToRequestQueue(orderObj, TAG_SUBMIT);
                                               }
                                           });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cart_list, menu);
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
