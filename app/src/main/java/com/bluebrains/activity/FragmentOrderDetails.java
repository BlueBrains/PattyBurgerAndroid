package com.bluebrains.activity;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bluebrains.app.Controller;
import com.bluebrains.model.Address;
import com.bluebrains.pattyburger.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentOrderDetails.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentOrderDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentOrderDetails extends Fragment {

    private static final String TAG = FragmentOrderDetails.class.getSimpleName();
    private Context mContext;
    private Spinner mArea;
    private Spinner mLocationType;
    private EditText mStreet;
    private EditText mBlock;
    private EditText mHouse;
    private EditText mInfo;
    private Button mNext;
    private Controller mController;

    String  area, locationType, street, house, block, info;

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
     * @return A new instance of fragment FragmentOrderDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentOrderDetails newInstance(String param1, String param2) {
        FragmentOrderDetails fragment = new FragmentOrderDetails();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentOrderDetails() {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_details, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mArea = (Spinner) view.findViewById(R.id.area_spinner);
        mLocationType = (Spinner) view.findViewById(R.id.place_type_spinner);
        mStreet = (EditText) view.findViewById(R.id.street_edit_text);
        mBlock = (EditText) view.findViewById(R.id.block_edit_text);
        mHouse = (EditText) view.findViewById(R.id.house_edit_text);
        mInfo = (EditText) view.findViewById(R.id.adress_more_info_text);
        mNext = (Button) view.findViewById(R.id.adress_next_btn);
        mController = (Controller)mContext;

        ArrayAdapter<CharSequence> areasAdapter = ArrayAdapter.createFromResource(mContext,
                R.array.areas_array, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        areasAdapter.setDropDownViewResource(R.layout.spinner_drop_down_item);
        // Apply the adapter to the spinner
        mArea.setAdapter(areasAdapter);

        ArrayAdapter<CharSequence> typesAdapter = ArrayAdapter.createFromResource(mContext,
                R.array.place_type_array, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        typesAdapter.setDropDownViewResource(R.layout.spinner_drop_down_item);
        // Apply the adapter to the spinner
        mLocationType.setAdapter(typesAdapter);

        mArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                area = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mLocationType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                locationType = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                street = mStreet.getText().toString();
                block = mBlock.getText().toString();
                house = mHouse.getText().toString();
                info = mInfo.getText().toString();
                if(!area.isEmpty()&&!block.isEmpty()){
                    Address address = new Address(area, locationType, street, house, block, info);
                    mController.getModelCart().setmAddress(address);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    FragmentSubmitOrder fragment = new FragmentSubmitOrder(); //TODO create fragment order detials
                    transaction.replace(R.id.container_body, fragment);
                    transaction.addToBackStack(FragmentSubmitOrder.class.getSimpleName());
                    transaction.commit();
                    ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(R.string.title_submit_order);
                }else{
                    Toast.makeText(mContext,
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
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
