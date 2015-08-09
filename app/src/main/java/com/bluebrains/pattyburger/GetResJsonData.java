package com.bluebrains.pattyburger;

import android.net.Uri;
import android.util.Log;

import com.bluebrains.app.AppConfig;
import com.bluebrains.model.Restaurant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Molham on 3/30/2015.
 */
public class GetResJsonData extends GetRowData{
    private String LOG_TAG = GetResJsonData.class.getName();
    private List<Restaurant> mRestaurants;
    private Uri mDestinationUri;

    public GetResJsonData(){
        super(null,GET);
        createAndroidUpdateUri();
        mRestaurants = new ArrayList<Restaurant>();
    }

    public void execute (){
        super.setmRowUrl(mDestinationUri.toString());
        DownloadJsonData downloadJsonData = new DownloadJsonData();
        Log.v(LOG_TAG,"Built URI = "+ mDestinationUri.toString());
        downloadJsonData.execute(mDestinationUri.toString());

    }
    public boolean createAndroidUpdateUri(){
        mDestinationUri = Uri.parse(AppConfig.BASE_URL+"restaurants/ress");
        return mDestinationUri != null;
    }

    public List<Restaurant> getmRestaurants() {
        return mRestaurants;
    }

    public void processResult(){
        if(getmDownloadStatus() != DownloadStatus.OK){
            Log.e(LOG_TAG,"Error downloading row file");
            return;
        }
        final String RES_ITEMS = "restaurants";
        final String RES_ID = "id";
        final String RES_NAME = "name";
        final String RES_DESCRIPTION = "description";
        final String RES_LOGO = "logo";
        final String RES_RANGE = "price_range";
        final String RES_ADDRESS = "address";
        final String RES_LAT="lat";
        final String RES_LNG="lng";
        final String RES_RATE="rate";
        final String RES_DELIVERABLE="deliverable";
        final String RES_TYPE="category_name";
        final String RES_PHONE="phone_nbr_1";
        final String RES_CATEGORY_NUM = "category_num";


        try {
            JSONObject jsonData = new JSONObject(getmData());
            JSONArray itemsArray = jsonData.getJSONArray(RES_ITEMS);
            for (int i = 0; i<itemsArray.length();i++){
                JSONObject jsonRes = itemsArray.getJSONObject(i);
                String name = jsonRes.getString(RES_NAME);
                String address=jsonRes.getString(RES_ADDRESS);
                String description = jsonRes.getString(RES_DESCRIPTION);
                String type=jsonRes.getString(RES_TYPE);
                String logoUrl = jsonRes.getString(RES_LOGO);
                String phone = jsonRes.getString(RES_PHONE);
                double lat=jsonRes.getDouble(RES_LAT);
                double lng=jsonRes.getDouble(RES_LNG);
                double rating=jsonRes.getDouble(RES_RATE);
                double priceRange = jsonRes.getDouble(RES_RANGE);
                int id = jsonRes.getInt(RES_ID);
//                int categoryNum = jsonRes.getInt(RES_CATEGORY_NUM);
                boolean deliverable = jsonRes.getInt(RES_DELIVERABLE) == 1 ? true : false;
                //double res_rating=people_number<1?0:(rating/people_number);
                Restaurant resObject = new Restaurant(name, address, type, logoUrl, description, id, rating, lat, lng, phone, deliverable, priceRange);

                this.mRestaurants.add(resObject);
            }
            for(Restaurant singleRes : mRestaurants){
                Log.v(LOG_TAG, singleRes.toString());
            }

        }catch (JSONException jsone){
            jsone.printStackTrace();
            Log.e(LOG_TAG, "Error processing Json data");
        }
    }

    public class DownloadJsonData extends DownloadRowData{

        protected void onPostExecute(String webData){
            super.onPostExecute(webData);
            processResult();
        }

        protected String doInBackground(String... params){
            String [] par = {mDestinationUri.toString()};
            return super.doInBackground(par);
        }
    }
}
