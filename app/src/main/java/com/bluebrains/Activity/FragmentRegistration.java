package com.bluebrains.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bluebrains.app.AppConfig;
import com.bluebrains.app.Controller;
import com.bluebrains.helper.ParseUtils;
import com.bluebrains.helper.SQLiteHandler;
import com.bluebrains.helper.SessionManager;
import com.bluebrains.pattyburger.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentRegistration.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentRegistration#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentRegistration extends Fragment {

    private static final String TAG = FragmentRegistration.class.getSimpleName();
    private Context mContext;
    private EditText inputName;
    private EditText inputPhone;
    private Button btnRegister;
    private SessionManager pref;
    private ProgressDialog pDialog;
    private SQLiteHandler db;

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
     * @return A new instance of fragment FragmentRegisteration.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentRegistration newInstance(String param1, String param2) {
        FragmentRegistration fragment = new FragmentRegistration();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentRegistration() {
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
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pref = new SessionManager(mContext);
        db = new SQLiteHandler(getActivity().getApplicationContext());
        if (pref.isResgisterd()) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            FragmentOrderDetails fragment = new FragmentOrderDetails();
            transaction.replace(R.id.container_body, fragment);
            transaction.addToBackStack(FragmentOrderDetails.class.getSimpleName());
            transaction.commit();
            ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(R.string.title_order_details);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registeration, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inputName = (EditText) view.findViewById(R.id.user_name);
        inputPhone = (EditText) view.findViewById(R.id.user_phone);
        btnRegister = (Button) view.findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = inputName.getText().toString();
                String phone = inputPhone.getText().toString();

                // Check for empty data in the form
                if (!name.isEmpty() && !phone.isEmpty()) {
                    registerUser(name, phone);
                } else {
                    Toast.makeText(mContext,
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
        }
    private void registerUser(final String name, final String phone) {
        // Tag used to cancel the request
        final Controller mController =(Controller) mContext;
        String tag_string_req = "req_register";
        pDialog.setMessage("Registering ...");
        showDialog();
        Map<String, String> params = new HashMap<String, String>();
        params.put("tag", "register");
        params.put("name", name);
        params.put("phone", phone);
        JSONObject json = new JSONObject(params);
        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST, AppConfig.URL_REGISTER, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        hideDialog();

                        try {
                            JSONObject jObj = response;
                            boolean error = jObj.getBoolean("error");
                            if (!error) {
                                // User successfully stored in MySQL
                                // Now store the user in sqlite
                                String uid = jObj.getString("uid");
                                Integer id = jObj.getInt("id");
                                JSONObject user = jObj.getJSONObject("user");
                                String name = user.getString("name");
                                String phone = user.getString("phone");
                                String created_at = user.getString("created_at");

                                // Inserting row in users table
                                db.addUser(id, name, phone, uid, created_at);
                                ParseUtils.subscribeWithUniqueId(id);
                                pref.createLoginSession(id);
                                // Launch login activity
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                FragmentOrderDetails fragment = new FragmentOrderDetails();
                                transaction.replace(R.id.container_body, fragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                                ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(R.string.title_order_details);

                            } else {
                                // Error occurred in registration. Get the error
                                // message
                                String errorMsg = jObj.getString("error_msg");
                                Toast.makeText(mContext,
                                        errorMsg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + (error.getMessage()!= null ? error.getMessage():"Connection error"));
                Toast.makeText(mContext,
                        error.getMessage()!= null ? error.getMessage():"Connection error", Toast.LENGTH_LONG).show();
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
        Controller.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
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
