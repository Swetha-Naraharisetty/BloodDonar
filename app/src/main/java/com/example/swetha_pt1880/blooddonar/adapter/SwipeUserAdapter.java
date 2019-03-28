package com.example.swetha_pt1880.blooddonar.adapter;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swetha_pt1880.blooddonar.R;
import com.example.swetha_pt1880.blooddonar.activity.AddUserActivity;
import com.example.swetha_pt1880.blooddonar.activity.UserScreenSlideActivity;
import com.example.swetha_pt1880.blooddonar.database.Database;
import com.example.swetha_pt1880.blooddonar.database.User;
import com.example.swetha_pt1880.blooddonar.database.UserDBMethods;
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
import java.util.HashMap;

/**
 * Created by swetha-pt1880 on 22/2/18.
 */

public class SwipeUserAdapter extends PagerAdapter {
    Context mContext;
    TextView id, gen,name,dob,age, priv, donar,contact, password;
    ImageButton editPass, editPhone, editPriv, editName, editDonar;
    UserDBMethods uMethod;
   UserScreenSlideActivity screenSlideActivity = new UserScreenSlideActivity();
    AppCompatActivity activity;
    ArrayList<User> users = new ArrayList<>();

    Database db;
    String uId, loggedUser;

    String TAG = "Swipe User Adapter ";

    User user = new User();
    DatabaseReference databaseReference;
    SessionManagement sessionManagement;
    Toolbar toolbar;
    private View mCurrentView;
    public boolean pos = true;

    public SwipeUserAdapter(Context context, ArrayList<User> users,int position1){
        mContext = context;
        this.users = users;
        uMethod = new UserDBMethods(mContext);
        db = new Database(mContext);
        activity = (AppCompatActivity) mContext;
       //activity.getMenuInflater().inflate(R.menu.menu_main, menu);

    }

    @Override
    public int getCount() {
        return users.size();

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;

    }

    @Override
    public Object instantiateItem(ViewGroup container,int position) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.user_profile, container, false);

         id = (TextView)rootView.findViewById(R.id.puid);
        gen = (TextView)rootView.findViewById(R.id.pugen);
        name = (TextView)rootView.findViewById(R.id.puName);
        dob = (TextView)rootView.findViewById(R.id.puDob);
        age = (TextView)rootView.findViewById(R.id.puage);
        priv = (TextView) rootView.findViewById(R.id.puPriv);
        donar = (TextView)rootView.findViewById(R.id.puDonar);
        contact = (TextView) rootView.findViewById(R.id.pucontact);
        password = (TextView) rootView.findViewById(R.id.pupass);
        editPass = (ImageButton)rootView.findViewById(R.id.editPass);
        editPhone = (ImageButton)rootView.findViewById(R.id.editPhone);
        editPriv = (ImageButton)rootView.findViewById(R.id.edit_upriv);
        editName = (ImageButton)rootView.findViewById(R.id.edit_uname);
        editDonar = (ImageButton)rootView.findViewById(R.id.edit_udonar);

        Log.i(TAG + "instant" , "true ");



       // activity.setSupportActionBar(toolbar);


//
        sessionManagement = new SessionManagement(mContext);
        toolbar=  ((UserScreenSlideActivity)mContext ).findViewById(R.id.toolbar);
        HashMap<String, String> sessionUser = sessionManagement.getUserDetails();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");


        Log.i(TAG + "users count", users.get(position).getUserId()+ " " + position) ;
        setUserData(users.get(position));
        loggedUser = sessionUser.get(SessionManagement.KEY_ID);
        if( !loggedUser.equals("admin")){
            editPass.setVisibility(View.VISIBLE);
            editPhone.setVisibility(View.VISIBLE);
        }


        //notifyDataSetChanged();

        container.addView(rootView);
        return rootView;
        //return super.instantiateItem(container, position);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mCurrentView = (View)object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }





    void setUserData(User user){

        // Enabling Up / Back navigation

        id.setText(user.getUserId());
        gen.setText(user.getuGender());
        name.setText(user.getuName());
        dob.setText(user.getuDOB());
        if(user.getuDOB().toString() == "")
            age.setText("null");
        else
            age.setText("30");
        contact.setText(user.getuContact());
        password.setText(user.getuPassWord());
        if(user.getuPriviledge().equals("one")) {
            priv.setText("Granted");
            priv.setTextColor(-16711936); //green color
        } else if(user.getuPriviledge().equals("zero")) {
            priv.setText("Revoked");
            priv.setTextColor(-65536);//red color
        }else
            priv.setText("Super User");
        if(user.getDonar() == 0) {
            donar.setText("No");
            donar.setTextColor(-65536);//red color
        }else {
            donar.setText("Yes");
            donar.setTextColor(-16711936); //green color
        }
        //toolbar.setTitle(users.get((int)mCurrentView.getId()).getUserId());
        Log.i(TAG + user.getUserId(), toolbar.getTitle().toString());
    }



}
