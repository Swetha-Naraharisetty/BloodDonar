package com.example.swetha_pt1880.blooddonar.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcel;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swetha_pt1880.blooddonar.R;

import com.example.swetha_pt1880.blooddonar.adapter.SwipeUserAdapter;
import com.example.swetha_pt1880.blooddonar.database.Database;
import com.example.swetha_pt1880.blooddonar.database.User;
import com.example.swetha_pt1880.blooddonar.database.UserDBMethods;
import com.example.swetha_pt1880.blooddonar.fragment.ProfileFragment;
import com.example.swetha_pt1880.blooddonar.session.SessionManagement;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class UserScreenSlideActivity extends AppCompatActivity {


    private ViewPager mPager;
    Menu mMenu;
    String TAG = "Swipe User ";
    UserDBMethods uMethod = new UserDBMethods(this);
    String uId, loggedUser;
     static ArrayList<User> users = new ArrayList<>();


    Database db = new Database(this);
    User user = new User();
    DatabaseReference databaseReference;
    SessionManagement sessionManagement;
    Toolbar toolbar;



    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_screen_slide);
         toolbar = (Toolbar) findViewById(R.id.toolbar);
        mPager = (ViewPager) findViewById(R.id.pager);
        sessionManagement = new SessionManagement(getApplicationContext());
        HashMap<String, String> sessionUser = sessionManagement.getUserDetails();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");


        loggedUser = sessionUser.get(SessionManagement.KEY_ID);

        
        User newUser = new User();
        users = uMethod.getUserList();
        int position = getIntent().getIntExtra("position", 1);
        String id = getIntent().getStringExtra("id");

        Parcel parcel = Parcel.obtain();
        parcel.writeParcelable(View.BaseSavedState.EMPTY_STATE, 0);

        parcel.writeInt(position);
        parcel.writeParcelable(null, 0);

        parcel.setDataPosition(0);
        ViewPager.SavedState savedState = ViewPager.SavedState.CREATOR.createFromParcel(parcel);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPager.onRestoreInstanceState(savedState);

        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                toolbar.setTitle(users.get(position).getUserId());
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        user = uMethod.getProfile(id);
        //setUserData(user);

        for(User u : users){
            if(u.getUserId().equals("admin")) {
                newUser = u;
                break;
            }
        }
       
        users.remove(newUser);
//        Collections.sort(users, new Comparator<User>() {
//            public int compare(User v1, User v2) {
//                //Log.i(TAG + "sort", v1.getUserId()+ v2.getUserId());
//                return v1.getuName().toLowerCase().compareTo(v2.getuName().toLowerCase());
//            }
//        });

        Log.i(TAG + " pos ####", position+ "adapter is called");

        mPager.setAdapter(new SwipeUserAdapter(this,users, position ));
    }
    


    @Override
    public void onBackPressed() {

            super.onBackPressed();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG + "in menu", "create ");
        toolbar.setTitle(users.get(mPager.getCurrentItem()).getUserId());
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        mMenu = menu;
        if(loggedUser.equals(uId) ) {
            menu.findItem(R.id.delete).setVisible(false);
            toolbar.setTitle("User Profile");

        }if(!loggedUser.equals("admin"))
            menu.findItem(R.id.edit).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG + "in menu", "options ");
        toolbar.setTitle(users.get(mPager.getCurrentItem()).getUserId());
        if(item.getItemId() == R.id.delete){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to delete " + users.get(mPager.getCurrentItem()).getuName() + " ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    try {
                        uMethod.delUser(users.get(mPager.getCurrentItem()).getUserId());
                        Query query = databaseReference.child(users.get(mPager.getCurrentItem()).getUserId());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                    appleSnapshot.getRef().removeValue();
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e(TAG, "onCancelled", databaseError.toException());
                            }
                        });

                    } catch (SQLException e) {
                        e.printStackTrace();
                        Toast.makeText(UserScreenSlideActivity.this, "Couldn't delete !!", Toast.LENGTH_LONG).show();
                    }
                    finally {
                        uMethod.close();
                    }
                    Toast.makeText(UserScreenSlideActivity.this, "Deleted User : " + users.get(mPager.getCurrentItem()).getuName() , Toast.LENGTH_LONG).show();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                    //no action
                    Toast.makeText(UserScreenSlideActivity.this, "no action", Toast.LENGTH_LONG).show();
                }
            });
            // Create the AlertDialog object and return it
            android.app.AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if(item.getItemId() == R.id.edit) {
            Log.i(TAG + "edit profile", "im editting" + user.getUserId());
            if (loggedUser.equals("admin")) {
                Intent in = new Intent(this, AddUserActivity.class);

                Bundle args = new Bundle();
                args.putSerializable("chatobj", (Serializable) users.get(mPager.getCurrentItem()));
                in.putExtra("DATA", args);
                startActivityForResult(in, 2);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }

        else if (item.getItemId() == android.R.id.home )
            onBackPressed();
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2){

            uId = getIntent().getStringExtra("id");

        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(loggedUser.equals(uId)) {
            try {
                uMethod.updateUser(users.get(mPager.getCurrentItem()));
                databaseReference.child(users.get(mPager.getCurrentItem()).getUserId()).setValue(users.get(mPager.getCurrentItem()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            finally {
                uMethod.close();
            }
        }

    }

}



