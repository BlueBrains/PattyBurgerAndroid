package com.bluebrains.Activity;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bluebrains.app.Controller;
import com.bluebrains.model.Meal;
import com.bluebrains.pattyburger.R;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentMeal.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentMeal#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMeal extends Fragment {

    private TextView mMealName;
    private TextView mMealDescription;
    private TextView mMealPrice;
    private ImageView mMealImage;
    private Button mAddToCart;
    private Context mContext;

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
        mContext = getActivity().getApplicationContext();
        final Controller mController =(Controller) mContext;

        mMealName = (TextView)view.findViewById(R.id.mealName);
        mMealDescription = (TextView)view.findViewById(R.id.mealDescription);
        mMealPrice = (TextView)view.findViewById(R.id.mealPrice);
        mMealImage = (ImageView)view.findViewById(R.id.mealImage);
        mAddToCart = (Button)view.findViewById(R.id.cartButton);

        final Meal currentMeal = (Meal) getArguments().getParcelable(FragmentRestaurantTab.Item);//getParcelableExtra(RestaurantTab.Item);
        mMealName.setText(currentMeal.getmName());
        mMealDescription.setText(currentMeal.getmDescription());
        mMealPrice.setText("Price\r\n"+currentMeal.getmPrice()+" s.p");
        Picasso.with(mContext).load(currentMeal.getmImage())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(mMealImage);

        mAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"Not Supported yet",Toast.LENGTH_LONG).show();
                if(mController.getModelCart().checkItemInCart(currentMeal)){
                    Toast.makeText(mContext, "You have already added this meal!", Toast.LENGTH_LONG).show();
                }else {
                    mController.getModelCart().setItem(currentMeal);
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
