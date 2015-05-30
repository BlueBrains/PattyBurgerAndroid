package com.bluebrains.pattyburger;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RestaurantTab.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RestaurantTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RestaurantTab extends Fragment {

    public static final String ARG_PARAM1 = "restaurant_id";
    public static final String ARG_PARAM2 = "restaurant_tabs_number";
    public static final String Item = "restaurant_tab_item";
    private int mRestaurantId;
    private int mRestaurantTabNum;
    private OnFragmentInteractionListener mListener;
    private com.bluebrains.common.view.SlidingTabLayout mSlidingTabLayout;

    /**
     * A {@link android.support.v4.view.ViewPager} which will be used in conjunction with the {@link com.bluebrains.common.view.SlidingTabLayout} above.
     */

    //private List<Meal> mMealList;
    private List<RecyclerView> mRecyclerViews;
    //private MealRecyclerViewAdapter mealRecyclerViewAdapters;
    private ViewPager mViewPager;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param restaurantId Identifer for restaurnt.
     * @param restaurantTabNum number of tabs in the menu.
     * @return A new instance of fragment RestaurantTab.
     */
    // TODO: Rename and change types and number of parameters
    public static RestaurantTab newInstance(int restaurantId, int restaurantTabNum) {
        RestaurantTab fragment = new RestaurantTab();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, restaurantId);
        args.putInt(ARG_PARAM2, restaurantTabNum);
        fragment.setArguments(args);
        return fragment;
    }

    public RestaurantTab() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRestaurantId = getArguments().getInt(ARG_PARAM1);
            mRestaurantTabNum = getArguments().getInt(ARG_PARAM2);
            mRecyclerViews = new ArrayList<RecyclerView>(mRestaurantTabNum);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.restaurant_tabs, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // BEGIN_INCLUDE (setup_viewpager)
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SamplePagerAdapter());
        // END_INCLUDE (setup_viewpager)

        // BEGIN_INCLUDE (setup_slidingtablayout)
        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        mSlidingTabLayout = (com.bluebrains.common.view.SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);
        // END_INCLUDE (setup_slidingtablayout)
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

    class SamplePagerAdapter extends PagerAdapter {

        /**
         * @return the number of pages to display
         */
        @Override
        public int getCount() {
            return mRestaurantTabNum;
        }

        /**
         * @return true if the value returned from {@link #instantiateItem(ViewGroup, int)} is the
         * same object as the {@link View} added to the {@link ViewPager}.
         */
        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        // BEGIN_INCLUDE (pageradapter_getpagetitle)
        /**
         * Return the title of the item at {@code position}. This is important as what this method
         * returns is what is displayed in the {@link com.bluebrains.common.view.SlidingTabLayout}.
         * <p>
         * Here we construct one using the position value, but for real application the title should
         * refer to the item's contents.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return "Item " + (position + 1);
        }
        // END_INCLUDE (pageradapter_getpagetitle)

        /**
         * Instantiate the {@link View} which should be displayed at {@code position}. Here we
         * inflate a layout from the apps resources and then change the text view to signify the position.
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // Inflate a new layout from our resources
            View view = getActivity().getLayoutInflater().inflate(R.layout.restaurant_tab_item,
                    container, false);
            // Add the newly created View to the ViewPager
            container.addView(view);
            mRecyclerViews.add(position, (RecyclerView) view.findViewById(R.id.recycler_view));
            mRecyclerViews.get(position).setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

            ProccessMeals proccessMeals = new ProccessMeals(mRestaurantId, position + 1);
            proccessMeals.execute();

            // Retrieve a TextView from the inflated View, and update it's text
            //TextView title = (TextView) view.findViewById(R.id.item_title);
            //title.setText(String.valueOf(position + 1));

            //com.example.android.common.logger.Log.i(LOG_TAG, "instantiateItem() [position: " + position + "]");

            // Return the View
            return view;
        }

        /**
         * Destroy the item from the {@link ViewPager}. In our case this is simply removing the
         * {@link View}.
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            //com.example.android.common.logger.Log.i(LOG_TAG, "destroyItem() [position: " + position + "]");
        }

    }
    public class ProccessMeals extends GetMealJsonData {
        private int poistion;
        public ProccessMeals(int id, int tabNum) {
            super(id, tabNum);
            poistion = tabNum-1;
        }

        public void execute(){
            ProccessData proccessData = new ProccessData();
            proccessData.execute();
        }

        public class ProccessData extends DownloadJsonData{
            protected void onPostExecute(String webData){
                super.onPostExecute(webData);
                MealRecyclerViewAdapter mealRecyclerViewAdapter = new MealRecyclerViewAdapter(getActivity(),getmMeals());
                mRecyclerViews.get(poistion).setAdapter(mealRecyclerViewAdapter);
            }

        }
    }
}
