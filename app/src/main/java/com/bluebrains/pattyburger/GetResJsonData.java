package com.bluebrains.pattyburger;

import android.net.Uri;
import android.util.Log;

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
        final String RES_NAME = "res_name";
        final String RES_DESCRIPTION = "res_logo";
        final String RES_LOGO_URL = "res_logo";
        final String RES_ID = "id";
        try {
            JSONObject jsonData = new JSONObject(getmData());
            JSONArray itemsArray = jsonData.getJSONArray(RES_ITEMS);
            for (int i = 0; i<itemsArray.length();i++){
                JSONObject jsonRes = itemsArray.getJSONObject(i);
                String name = jsonRes.getString(RES_NAME);
                String description = jsonRes.getString(RES_DESCRIPTION);
                int id = jsonRes.getInt(RES_ID);
                //String logoUrl = jsonRes.getString(RES_LOGO_URL);
                Restaurant resObject = new Restaurant(name,null,null,null,null,description,id);

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
