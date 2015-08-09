package com.bluebrains.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
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
import com.bluebrains.helper.SessionManager;
import com.bluebrains.model.Address;
import com.bluebrains.model.CartItem;
import com.bluebrains.model.ModelCart;
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
 * {@link FragmentSubmitOrder.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentSubmitOrder#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSubmitOrder extends Fragment {

    private static final String TAG = FragmentSubmitOrder.class.getSimpleName();
    private Context mContext;
    private ProgressDialog pDialog;
    private RadioGroup mRadioGroup;
    private TextView mTotal;
    private Button mCheckOut;
    private Controller mController;
    private Integer mOrderType = 1;
    private SessionManager mSession;
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
     * @return A new instance of fragment FragmentSubmitOrder.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSubmitOrder newInstance(String param1, String param2) {
        FragmentSubmitOrder fragment = new FragmentSubmitOrder();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentSubmitOrder() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mContext = getActivity().getApplicationContext();
        mSession = new SessionManager(mContext);
        mController = (Controller) mContext;
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_submit_order, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRadioGroup = (RadioGroup) view.findViewById(R.id.submit_order_radioGroup);
        mTotal = (TextView) view.findViewById(R.id.submit_oder_total_coast);
        mCheckOut = (Button) view.findViewById(R.id.check_out_btn);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.submit_order_radio1:
                        mOrderType = 1;
                        break;
                    case R.id.submit_order_radio2:
                        mOrderType = 2;
                        break;
                    case R.id.submit_order_radio3:
                        mOrderType = 3;
                        break;
                }
            }
        });

        mCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOrder();
            }
        });

        mTotal.setText("Total Coast: "+mController.getModelCart().getmTotalCoast());
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

    private void sendOrder(){
        ModelCart modelCart = mController.getModelCart();
        ArrayList<CartItem> cart = modelCart.getCart();
        pDialog.setMessage("Sending Request ...");
        showDialog();
        final String USER_ID = "user_id";
        final String RES_ID = "res_id";
        final String ORDER = "order";
        final String ITEM_ID = "res_meal";
        final String ITEM_SPEC = "spec";
        final String ITEM_COUNT = "count";
        Address addressInfo = modelCart.getmAddress();
        JSONArray meals = new JSONArray();
        JSONObject address = new JSONObject();
        JSONObject order = new JSONObject();
        try {
            for (CartItem item : cart) {
                JSONObject jsonItem = new JSONObject();
                jsonItem.put(ITEM_ID, item.getmID());
                JSONArray specs = new JSONArray();
                jsonItem.put(ITEM_SPEC, specs);
                jsonItem.put(ITEM_COUNT,item.getmCount());
                meals.put(jsonItem);
            }

            address.put("area",addressInfo.getmArea());
            address.put("location_type",addressInfo.getmPlaceType());
            address.put("block",addressInfo.getmBlock());
            address.put("house",addressInfo.getmHouseDetails());
            address.put("street",addressInfo.getmStreetDetails());
            address.put("more_info",addressInfo.getmInfo());
            order.put(USER_ID, mSession.getId())
                    .put(RES_ID, modelCart.getmResId())
                    .put(ORDER, meals).put("address",address)
                    .put("payment_method",mOrderType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Tag used to cancel the request
        Log.d(TAG, order.toString());

        JsonObjectRequest orderObj = new JsonObjectRequest(Request.Method.POST, AppConfig.ORDER_URL, order,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        Toast.makeText(mContext,"your order has just sent, please wait till you receive a cofirmation",Toast.LENGTH_LONG).show();
                        hideDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Log.d(TAG, error.toString());
                Toast.makeText(mContext,"error sending request!",Toast.LENGTH_LONG).show();
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
        Controller.getInstance().addToRequestQueue(orderObj, TAG);

    }
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
