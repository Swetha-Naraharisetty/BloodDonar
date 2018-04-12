package com.example.swetha_pt1880.blooddonar.activity;


import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.example.swetha_pt1880.blooddonar.R;
import com.example.swetha_pt1880.blooddonar.adapter.ViewPagerAdapter;
import com.example.swetha_pt1880.blooddonar.database.Database;
import com.example.swetha_pt1880.blooddonar.database.Donar;
import com.example.swetha_pt1880.blooddonar.database.DonarDBMethods;
import com.example.swetha_pt1880.blooddonar.database.User;
import com.example.swetha_pt1880.blooddonar.database.UserDBMethods;
import com.example.swetha_pt1880.blooddonar.fragment.DonarListFragment;
import com.example.swetha_pt1880.blooddonar.fragment.UserListFragment;
import com.example.swetha_pt1880.blooddonar.session.SessionManagement;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TabbedActivity extends AppCompatActivity {
    String TAG = " TabbedActivity ";
    ArrayList<Donar> donars = new ArrayList<Donar>();
    ArrayList<User> user = new ArrayList<>();
    String credetial = " ";
    DonarDBMethods dMethod ;
    UserDBMethods uMethod;
    SessionManagement session;
    Database db = new Database(this);
    DatabaseReference dbRefer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Blood Donar");
        session = new SessionManagement(getApplicationContext());

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        HashMap<String, String> userDetail = session.getUserDetails();
        if(userDetail.get(SessionManagement.KEY_ID) != (null)) {

            HashMap<String, String> loginUser = session.getUserDetails();
            credetial = loginUser.get(SessionManagement.KEY_ID);
        }

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DonarListFragment(), "Donars");
        adapter.addFragment(new UserListFragment(), "Users");
        viewPager.setAdapter(adapter);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            NavUtils.navigateUpFromSameTask(this);
        return super.onOptionsItemSelected(item);
    }


    // Adapter for the viewpager using FragmentPagerAdapter


    //
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_tabbed, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.tab_beADonar) {
//            Intent beDonarIntent = new Intent(TabbedActivity.this, AddDonarActivity.class);
//            beDonarIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//            startActivity(beDonarIntent);
//            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//            return true;
//        }else if(id == R.id.tab_profile){
//            Intent profileIntent = new Intent(TabbedActivity.this, ProfileActivity.class);
//            Log.i(TAG +"checking profile id : ", credetial);
//            profileIntent.putExtra("id", credetial);
//            profileIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//            startActivity(profileIntent);
//            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//
//        }else if (id == R.id.tab_logout){
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setMessage("Do you want to logout ?");
//            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//
//                    donars.clear();
//                    donars = dMethod.getDonarsList();
//
//                    for (Donar donar : donars){
//                        Log.i(TAG + "donar name val ", donar.getdName() + "");
//                        dbRefer.child("Donars").child(donar.getdContact()).setValue(donar);
//                    }
//                    user = uMethod.getUserList();
//                    Log.i(TAG + " user vals ", user+ "");
//                    for (User u: user){
//                        Log.i(TAG + "donar val ", u.getDonar() + u.getuName());
//                        dbRefer.child("Users").child(u.getUserId()).setValue(u);
//                    }
//                    db.delete();
//                    session.logoutUser();
//                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                    finish();
//                }
//            });
//            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//
//
//                }
//            });
//            // Create the AlertDialog object and return it
//            android.app.AlertDialog dialog = builder.create();
//            dialog.show();
//
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
//
//    /**
//     * A placeholder fragment containing a simple view.
//     */
//    public static class PlaceholderFragment extends Fragment {
//        /**
//         * The fragment argument representing the section number for this
//         * fragment.
//         */
//        private static final String ARG_SECTION_NUMBER = "section_number";
//
//        public PlaceholderFragment() {
//        }
//
//        /**
//         * Returns a new instance of this fragment for the given section
//         * number.
//         */
//        public static PlaceholderFragment newInstance(int sectionNumber) {
//            PlaceholderFragment fragment = new PlaceholderFragment();
//            Bundle args = new Bundle();
//            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//            fragment.setArguments(args);
//            return fragment;
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_tabbed, container, false);
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
//            return rootView;
//        }
//    }
//
//    /**
//     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
//     * one of the sections/tabs/pages.
//     */
//    public class SectionsPagerAdapter extends FragmentPagerAdapter {
//
//        public SectionsPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            // getItem is called to instantiate the fragment for the given page.
//            // Return a PlaceholderFragment (defined as a static inner class below).
//            return PlaceholderFragment.newInstance(position + 1);
//        }
//
//        @Override
//        public int getCount() {
//            // Show 3 total pages.
//            return 3;
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            switch (position) {
//                case 0:
//                    return "SECTION 1";
//                case 1:
//                    return "SECTION 2";
//                case 2:
//                    return "SECTION 3";
//            }
//            return null;
//        }
//    }

