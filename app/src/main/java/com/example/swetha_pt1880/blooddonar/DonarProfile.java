package com.example.swetha_pt1880.blooddonar.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swetha_pt1880.blooddonar.R;
import com.example.swetha_pt1880.blooddonar.database.Database;
import com.example.swetha_pt1880.blooddonar.database.Donar;
import com.example.swetha_pt1880.blooddonar.database.DonarDBMethods;
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
import java.util.HashMap;

import static com.example.swetha_pt1880.blooddonar.R.id.dlName;

public class DonarProfile extends AppCompatActivity {

    TextView  gen, name, dob, age, blood, lastDonated, location, weight, contact;
    ImageButton callDonar;
    Context context;
    Donar donar;
    DonarDBMethods dbMethods;
    UserDBMethods uMethods;
    Database db = new Database(this);
    String TAG = "DonarProfile";
    private final int CALL_REQUEST = 100;
    DatabaseReference databaseReference;
    SessionManagement session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donar_profile);

        //initialising the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Donar Profile");
        setSupportActionBar(toolbar);

        // Enabling Up / Back navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //instance for donar menthods
        dbMethods = new DonarDBMethods(this);
        uMethods = new UserDBMethods(this);
        context = DonarProfile.this;

        //instance for firebase
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //Getting donar object from Intent
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("DATA");
        donar = (Donar) args.getSerializable("chatobj");



        //Session object
        session = new SessionManagement(getApplicationContext());
        Log.v(TAG + "intent value did ", donar+ "");

        //Initialising  the widgets
        gen = (TextView) findViewById(R.id.pdgen);
        name = (TextView) findViewById(dlName);
        dob = (TextView) findViewById(R.id.pddob);
        age = (TextView) findViewById(R.id.pdage);
        blood = (TextView) findViewById(R.id.pdblood);
        lastDonated = (TextView) findViewById(R.id.pddonated);
        location = (TextView) findViewById(R.id.pdLocation);
        callDonar = (ImageButton) findViewById(R.id.callDonar);
        weight = (TextView) findViewById(R.id.pdWeight);
        contact = (TextView)findViewById(R.id.pdcontact);


//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(name.getId()));
//            name.setTransitionName("donar_name");
//        }

        //Setting the donar profile to widgets

        gen.setText(donar.getdGender());
        name.setText(donar.getdName());
        dob.setText(donar.getdDOB());
        age.setText(db.getAge(donar.getdDOB()) + "");
        blood.setText(donar.getdBloodType());
        lastDonated.setText(donar.getdLastDonated());
        location.setText(donar.getdCurrentLoc());
        weight.setText(donar.getdWeight());
        contact.setText(donar.getdContact());
        Log.i(TAG + "contact no :", donar.getdContact());
        callDonar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callPhoneNumber();
                Log.i(TAG +"call", "trying to make a call");
            }


        });


    }

    //Making a call
    public void callPhoneNumber()
    {
        try
        {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                if (ActivityCompat.checkSelfPermission(DonarProfile.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling

                    ActivityCompat.requestPermissions(DonarProfile.this, new String[]{Manifest.permission.CALL_PHONE}, CALL_REQUEST);

                    return;
                }
            }

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + donar.getdContact()));
            startActivity(callIntent);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    //Requesting permissions for making a call
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults)
    {
        if(requestCode == CALL_REQUEST)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                callPhoneNumber();
            }
            else
            {
                Toast.makeText(DonarProfile.this, "denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        HashMap<String, String> sessionUser = session.getUserDetails();
        if (sessionUser.get(SessionManagement.KEY_ID).equals("admin")) {
            menu.findItem(R.id.delete).setVisible(true);
            menu.findItem(R.id.edit).setVisible(true);

        }
               return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.delete){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Do you want to delete " + donar.getdName() + " ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    try {
                       dbMethods.delDonar(donar.getdContact());

                        databaseReference.child("Donars").child(donar.getdContact() + "").getRef().removeValue();
                        final User user = uMethods.getUser(donar.getdContact());
                        if(user.getUserId() != null){
                            user.setDonar(0);
                            Query query1 = databaseReference.child("Users").child(user.getUserId());
                            query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    dataSnapshot.getRef().setValue(user);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Couldn't delete !!", Toast.LENGTH_LONG).show();
                    }
                    finally {
                        dbMethods.close();
                    }
                    Toast.makeText(context, "Deleted  Donar " + donar.getdName(), Toast.LENGTH_LONG).show();
                    //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    onBackPressed();

//                   Intent in = new Intent(context, HomeActivity.class);
//                   context.startActivity(in);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
//                    // User cancelled the dialog
//                    //no action
//                    Toast.makeText(context, "no action", Toast.LENGTH_LONG).show();
                }
            });
            // Create the AlertDialog object and return it
            android.app.AlertDialog dialog = builder.create();
            dialog.show();
        }
        if(item.getItemId() == R.id.edit){
            Log.i(TAG + "edit profile", "im editting");
            Intent in = new Intent(this, AddDonarActivity.class);
            Bundle args = new Bundle();
            args.putSerializable("chatobj",(Serializable)donar);
            in.putExtra("DATA",args);
            in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(in);
            //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
        if (item.getItemId() == android.R.id.home)
            supportFinishAfterTransition();


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
