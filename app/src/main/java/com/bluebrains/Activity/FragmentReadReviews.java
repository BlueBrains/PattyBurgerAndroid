package com.bluebrains.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bluebrains.adapter.ReviewRecyclerViewAdapter;
import com.bluebrains.app.AppConfig;
import com.bluebrains.app.Controller;
import com.bluebrains.helper.DividerItemDecoration;
import com.bluebrains.model.Review;
import com.bluebrains.pattyburger.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentReadReviews.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentReadReviews#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentReadReviews extends Fragment {

    private static String TAG = FragmentDrawer.class.getSimpleName();
    private Context mContext;
    private ProgressDialog pDialog;
    private ArrayList<Review> mReviews;
    private RecyclerView mRecyclerView;
    private ReviewRecyclerViewAdapter reviewRecyclerViewAdapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_RES_ID = "param1";

    // TODO: Rename and change types of parameters
    private Integer mResID;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param resID Parameter 1.
     * @return A new instance of fragment FragmentReadReviews.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentReadReviews newInstance(Integer resID) {
        FragmentReadReviews fragment = new FragmentReadReviews();
        Bundle args = new Bundle();
        args.putInt(ARG_RES_ID, resID);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentReadReviews() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mResID = getArguments().getInt(ARG_RES_ID);
        }
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        mContext = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_read_reviews, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mReviews = new ArrayList<>();
        getReviews();
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        reviewRecyclerViewAdapter =
                new ReviewRecyclerViewAdapter(getActivity(),mReviews);
        mRecyclerView.setAdapter(reviewRecyclerViewAdapter);
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
    void getReviews(){
        showDialog();
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, AppConfig.URL_REVIEW+"/id/"+mResID, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        hideDialog();
                        try{
                            JSONArray reviews = response.getJSONArray("reviews");
                            for(int i = 0; i<reviews.length();i++){
                                JSONObject review = reviews.getJSONObject(i);
                                String reviewText = review.getString("review");
                                String name = review.getString("name");
                                double rate = review.getDouble("rate");
                                mReviews.add(new Review(reviewText, name, rate));
                            }
                            reviewRecyclerViewAdapter.notifyDataSetChanged();
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(mContext,
                        error.getMessage()!= null ? error.getMessage():"Connection error", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        });
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

}
