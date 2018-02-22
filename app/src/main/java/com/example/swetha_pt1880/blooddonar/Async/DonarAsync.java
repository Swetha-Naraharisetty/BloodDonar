package com.example.swetha_pt1880.blooddonar.Async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.swetha_pt1880.blooddonar.activity.LoginActivity;
import com.example.swetha_pt1880.blooddonar.database.Donar;
import com.example.swetha_pt1880.blooddonar.database.DonarDBMethods;
import com.example.swetha_pt1880.blooddonar.database.User;
import com.example.swetha_pt1880.blooddonar.database.UserDBMethods;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by swetha-pt1880 on 21/2/18.
 */

public class DonarAsync extends AsyncTask {
    Context mcontext;
    DatabaseReference dRefer, uRefer;
    DonarDBMethods dbMethods;
    UserDBMethods uMethod ;
    ArrayList<Donar> donars = new ArrayList<>();
    ArrayList<User> users = new ArrayList<>();
    String TAG = " DonarAsync ";


    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
    }

    public DonarAsync(Context context) {

        mcontext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dRefer = FirebaseDatabase.getInstance().getReference("Donars");
        uRefer = FirebaseDatabase.getInstance().getReference("Users");
        dbMethods = new DonarDBMethods(mcontext);
        uMethod = new UserDBMethods(mcontext);

    }


    @Override
    protected DataObjects doInBackground(Object[] objects) {
        //final DataObjects dataObjects = new DataObjects();
        dRefer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                donars.clear();
                try {
                    dbMethods.deleteDonars();

                } catch (SQLException e) {
                    e.printStackTrace();
                }


                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Donar donar = data.getValue(Donar.class);
                    if(donar != null)
                        donars.add(donar);
                }
               // dataObjects.setDonars(donars);
                try {
                    Log.i(TAG + "populate donar", "populate");
                    dbMethods.populateDonar(donars);

                } catch (SQLException e) {
                    Toast.makeText(mcontext, "couldnt push the data", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(mcontext, "cannot load donar data", Toast.LENGTH_LONG).show();
            }
        });



        uRefer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                users.clear();

                Log.i(TAG + "populate user", "populate");
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    User user = data.getValue(User.class);
                    Log.i(TAG + "user donar +++++ ", user.getuName() + user.getDonar() + user.getuPassWord() + user.getuPriviledge());
                    users.add(user);

                }
                try {
                    Log.i(TAG + "populate donar", "populate");
                    uMethod.populateUsers(users);
                } catch (SQLException e) {
                    Toast.makeText(mcontext, "couldnt push the data", Toast.LENGTH_SHORT).show();
                }
                //dataObjects.setUsers(users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(mcontext,"server error for users", Toast.LENGTH_LONG).show();

            }
        });


        publishProgress (1);

        return null;
    }
}

class DataObjects {
    DataObjects(){}

    public ArrayList<Donar> getDonars() {
        return donars;
    }

    public void setDonars(ArrayList<Donar> donars) {
        this.donars = donars;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    ArrayList<Donar> donars;
    ArrayList<User> users;
    DataObjects(ArrayList<Donar> donars, ArrayList<User> users){
        this.donars = donars;
        this.users = users;
    }
}
