package com.bluebrains.pattyburger;

import android.net.Uri;
import android.util.Log;

import com.bluebrains.app.AppConfig;
import com.bluebrains.model.Meal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Molham on 3/31/2015.
 */
public class GetMealJsonData extends GetRowData{
    public GetMealJsonData(){
        super(null,GET);

    }

    private String LOG_TAG = GetMealJsonData.class.getName();
    private List<Meal> mMeals;
    private Uri mDestinationUri;
    private int mResID;
    public GetMealJsonData(int id, int tabNum){
        super(null,GET);
        createAndroidUpdateUri(id,tabNum);
        mMeals = new ArrayList<Meal>();
    }

    public void execute (){
        super.setmRowUrl(mDestinationUri.toString());
        DownloadJsonData downloadJsonData = new DownloadJsonData();
        Log.v(LOG_TAG, "Built URI = " + mDestinationUri.toString());
        downloadJsonData.execute(mDestinationUri.toString());

    }
    public boolean createAndroidUpdateUri(int id, int tabNum){
        final String RES_ID_PARAM = "id";
        final String RES_TAB_PARAM = "tab";
        mResID = id;

        mDestinationUri = Uri.parse(AppConfig.BASE_URL+"restaurants/res").buildUpon()
        .appendQueryParameter(RES_ID_PARAM, Integer.toString(id))
        .appendQueryParameter(RES_TAB_PARAM, Integer.toString(tabNum))
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
        final String RES_MEALS = "meals";
        final String MEAL_ID = "id";
        final String MEAL_NAME = "name";
        final String MEAL_PRICE = "price";
        final String MEAL_TIME = "preparing_time";
        final String MEAL_DESCRIPTION = "details";
        final String MEAL_LOGO_URL = "image";
        final String MEAL_RATE = "rating";
        try {
            JSONObject jsonData = new JSONObject(getmData());
            JSONArray itemsArray = jsonData.getJSONArray(RES_MEALS);
            for (int i = 0; i<itemsArray.length();i++){
                JSONObject jsonMeal = itemsArray.getJSONObject(i);
                String name = jsonMeal.getString(MEAL_NAME);
                String description = jsonMeal.getString(MEAL_DESCRIPTION);
                double price = jsonMeal.getDouble(MEAL_PRICE);
                double prepare = jsonMeal.getDouble(MEAL_TIME);
                double ratnig = jsonMeal.getDouble(MEAL_RATE);
                String logoUrl = jsonMeal.getString(MEAL_LOGO_URL);
                Integer id = jsonMeal.getInt(MEAL_ID);
//                JSONArray specs = jsonMeal.getJSONArray("specs");
                Meal mealObject = new Meal(id,name,null,price,prepare,description,logoUrl,ratnig);
                mealObject.setmResID(mResID);
//                for(int j = 0; j<specs.length(); j++){
//                    JSONObject spec = specs.getJSONObject(i);
//                    mealObject.getmSpecs().add(spec.getString("spec_name"));
//                }
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
