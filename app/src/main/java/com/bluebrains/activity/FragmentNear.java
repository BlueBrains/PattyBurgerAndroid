package com.bluebrains.activity;


import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bluebrains.adapter.GPSTracker;
import com.bluebrains.model.Restaurant;
import com.bluebrains.pattyburger.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentNear.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentNear#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentNear extends Fragment  implements GoogleMap.OnMarkerClickListener{
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
     * @return A new instance of fragment FragmentNear.
     */
    Marker marker;
    GPSTracker gps;
    GoogleMap googleMap;
    private List<Restaurant> res;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        res=FragmentRestaurants.SRestaurant;
        gps=new GPSTracker(this.getActivity());
        Toast.makeText(getActivity().getApplication(),"welcome res = "+res.size(),Toast.LENGTH_LONG).show();
        if (gps.canGetLocation()) {

            Log.d("Your Location", "latitude:" + gps.getLatitude() + ", longitude: " + gps.getLongitude());
            LatLng currentLatLng;
            currentLatLng=new LatLng(gps.getLatitude(),gps.getLongitude());
            SupportMapFragment fm = (SupportMapFragment)getChildFragmentManager()
                    .findFragmentById(R.id.map3);
            googleMap = fm.getMap();
            marker = googleMap.addMarker(new MarkerOptions()
                    .position(currentLatLng)
                    .title("Your Location"));
            for(int i=0;i<res.size();i++)
            {
                LatLng temp=new LatLng(res.get(i).getmLat(),res.get(i).getmLng());
                double result=CalculationByDistance(currentLatLng,temp);
                Toast.makeText(getActivity().getApplication(),result+"",Toast.LENGTH_LONG).show();
                if(result<1000)
                {
                    marker = googleMap.addMarker(new MarkerOptions()
                            .position(currentLatLng)
                            .title(res.get(i).getmName()));
                }
            }

        }
    }
    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }
    // TODO: Rename and change types and number of parameters
    public static FragmentNear newInstance(String param1, String param2) {
        FragmentNear fragment = new FragmentNear();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentNear() {
        // Required empty public constructor
    }
    @Override
    public boolean onMarkerClick(final Marker marker) {

        if (marker.equals(marker))
        {
            Toast.makeText(getActivity().getApplicationContext(), marker.getTitle(), Toast.LENGTH_LONG).show();
        }
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //lat = getArguments().getDouble(LAT);
            //lng = getArguments().getDouble(LNG);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_near, container, false);
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
