package com.bluebrains.pattyburger;

import android.net.Uri;
import android.util.Log;

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
        final String BURGER_API_BASE_URL = "http://burger.remmaz.com/index.php/restaurants/ress";
        final String FORMAT_PRAM = "format";

        mDestinationUri = Uri.parse(BURGER_API_BASE_URL).buildUpon()
                .appendQueryParameter(FORMAT_PRAM, "json")
                .build();
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
        final String RES_NAME = "name";
        final String RES_DESCRIPTION = "description";
        final String RES_LOGO = "logo";
        final String RES_RANGE = "price_range";
        final String RES_ID = "id";
        final String RES_ADDRESS = "address";
        final String RES_LAT="lat";
        final String RES_LNG="lng";
        final String RES_RATIO="rate";
        final String RES_DELIVERABLE="deliverable";
        final String RES_TYPE="category_name";
        final String RES_PHONE="phone_nbr_1";


        try {
            JSONObject jsonData = new JSONObject(getmData());
            JSONArray itemsArray = jsonData.getJSONArray(RES_ITEMS);
            for (int i = 0; i<itemsArray.length();i++){
                JSONObject jsonRes = itemsArray.getJSONObject(i);
                String name = jsonRes.getString(RES_NAME);
               // String address=jsonRes.getString(RES_ADDRESS);
                String description = jsonRes.getString(RES_DESCRIPTION);
                String type=jsonRes.getString(RES_TYPE);
                String logoUrl = jsonRes.getString(RES_LOGO);
                String phone = jsonRes.getString(RES_PHONE);
                double lat=jsonRes.getDouble(RES_LAT);
                double lng=jsonRes.getDouble(RES_LNG);
                double rating=jsonRes.getDouble(RES_RATIO);
                int range=jsonRes.getInt(RES_RANGE);
                int id =1;// jsonRes.getInt(RES_ID);

                //int people_number=jsonRes.getInt("people_number");
                //double res_rating=people_number<1?0:(rating/people_number);
                Restaurant resObject = new Restaurant(name,null,type,logoUrl,null,description,id,rating,lat,lng);

                //String logo="http://10.0.3.2/burger_ownercp/uploads/"+id+"/"+jsonRes.getString("res_logo");
//                int people_number=jsonRes.getInt("people_number");
//                double rating=jsonRes.getDouble("rating");
//                double res_rating=people_number<1?0:(rating/people_number);
                //String logoUrl = jsonRes.getString(RES_LOGO_URL);
                //Restaurant resObject = new Restaurant(name,address,null,logo,null,description,id,res_rating);


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
