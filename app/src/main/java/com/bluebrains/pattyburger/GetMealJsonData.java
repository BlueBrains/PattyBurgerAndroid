package com.bluebrains.pattyburger;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Molham on 3/31/2015.
 */
public class GetMealJsonData extends GetRowData{
    private String LOG_TAG = GetMealJsonData.class.getName();
    private List<Meal> mMeals;
    private Uri mDestinationUri;

    public GetMealJsonData(int id){
        super(null,GET);
        createAndroidUpdateUri(id);
        mMeals = new ArrayList<Meal>();
    }

    public void execute (){
        super.setmRowUrl(mDestinationUri.toString());
        DownloadJsonData downloadJsonData = new DownloadJsonData();
        Log.v(LOG_TAG, "Built URI = " + mDestinationUri.toString());
        downloadJsonData.execute(mDestinationUri.toString());

    }
    public boolean createAndroidUpdateUri(int id){
        final String BURGER_API_BASE_URL = "http://10.0.3.2/burger_ownercp/res/get_res";
        final String FORMAT_PRAM = "format";
        final String RES_ID_PARAM = "res_id";

        mDestinationUri = Uri.parse(BURGER_API_BASE_URL).buildUpon()
        .appendQueryParameter(RES_ID_PARAM, Integer.toString(id))
        .appendQueryParameter(FORMAT_PRAM, "json")
        .build();

        return mDestinationUri != null;


    }

    public List<Meal> getmMeals() {
        return mMeals;
    }

    public void processResult(){
        if(getmDownloadStatus() != DownloadStatus.OK){
            Log.e(LOG_TAG,"Error downloading row file");
            return;
        }
        final String RES_ITEMS = "meal";
        final String MEAL_NAME = "meal_name";
        final String MEAL_LOGO_URL = "meal_logo";
        try {
            JSONObject jsonData = new JSONObject(getmData());
            JSONArray itemsArray = jsonData.getJSONArray(RES_ITEMS);
            for (int i = 0; i<itemsArray.length();i++){
                JSONObject jsonMeal = itemsArray.getJSONObject(i);
                String name = jsonMeal.getString(MEAL_NAME);
                //String logoUrl = jsonMeal.getString(RES_LOGO_URL);
                Meal mealObject = new Meal(name,null,0,null,null);

                this.mMeals.add(mealObject);
            }
            for(Meal singleMeal : mMeals){
                Log.v(LOG_TAG, singleMeal.toString());
            }

        }catch (JSONException jsone){
            jsone.printStackTrace();
            Log.e(LOG_TAG, "Error processing Json data");
        }
    }

    public class DownloadJsonData extends GetRowData.DownloadRowData {

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
