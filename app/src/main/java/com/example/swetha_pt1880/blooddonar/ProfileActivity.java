package com.example.swetha_pt1880.blooddonar.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Layout;
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
import com.example.swetha_pt1880.blooddonar.database.Database;
import com.example.swetha_pt1880.blooddonar.database.User;
import com.example.swetha_pt1880.blooddonar.database.UserDBMethods;
import com.example.swetha_pt1880.blooddonar.listener.OnSwipeTouchListener;
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
import java.util.Iterator;
import java.util.List;

public class ProfileActivity extends AppCompatActivity  {

    TextView id, gen,name,dob,age, priv, donar,contact, password, user_text;
    ImageButton editPass, editPhone, editPriv, editName, editDonar;
    UserDBMethods uMethod = new UserDBMethods(this);
    String uId, loggedUser;
    ArrayList<User> users = new ArrayList<>();
    String TAG = "ProfileActivity";
    Database db = new Database(this);
    User user = new User();
   View actionBar;
    DatabaseReference databaseReference;
    SessionManagement sessionManagement;
    Toolbar toolbar;
    EditText edittext ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Your Profile");
        setSupportActionBar(toolbar);
        actionBar = (View) findViewById(R.id.action);
        actionBar.setVisibility(View.VISIBLE);
        user_text = (TextView)findViewById(R.id.user_text);
        uId = getIntent().getStringExtra("id");
        Log.i(TAG +" profile id", uId);
        id = (TextView)findViewById(R.id.puid);
        gen = (TextView)findViewById(R.id.pugen);
        name = (TextView)findViewById(R.id.puName);
        dob = (TextView)findViewById(R.id.puDob);
        age = (TextView)findViewById(R.id.puage);
        priv = (TextView) findViewById(R.id.puPriv);
        donar = (TextView)findViewById(R.id.puDonar);
        contact = (TextView) findViewById(R.id.pucontact);
        password = (TextView) findViewById(R.id.pupass);
        editPass = (ImageButton)findViewById(R.id.editPass);
        editPhone = (ImageButton)findViewById(R.id.editPhone);
        editPriv = (ImageButton)findViewById(R.id.edit_upriv);
        editName = (ImageButton)findViewById(R.id.edit_uname);
        editDonar = (ImageButton)findViewById(R.id.edit_udonar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        users = uMethod.getUserList();
        sessionManagement = new SessionManagement(getApplicationContext());
        HashMap<String, String> sessionUser = sessionManagement.getUserDetails();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        setUserData();

        loggedUser = sessionUser.get(SessionManagement.KEY_ID);
        if( !loggedUser.equals("admin")){
            editPass.setVisibility(View.VISIBLE);
            editPhone.setVisibility(View.VISIBLE);
        }

        editPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //contact.setEnabled(true);
                final AlertDialog.Builder alert = new AlertDialog.Builder(ProfileActivity.this);
                edittext = new EditText(ProfileActivity.this);
                edittext.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_CLASS_NUMBER);
                edittext.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                edittext.setText(user.getuContact());
                alert.setMessage("please enter new Contact number");
                alert.setTitle("Contact Information");
                alert.setView(edittext);
                alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //What ever you want to do with the value
                        String newContact = edittext.getText().toString().replaceAll(" ", "");

                        if(newContact.length() != 10){
                            contact.requestFocus();

                            final AlertDialog.Builder warning = new AlertDialog.Builder(ProfileActivity.this);
//
                            warning.setMessage("enter a valid number");
                           warning.show();
                            Toast.makeText(ProfileActivity.this,  "Enter a valid number",Toast.LENGTH_SHORT).show();
                        } else {
                            contact.setText(newContact);
                            user.setuContact(newContact);
                            Toast.makeText(ProfileActivity.this,  newContact,Toast.LENGTH_SHORT).show();
                        }


                    }
                });

                alert.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                    }
                });
                alert.show();

            }
        });

        editPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alert = new AlertDialog.Builder(ProfileActivity.this);
                alert.setMessage("please enter new Password");
                alert.setTitle("New Password");
                edittext = new EditText(ProfileActivity.this);
                edittext.setText(user.getuPassWord());
                alert.setView(edittext);

                alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //What ever you want to do with the value
                        String newPassword = edittext.getText().toString();
                        if(newPassword.equals(user.getuPassWord())){
                            final AlertDialog.Builder warning = new AlertDialog.Builder(ProfileActivity.this);
//
                            warning.setMessage("Password remains unchanged");
                            warning.show();
                        }

                        password.setText(newPassword);
                        user.setuPassWord(newPassword);

                        Toast.makeText(ProfileActivity.this,  newPassword,Toast.LENGTH_SHORT).show();
                    }
                });

                alert.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.


                    }
                });
                alert.show();
            }
        });






    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

            menu.findItem(R.id.delete).setVisible(false);
       if(loggedUser.equals("admin"))
            menu.findItem(R.id.edit).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.delete){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to delete " + user.getuName() + " ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    try {
                        uMethod.delUser(uId);
                        Query query = databaseReference.child(user.getUserId());
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
                        Toast.makeText(ProfileActivity.this, "Couldn't delete !!", Toast.LENGTH_LONG).show();
                    }
                    finally {
                        uMethod.close();
                    }
                    Toast.makeText(ProfileActivity.this, "Deleted User : " + uId, Toast.LENGTH_LONG).show();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                    //no action
                    Toast.makeText(ProfileActivity.this, "no action", Toast.LENGTH_LONG).show();
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
                args.putSerializable("chatobj", (Serializable) user);
                in.putExtra("DATA", args);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(this,user_text , "test");


                startActivityForResult(in, 2);
                //startActivity(in,  options.toBundle());
                //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }
            else if (item.getItemId() == android.R.id.home )
            onBackPressed();
        return true;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2){

            uId = getIntent().getStringExtra("id");
            setUserData();
        }

    }

        @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG + "on destroy", "is called");
        if(loggedUser.equals(uId)) {
            try {
                uMethod.updateUser(user);
                databaseReference.child(user.getUserId()).setValue(user);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            finally {
                uMethod.close();
            }
        }
            ActivityManager m = (ActivityManager) getSystemService( ACTIVITY_SERVICE );
            List<RunningTaskInfo> runningTaskInfoList = m.getRunningTasks(10);
            Iterator<RunningTaskInfo> itr = runningTaskInfoList.iterator();
            while (itr.hasNext()) {
                RunningTaskInfo runningTaskInfo = (RunningTaskInfo) itr.next();
                int id = runningTaskInfo.id;
                CharSequence desc = runningTaskInfo.description;
                int numOfActivities = runningTaskInfo.numActivities;
                String topActivity = runningTaskInfo.topActivity
                        .getShortClassName();
                Log.i(TAG + " " + id,  numOfActivities + " " +topActivity + " data");

            }


    }




    void setUserData(){
        user= uMethod.getProfile(uId);
        // Enabling Up / Back navigation

        id.setText(user.getUserId());
        gen.setText(user.getuGender());
        name.setText(user.getuName());
        dob.setText(user.getuDOB());
        if(user.getuDOB().toString() == "")
            age.setText("null");
        else
            age.setText(db.getAge(user.getuDOB()) + " ");
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
    }



}
