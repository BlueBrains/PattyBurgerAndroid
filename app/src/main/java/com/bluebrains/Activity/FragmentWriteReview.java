package com.bluebrains.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bluebrains.app.AppConfig;
import com.bluebrains.app.Controller;
import com.bluebrains.model.Restaurant;
import com.bluebrains.pattyburger.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentWriteReview.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentWriteReview#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentWriteReview extends Fragment {

    private static String TAG = FragmentDrawer.class.getSimpleName();
    private Context mContext;
    private ProgressDialog pDialog;

    private static final String JSON_MESSAGE = "json_message";
    private JSONObject mReceviedData;

    private String mJsonMessage;

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
    final String RES_TYPE="category_name";
    final String RES_PHONE="phone_nbr_1";
    final String RES_ORDER="order_id";


    private Restaurant mRestaurant;

    private ImageView thumbnail;
    private TextView name;
    private  TextView description;
    private RatingBar rating;
    private double lat;
    private double lng;
    private ImageView MapButton;

    private RatingBar mNewRate;
    private EditText mReview;
    private Button mSend;

    private Integer mOrderId;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param jsonMessage Parameter 1.
     * @return A new instance of fragment FragmentWriteReview.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentWriteReview newInstance(String jsonMessage) {
        FragmentWriteReview fragment = new FragmentWriteReview();
        Bundle args = new Bundle();
        args.putString(JSON_MESSAGE, jsonMessage);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentWriteReview() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mJsonMessage = getArguments().getString(JSON_MESSAGE);
        }
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        parseMessage();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_write_review, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity().getApplicationContext();

        thumbnail = (ImageView)view.findViewById(R.id.thumbnail);
        name = (TextView)view.findViewById(R.id.name);
        description = (TextView)view.findViewById(R.id.description);
        rating=(RatingBar)view.findViewById(R.id.body_ratingBar);
        MapButton=(ImageView)view.findViewById(R.id.map);
        mNewRate = (RatingBar)view.findViewById(R.id.new_rate);
//        LayerDrawable stars = (LayerDrawable) mNewRate.getProgressDrawable().getgetDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);;
//        stars.
        mReview = (EditText)view.findViewById(R.id.written_review);
        mSend = (Button) view.findViewById(R.id.submit_review_btn);

        Picasso.with(mContext).load(mRestaurant.getmImage())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(thumbnail);
        name.setText(mRestaurant.getmName());
        description.setText(mRestaurant.getmDescription());
        rating.setRating((float)mRestaurant.getmRating());

        MapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                lat=mRestaurant.getmLat();
                lng=mRestaurant.getmLng();
                args.putDouble(FragmentMap.LAT, lat);
                args.putDouble(FragmentMap.LNG, lng);
                if (mContext instanceof FragmentActivity) {
                    // We can get the fragment manager
                    FragmentActivity activity = (FragmentActivity)(mContext);
                    FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                    FragmentMap fragment = new FragmentMap();
                    fragment.setArguments(args);
                    transaction.replace(R.id.container_body, fragment);
                    transaction.addToBackStack(FragmentMap.class.getSimpleName());
                    transaction.commit();
                    ((ActionBarActivity)mContext).getSupportActionBar().setTitle(R.string.title_restaurant_location);
                }
            }
        });
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String review = mReview.getText().toString();
                double rating = mNewRate.getRating();

                if(!review.isEmpty()){
                    sendReview(review, rating);
                }
            }
        });
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
    void parseMessage(){
        try{
            mReceviedData = new JSONObject(mJsonMessage);
            JSONObject res = mReceviedData.getJSONObject("res");
            mOrderId = mReceviedData.getInt(RES_ORDER);
            String name = res.getString(RES_NAME);
            String description = res.getString(RES_DESCRIPTION);
            String logoUrl = res.getString(RES_LOGO);
            double lat=Double.parseDouble(res.getString(RES_LAT));
            double lng=Double.parseDouble(res.getString(RES_LNG));
            double rating=Double.parseDouble(res.getString(RES_RATE));
            int id = Integer.parseInt(res.getString(RES_ID));
            mRestaurant =  new Restaurant(name,null,null,logoUrl,description,id,rating,lat,lng,null,false,-1);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    void sendReview(String review, double rating){
        JSONObject json = new JSONObject();
        try {
            json.put("review",review).put("rate",rating).put("order_id",mOrderId);
            Log.d(TAG, json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConfig.URL_REVIEW, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        hideDialog();
                        Toast.makeText(mContext,
                                "sent it successfully , thanks for sharing your opinion :D !",
                                Toast.LENGTH_LONG).show();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        FragmentRestaurants fragment = new FragmentRestaurants();
                        transaction.replace(R.id.container_body, fragment);
                        transaction.addToBackStack(FragmentRestaurants.class.getSimpleName());
                        transaction.commit();
                        ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(R.string.title_order_details);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(mContext,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
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
        // Adding request to request queue
        Controller.getInstance().addToRequestQueue(req, TAG);
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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
