package com.example.swetha_pt1880.blooddonar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swetha_pt1880.blooddonar.Async.DonarAsync;
import com.example.swetha_pt1880.blooddonar.R;
import com.example.swetha_pt1880.blooddonar.database.Donar;
import com.example.swetha_pt1880.blooddonar.database.DonarDBMethods;
import com.example.swetha_pt1880.blooddonar.database.User;
import com.example.swetha_pt1880.blooddonar.database.UserDBMethods;
import com.example.swetha_pt1880.blooddonar.session.SessionManagement;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    Button login;
    String uid, upass;

    TextInputLayout usernameWrapper;
     TextInputLayout passwordWrapper ;
    String TAG = "LoginActivity  :";
    Intent in;
    SessionManagement sessionManagement;
    DatabaseReference dRefer, uRefer;
    DonarAsync donarAsync;
    DonarDBMethods dbMethods = new DonarDBMethods(this);
    UserDBMethods uMethod = new UserDBMethods(this);
    ArrayList<Donar> donars = new ArrayList<>();
    ArrayList<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login_layout);
        donarAsync = new DonarAsync(this);

        sessionManagement = new SessionManagement(getApplicationContext());
        usernameWrapper = (TextInputLayout) findViewById(R.id.user_id);
        passwordWrapper = (TextInputLayout) findViewById(R.id.user_password);
        login = (Button)findViewById(R.id.user_login);

        HashMap<String, String> user = sessionManagement.getUserDetails();
        in = new Intent(LoginActivity.this, HomeActivity.class);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Blood Donar");
        setSupportActionBar(toolbar);

        dRefer = FirebaseDatabase.getInstance().getReference("Donars");
        uRefer = FirebaseDatabase.getInstance().getReference("Users");
        //checkng whether the user is already logged in
        Log.i(TAG + "user", "updating");
        if (user.get(SessionManagement.KEY_ID) != (null)) {
            Log.i(TAG + "user", "updating 1111");
            try {
                HashMap<String, String> loginUser = sessionManagement.getUserDetails();
                uid = loginUser.get(SessionManagement.KEY_ID);
                upass = loginUser.get(SessionManagement.KEY_NAME);
                Log.i(TAG + "credentials----", uid + upass);
                if (uMethod.checkUser(uid, upass).equals("one")) {
                    in.putExtra("credential", uid);
                    in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(in);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                } else if (uMethod.checkUser(uid, upass).equals("two")) {
                    in.putExtra("credential", "admin");
                    in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(in);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.i(TAG + "user", "updating 32222");


        }
        passwordWrapper.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    login.performClick();
                    return true;
                }
                return false;
            }
        });

        //signing the user
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uid = usernameWrapper.getEditText().getText().toString();
                upass = passwordWrapper.getEditText().getText().toString();
                if (uid.isEmpty()) {
                   usernameWrapper.getEditText().requestFocus();
                   usernameWrapper.getEditText().setError("Invalid User Id");
                } else if (upass.isEmpty()) {
                    passwordWrapper.getEditText().requestFocus();
                    passwordWrapper.getEditText().setError("Wrong Password");
                } else {
                    Log.i(TAG + "credentials", uid + upass);
                    sessionManagement.createLoginSession(upass, uid);
                    try {
                        Log.i(TAG + "output", uMethod.checkUser(uid, upass));


                        switch (uMethod.checkUser(uid, upass)) {
                            case "zero":
                                Toast.makeText(LoginActivity.this, "Permissions revoked !! Contact Admin", Toast.LENGTH_LONG).show();
                                break;

                            case "one":
                                in.putExtra("credential", uid);
                                in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(in);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();
                                break;
                            case "two":
                                in.putExtra("credential", "admin");
                                in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(in);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();
                                break;
                            case "three":
                                usernameWrapper.getEditText().requestFocus();
                                usernameWrapper.getEditText().setError("Invalid User Id");
                                Toast.makeText(LoginActivity.this, "You are not a Authorized user !!", Toast.LENGTH_LONG).show();
                                break;

                            case "pass":
                                passwordWrapper.getEditText().requestFocus();
                                passwordWrapper.getEditText().setError("Wrong Password");
                                break;

                        }

                    } catch (SQLException e) {
                        e.printStackTrace();

                    }
                }

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
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
                    Toast.makeText(LoginActivity.this, "couldnt push the data", Toast.LENGTH_SHORT).show();
                }
                finally {
                    dbMethods.close();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "cannot load donar data", Toast.LENGTH_LONG).show();
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
                    uMethod.deleteUsers();
                    Log.i(TAG + "populate donar", "populate");
                    uMethod.populateUsers(users);
                } catch (SQLException e) {
                    Toast.makeText(LoginActivity.this, "couldnt push the data", Toast.LENGTH_SHORT).show();
                }
                finally {
                    uMethod.close();
                }
                //dataObjects.setUsers(users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(LoginActivity.this,"server error for users", Toast.LENGTH_LONG).show();

            }
        });


    }
}
