package com.bluebrains.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentRestaurantDetails.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentRestaurantDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentRestaurantDetails extends Fragment {

    private static final String TAG = FragmentRestaurantDetails.class.getSimpleName();
    private Restaurant mRes;
    private Button mMenu;
    private TextView mRestaurantName;
    private TextView mLocation;
    private TextView mPhone;
    private TextView mDescription;
    private TextView mType;
    private ImageView mLogo;
    private ImageView mIsDeliverable;
    private ImageView mReviews;
    private RatingBar mRating;
    private RatingBar mPriceRange;
    private ProgressDialog pDialog;
    private ArrayList<String> mTabTitles = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_RES = "param1";


    private OnFragmentInteractionListener mListener;
    private Context mContext;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment FraqmentRestaurantDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentRestaurantDetails newInstance(String param1) {
        FragmentRestaurantDetails fragment = new FragmentRestaurantDetails();
        Bundle args = new Bundle();
        args.putString(ARG_RES, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMenu = (Button) view.findViewById(R.id.show_menu);
        mRestaurantName = (TextView) view.findViewById(R.id.res_name);
        mLocation = (TextView) view.findViewById(R.id.location_text);
        mPhone = (TextView) view.findViewById(R.id.phone_text);
        mDescription = (TextView) view.findViewById(R.id.description_text);
        mType = (TextView) view.findViewById(R.id.type_text);
        mLogo = (ImageView) view.findViewById(R.id.res_logo);
        mIsDeliverable = (ImageView) view.findViewById(R.id.deliverable_image);
        mReviews = (ImageView) view.findViewById(R.id.read_reviews_arrow);
        mRating = (RatingBar) view.findViewById(R.id.body_ratingBar);
        mPriceRange = (RatingBar) view.findViewById(R.id.footer_priceRange);


        mMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putInt(FragmentRestaurantTab.ARG_RES_ID, mRes.getmID());
                args.putStringArrayList(FragmentRestaurantTab.ARG_TABS, mTabTitles);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                FragmentRestaurantTab fragment = new FragmentRestaurantTab();
                fragment.setArguments(args);
                transaction.replace(R.id.container_body, fragment);
                transaction.addToBackStack(FragmentRestaurantTab.class.getSimpleName());
                transaction.commit();
                getActivity().setTitle(R.string.title_restaurant_menu);
            }
        });

        mRestaurantName.setText(mRes.getmName());
        mLocation.setText(mRes.getmLocation());
        mPhone.setText(mRes.getmPhone());
        mDescription.setText(mRes.getmDescription());
        mType.setText(mRes.getmType());
        mPriceRange.setRating((float) mRes.getmPriceRange());
        mRating.setRating((float) mRes.getmRating());
        Picasso.with(mContext).load(mRes.getmImage())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(mLogo);
        if(mRes.ismDeliverable())
            mIsDeliverable.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_accept));
        else
            mIsDeliverable.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_cancel));
        mReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                FragmentReadReviews fragment = FragmentReadReviews.newInstance(mRes.getmID());
                transaction.replace(R.id.container_body, fragment);
                transaction.addToBackStack(FragmentRestaurants.class.getSimpleName());
                transaction.commit();
                ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(R.string.title_read_reviews);
            }
        });
    }

    public FragmentRestaurantDetails() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
        if (getArguments() != null) {
            mRes = getArguments().getParcelable(ARG_RES);
        }
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        fetchTabs();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_restaurant_details, container, false);
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
    private void fetchTabs() {
        // Showing progress dialog before making request

        pDialog.setMessage("please wait..");

        showpDialog();

        // Making json object request
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                AppConfig.RES_URL+mRes.getmID(), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    JSONArray tabs = response
                            .getJSONArray("tab_names");

                    // looping through all product nodes and storing
                    // them in array list
                    for (int i = 0; i < tabs.length(); i++) {

                        JSONObject tab = (JSONObject) tabs.get(i);
                        mTabTitles.add(tab.getString("restaurantMeals_cat_name"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

                // hiding the progress dialog
                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                hidepDialog();
            }
        });

        // Adding request to request queue
        Controller.getInstance().addToRequestQueue(jsonObjReq);
    }
    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
