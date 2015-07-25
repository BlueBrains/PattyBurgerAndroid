package com.bluebrains.Activity;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bluebrains.adapter.CartRecyclerViewAdapter;
import com.bluebrains.app.AppConfig;
import com.bluebrains.app.Controller;
import com.bluebrains.model.CartItem;
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
 * {@link FragmentCart.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentCart#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCart extends Fragment {

    private RecyclerView mRecyclerView;
    private Button mSubmit;
    private TextView mTotalCoast;
    private Context mContext;
    private CartRecyclerViewAdapter mCartRecyclerViewAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentCart.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentCart newInstance(String param1, String param2) {
        FragmentCart fragment = new FragmentCart();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentCart() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity().getApplicationContext();
        final Controller mController =(Controller) mContext;

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        mSubmit = (Button) view.findViewById(R.id.cart_order_button);
        mTotalCoast = (TextView) view.findViewById(R.id.total_order_coast);
        mTotalCoast.append(" "+mController.getModelCart().getmTotalCoast()+"");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCartRecyclerViewAdapter = new CartRecyclerViewAdapter(getActivity(),mController.getModelCart().getCart());
        mRecyclerView.setAdapter(mCartRecyclerViewAdapter);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<CartItem> cart = mController.getModelCart().getCart();
                final String USER_ID = "user_id";
                final String ORDER = "order";
                final String ITEM_ID = "item_id";
                final String ITEM_SPEC = "item_spec";
                final String ITEM_COUNT = "item_count";
                JSONArray meals = new JSONArray();
                JSONObject order = new JSONObject();
                try {
                    for (CartItem item : cart) {
                        JSONObject jsonItem = new JSONObject();
                        jsonItem.put(ITEM_ID, 1);
                        jsonItem.put(ITEM_SPEC, "spec");
                        jsonItem.put(ITEM_COUNT, 12);

                        meals.put(jsonItem);
                    }
                    order.put(USER_ID, 1).put(ORDER, meals);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Tag used to cancel the request
                final String TAG_SUBMIT = "submit_cart";
                Log.d(TAG_SUBMIT, order.toString());

                JsonObjectRequest orderObj = new JsonObjectRequest(Request.Method.POST, AppConfig.ORDER_URL, order,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG_SUBMIT, response.toString());
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG_SUBMIT, "Error: " + error.getMessage());
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
                // Adding request to request queue
                mController.getInstance().addToRequestQueue(orderObj, TAG_SUBMIT);
            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
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

}
