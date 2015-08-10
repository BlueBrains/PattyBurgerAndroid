package com.bluebrains.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bluebrains.app.AppConfig;
import com.bluebrains.app.Controller;
import com.bluebrains.model.Meal;
import com.bluebrains.model.ModelCart;
import com.bluebrains.pattyburger.R;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentMeal.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentMeal#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMeal extends Fragment {

    private static final String TAG = FragmentMeal.class.getSimpleName();
    private TextView mMealName;
    private TextView mMealDescription;
    private TextView mMealPrice;
    private ImageView mMealImage;
    private Button mAddToCart;
    private Context mContext;
    private ProgressDialog pDialog;
    private Meal mCurrentMeal;
    ArrayList<String> specs;
    ArrayList<Integer> checkBoxesMap;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(FragmentRestaurantTab.Item,mCurrentMeal);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Meal.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMeal newInstance(String param1, String param2) {
        FragmentMeal fragment = new FragmentMeal();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentMeal() {
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
        if(savedInstanceState != null){
            mCurrentMeal = (Meal) savedInstanceState.getParcelable(FragmentRestaurantTab.Item);//getParcelableExtra(RestaurantTab.Item);
        }else{
            mCurrentMeal = (Meal) getArguments().getParcelable(FragmentRestaurantTab.Item);//getParcelableExtra(RestaurantTab.Item);
        }
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meal, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Controller mController =(Controller) mContext;
        mMealName = (TextView)view.findViewById(R.id.mealName);
        mMealDescription = (TextView)view.findViewById(R.id.mealDescription);
        mMealPrice = (TextView)view.findViewById(R.id.mealPrice);
        mMealImage = (ImageView)view.findViewById(R.id.mealImage);
        mAddToCart = (Button)view.findViewById(R.id.cartButton);

        mMealName.setText(mCurrentMeal.getmName());
        mMealDescription.setText(mCurrentMeal.getmDescription());
        mMealPrice.setText("Price\r\n"+mCurrentMeal.getmPrice()+" s.p");
        Picasso.with(mContext).load(mCurrentMeal.getmImage())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(mMealImage);

        LinearLayout checkboxes = (LinearLayout)view.findViewById(R.id.checkboxes);
        specs = mCurrentMeal.getmSpecs();
        specs.add("ملح زيادة");
        specs.add("مع توابل ");
        checkBoxesMap = new ArrayList<>();
        int Array_Count = specs.size();
        for (int i = 0; i < Array_Count; i++)
        {
            TableRow row =new TableRow(getActivity());
            row.setId(i);
            row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            CheckBox checkBox = new CheckBox(getActivity());
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)  checkBoxesMap.add(buttonView.getId());
                }
            });
            checkBox.setId(i);
            checkBox.setText(specs.get(i));
            row.addView(checkBox);
            checkboxes.addView(row,i);
        }

        mAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"Not Supported yet",Toast.LENGTH_LONG).show();
                ModelCart cart = mController.getModelCart();
                if(cart.getCart().size()==0){
                    cart.setmResId(mCurrentMeal.getmResID());
                }
                if(cart.checkItemInCart(mCurrentMeal)){
                    Toast.makeText(mContext, "You have already added this meal!", Toast.LENGTH_LONG).show();
                }else {
                    if(cart.getmResId()!=mCurrentMeal.getmResID()){
                     cart.getCart().clear();
                     cart.setmTotalCoast(0);
                    }
                    cart.setItem(mCurrentMeal).setmSpecState(checkBoxesMap);
                    cart.setmTotalCoast(cart.getmTotalCoast()+mCurrentMeal.getmPrice());
                }
            }
        });
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

    void getSpecs(){
        showDialog();
        JsonObjectRequest Req = new JsonObjectRequest(Request.Method.GET, AppConfig.URL_SPECS+"/id/1", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        hideDialog();
//                        try {
//                            JSONArray specs = response.getJSONArray("specs");
//                            for(int i = 0; i<specs.length(); i++){
//                                JSONObject spec = specs.getJSONObject(i);
//                                mCurrentMeal.getmSpecs().add(spec.getString("spec_name"));
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
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
        Controller.getInstance().addToRequestQueue(Req, TAG);
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