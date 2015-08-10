package com.bluebrains.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bluebrains.adapter.BurgerRecyclerViewAdapter;
import com.bluebrains.app.Controller;
import com.bluebrains.helper.DividerItemDecoration;
import com.bluebrains.model.Restaurant;
import com.bluebrains.pattyburger.GetResJsonData;
import com.bluebrains.pattyburger.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentRestaurantSearch.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentRestaurantSearch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentRestaurantSearch extends Fragment {

    private RecyclerView mRecyclerView;
    private BurgerRecyclerViewAdapter burgerRecyclerViewAdapter;
    private ProgressDialog pDialog;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static ArrayList<Restaurant> SRestaurant=new ArrayList<Restaurant>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private int mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Restaurants.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentRestaurantSearch newInstance(String param1, String param2) {
        FragmentRestaurantSearch fragment = new FragmentRestaurantSearch();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentRestaurantSearch() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2= getArguments().getInt(ARG_PARAM2);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_restaurants, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view_res);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //ProccessRes proccessRes = new ProccessRes();

        //proccessRes.execute();
       // Toast.makeText(getActivity().getApplication(),mParam1.toString(),Toast.LENGTH_LONG).show();
        receive_data();

    }
    void setResataurant(JSONObject response)
    {
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
            //JSONObject jsonData = response.getJSONObject("res");
            JSONArray itemsArray = response.getJSONArray(RES_ITEMS);
            SRestaurant.clear();
           // Toast.makeText(getActivity().getApplication(),"length= "+itemsArray.length(),Toast.LENGTH_LONG).show();
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

                SRestaurant.add(resObject);
            }
            if(SRestaurant.size()>0)
            {
                burgerRecyclerViewAdapter =
                        new BurgerRecyclerViewAdapter(getActivity(),SRestaurant);
                mRecyclerView.setAdapter(burgerRecyclerViewAdapter);
            }

        }catch (JSONException jsone){
            jsone.printStackTrace();
            Log.e("Received", "Error processing Json data");
        }
    }
     void receive_data()
     {
         try {
             JSONObject json_data = new JSONObject();
             json_data.put("search", mParam1);
             String url;
             final String TAG_SUBMIT = "submit_cart";
             if(mParam2==0)
             {
                 url ="http://pattyburger.esy.es/restaurants/search";//"http://burger.remmaz.com/delivery/delivered";//"http://pattyburger.esy.es/restaurants/finish_task";// //"
             }
             else {
                 url = "http://pattyburger.esy.es/restaurants/search_address";
             }
             JsonObjectRequest taskObj = new JsonObjectRequest(Request.Method.POST, url, json_data,
                     new Response.Listener<JSONObject>() {
                         @Override
                         public void onResponse(JSONObject response) {
                             Log.d(TAG_SUBMIT, response.toString());

                                // Toast.makeText(getActivity().getApplication(), "enter here" + response.toString(3), Toast.LENGTH_LONG).show();
                                 setResataurant(response);

                         }
                     }, new Response.ErrorListener() {
                 @Override
                 public void onErrorResponse(VolleyError error) {
                     VolleyLog.d(TAG_SUBMIT, "Error: " + error.getMessage());
                     Toast.makeText(getActivity().getApplication(), "No Results ", Toast.LENGTH_LONG).show();
                     //Log.d(TAG_SUBMIT, order.toString());
                 }
             }) {
                 /**
                  * Passing some request headers
                  * */
                 @Override
                 public Map<String, String> getHeaders() throws
                         AuthFailureError {
                     HashMap<String, String> headers = new HashMap<String, String>();
                     headers.put("Content-Type", "application/json");
                     headers.put("charset", "utf-8");
                     return headers;
                 }
             };
             ((Controller)( getActivity().getApplication())).addToRequestQueue(taskObj, TAG_SUBMIT);
         }catch(JSONException e)
         {
             Log.e("Error in jason", "Push message json exception: " + e.getMessage());
             Toast.makeText(getActivity().getApplication(),e.getMessage(),Toast.LENGTH_LONG).show();
         }

     }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
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
                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Please wait...");
                pDialog.setCancelable(false);
                pDialog.show();

            }
            protected void onPostExecute(String webData){
                super.onPostExecute(webData);
                if (pDialog.isShowing())
                    pDialog.dismiss();
               // SRestaurant =getmRestaurants();
                burgerRecyclerViewAdapter =
                        new BurgerRecyclerViewAdapter(getActivity(),getmRestaurants());
                mRecyclerView.setAdapter(burgerRecyclerViewAdapter);

            }

        }
    }
}
