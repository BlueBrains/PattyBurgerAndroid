package com.bluebrains.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bluebrains.pattyburger.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentMap.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentMap#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMap extends Fragment implements GoogleMap.OnMarkerClickListener
{
    Marker marker;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toast.makeText(getActivity().getApplicationContext(),lat+" , "+lng,Toast.LENGTH_LONG).show();

        LatLng currentLatLng;

        SupportMapFragment fm = (SupportMapFragment)getChildFragmentManager()
                .findFragmentById(R.id.map2);
        /*
        if(fm!=null)
            Toast.makeText(getActivity().getApplicationContext(),"fm not null",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getActivity().getApplicationContext(),"fm is null",Toast.LENGTH_LONG).show();
*/
        currentLatLng=new LatLng(lat,lng);
        googleMap = fm.getMap();
        /**
        if(googleMap!=null)
        {
            Toast.makeText(getActivity().getApplicationContext(),"map not null",Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(getActivity().getApplicationContext(),"map is null",Toast.LENGTH_LONG).show();
**/
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        //currentLatLng=new LatLng(33.509032, 36.286178);
        marker = googleMap.addMarker(new MarkerOptions()
                .position(currentLatLng)
                .title("Patty Burger"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,
                11));
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String LAT = "param1";
    public static final String LNG = "param2";

    // TODO: Rename and change types of parameters
    private double lat;
    private double lng;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentMap.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMap newInstance(String param1, String param2) {
        FragmentMap fragment = new FragmentMap();
        Bundle args = new Bundle();
        args.putString(LAT, param1);
        args.putString(LNG, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentMap() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            lat = getArguments().getDouble(LAT);
            lng = getArguments().getDouble(LNG);
        }
    }
    GoogleMap googleMap;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_map, container, false);
    }
    @Override
    public boolean onMarkerClick(final Marker marker) {

        if (marker.equals(marker))
        {
            Toast.makeText(getActivity().getApplicationContext(),marker.getTitle(),Toast.LENGTH_LONG).show();
        }
        return true;
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
