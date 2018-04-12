package com.example.swetha_pt1880.blooddonar.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swetha_pt1880.blooddonar.R;
import com.example.swetha_pt1880.blooddonar.database.Database;
import com.example.swetha_pt1880.blooddonar.database.Donar;
import com.example.swetha_pt1880.blooddonar.database.DonarDBMethods;
import com.example.swetha_pt1880.blooddonar.database.User;
import com.example.swetha_pt1880.blooddonar.database.UserDBMethods;
import com.example.swetha_pt1880.blooddonar.session.SessionManagement;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class AddDonarActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText dName, dWeight, dContact;
    Spinner dGender, dBloodType;
    AutoCompleteTextView  dLocation;
    TextView  date, donate;
    ImageButton dDOB, dLastDonated;
    Donar donar;
    String gen, blood, contact, city ;
    String dateView;
    String dob, lastD;
    Toolbar toolbar;
    String  TAG = "AddDonarActivty ";
    
     Database db = new Database(this);
    UserDBMethods uMethods = new UserDBMethods(this);
    DonarDBMethods donarMethods = new DonarDBMethods(this);
    DatabaseReference dbRefer;
    SessionManagement sessionManagement;
    User uDonar = new User();
    HashMap<String, String> user;
    DatePickerDialog dob_dialog, ld_dialog ;
    int d_date, d_month, d_year, l_date, l_month, l_year;
    Bundle args;
    TextView appName ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_donar);
        dName = (EditText) findViewById(R.id.nameD);
        dWeight = (EditText) findViewById(R.id.weight);
        dContact = (EditText) findViewById(R.id.contactD);
        dGender = (Spinner) findViewById(R.id.dGen);
        dBloodType = (Spinner) findViewById(R.id.blood);
        date = (TextView) findViewById(R.id.dDOB);
        donate = (TextView) findViewById(R.id.lastDonated);
        dLocation = (AutoCompleteTextView) findViewById(R.id.location);
        dDOB = (ImageButton) findViewById(R.id.dDCal);
        dLastDonated = (ImageButton) findViewById(R.id.dLCal);
        Intent intent = getIntent();
        args = intent.getBundleExtra("DATA");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        appName = (TextView)toolbar.findViewById(R.id.app_name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        sessionManagement = new SessionManagement(this);
        user = sessionManagement.getUserDetails();
        // Enabling Up / Back navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dbRefer = FirebaseDatabase.getInstance().getReference("Donars");

        final Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH);
        final int day = c.get(Calendar.DAY_OF_MONTH);

        showDate(day, month + 1, year);
        dob = dateView;
        lastD = dateView;
        date.setText(dateView);
        donate.setText(dateView);
        dob_dialog = new DatePickerDialog(this, dobListener, year, month, day);
        ld_dialog = new DatePickerDialog(this, donateListener, year, month, day);
        //setting the gender spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dGender.setAdapter(adapter);
        dGender.setOnItemSelectedListener(this);
        List<String> placesList = Arrays.asList(Database.places);

        Collections.sort(placesList, new Comparator<String>() {
            public int compare(String v1, String v2) {
                return v1.toLowerCase().compareTo(v2.toLowerCase());
            }
        });

        //setting the city adapter
        ArrayAdapter cityAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item,
                placesList);
        dLocation.setAdapter(cityAdapter);
       dLocation.setOnItemSelectedListener(this);

        //setting the blood types adapter
        ArrayAdapter<CharSequence> typesAdapter = ArrayAdapter.createFromResource(this,
                R.array.BloodTypes, android.R.layout.simple_spinner_item);
        typesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dBloodType.setAdapter(typesAdapter);

        dBloodType.setOnItemSelectedListener(this);


        if (args != null) {
            donar = (Donar) args.getSerializable("chatobj");
            dName.setText(donar.getdName());
            dName.setSelection(dName.getText().length());
            dWeight.setText(donar.getdWeight());
            dContact.setText(donar.getdContact());
            contact = donar.getdContact();
            dContact.setEnabled(false);
            dGender.setSelection(adapter.getPosition(donar.getdGender()));
            dLocation.setText(donar.getdCurrentLoc());
            dBloodType.setSelection(typesAdapter.getPosition(donar.getdBloodType()));
            date.setText(donar.getdDOB().toString());
            dob = donar.getdDOB().toString();
            lastD = donar.getdLastDonated().toString();
            String dateValues[] = dob.split("/");
            dob_dialog= new DatePickerDialog(this, dobListener,Integer.parseInt(dateValues[2]), Integer.parseInt(dateValues[1]) - 1, Integer.parseInt(dateValues[0]) );
            String donateValues[] = lastD.split("/");
            ld_dialog= new DatePickerDialog(this, donateListener,Integer.parseInt(donateValues[2]), Integer.parseInt(donateValues[1]) - 1, Integer.parseInt(donateValues[0]) );
            gen = donar.getdGender();
            blood = donar.getdBloodType();
            donate.setText(donar.getdLastDonated().toString());
            city = (donar.getdCurrentLoc());
        }


        //initialising the calendar


        if (user.get(SessionManagement.KEY_ID) != (null)) {
            Log.i("user ", "session");
            HashMap<String, String> loginUser = sessionManagement.getUserDetails();
            String uid = loginUser.get(SessionManagement.KEY_ID);
            if (!uid.equals("admin")) {
                uDonar = uMethods.getProfile(uid);
                dName.setText(uDonar.getuName());
                date.setText(uDonar.getuDOB());
                dob = uDonar.getuDOB();
                dContact.setText(uDonar.getuContact());
                dGender.setSelection(adapter.getPosition(uDonar.getuGender()));

                Log.i("user data", "setting");
            }


        }


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.set(d_year, d_month, d_date);
                dob_dialog.show();
            }
        });
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.set(l_year, l_month, l_date);
                ld_dialog.show();
            }
        });
        dDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.set(d_year, d_month, d_date);
                dob_dialog.show();
            }
        });

        dLastDonated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.set(l_year, l_month, l_date);
                ld_dialog.show();
            }
        });
        dLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               city =  String.valueOf(adapterView.getItemAtPosition(i));
            }
        });


    }




   final DatePickerDialog.OnDateSetListener dobListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                showDate(dayOfMonth, monthOfYear + 1, year);
                d_date = dayOfMonth;
                d_month = monthOfYear;
                d_year = year;
                date.setText(dateView);
                Log.i("dob", dateView);
                dob = dateView;
            }

        };

    final DatePickerDialog.OnDateSetListener donateListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            showDate(dayOfMonth, monthOfYear + 1, year);
            l_date = dayOfMonth;
            l_month = monthOfYear;
            l_year = year;
            donate.setText(dateView);
            lastD = dateView;
            Log.i("lastDonated", dateView);
        }

    };
    void showDate(int day, int month, int year){
        dateView = (new StringBuilder().append(day).append("/").append(month).append("/").append(year)).toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (args != null) {
            dbRefer = FirebaseDatabase.getInstance().getReference("Users");
            User updateUser = uMethods.getUser(contact);
            if (updateUser != null & updateUser.getUserId() != null) {
                updateUser.setuName(donar.getdName());
                updateUser.setuDOB(donar.getdDOB());
                updateUser.setuGender(donar.getdGender());
                Log.i(TAG + "user update", updateUser.getUserId() + " updating" );

                try {
                    uMethods.updateUser(updateUser);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                dbRefer.child(updateUser.getUserId()).setValue(updateUser);
            }
        }


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
       Log.d("id",String.valueOf(adapterView.getItemAtPosition(i)));
       String result = String.valueOf(adapterView.getItemAtPosition(i)).toLowerCase();
       if(result.equals("male") || result.equals("female") || result.equals("other")){
           gen= adapterView.getItemAtPosition(i).toString();
       }else if (result.equals("o+")|| result.equals("o-")|| result.equals("a+")|| result.equals("a-") ||result.equals("b+")|| result.equals("b-")|| result.equals("ab+")|| result.equals("ab-")) {
           blood = adapterView.getItemAtPosition(i).toString();
        }
// else
//           city = String.valueOf(adapterView.getItemAtPosition(i));

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("add donor act",resultCode+" m");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.tick).setVisible(true);
        menu.findItem(R.id.delete).setVisible( false);
        menu.findItem(R.id.edit).setVisible(false);
        if (args == null)
            appName.setText("Donar Registration");
        else
            appName.setText("Donar Updation");
        appName.setVisibility(View.VISIBLE);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click

            switch (item.getItemId()) {
                case R.id.tick:
                    if (dName.getText().toString().isEmpty()) {
                        dName.requestFocus();
                        dName.setError("Field shouldn't be empty");
                    } else if (dContact.getText().toString().isEmpty() || dContact.getText().toString().replaceAll(" ", "").length() != 10) {
                        dContact.requestFocus();
                        dContact.setError("Must be of 10 numbers");
                    } else if (dWeight.getText().toString().isEmpty()) {
                        dWeight.requestFocus();
                        dWeight.setError("Field cannot be empty");
                    } else if (db.getAge(dob) < 18 || db.getAge(dob) > 80) {
                        dDOB.requestFocus();

                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("Age must be between 18 and 80");

                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        android.app.AlertDialog dialog = builder.create();
                        dialog.show();
                    } else if (db.getAge(lastD) > db.getAge(dob)) {
                        dLastDonated.requestFocus();
                        Log.i("age checking", db.getMonths(dob) + " ");
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("enter a valid date");
                        builder.setMessage("Age must be between 18 and 80");

                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        android.app.AlertDialog dialog = builder.create();
                        dialog.show();
                    } else {
                        Log.d("donar gen", gen);
                        if (args == null) {

                            long result = -1;
                            try {
                               // city = dLocation.getText().toString();
                                donar = new Donar(dName.getText().toString(), dob, gen, dContact.getText().toString().replaceAll(" ", ""), city, blood, lastD, dWeight.getText().toString());
                                result = donarMethods.addDonar(dName.getText().toString(), dob, gen, dContact.getText().toString(), city, blood, lastD, dWeight.getText().toString());

                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(AddDonarActivity.this, "Exception", Toast.LENGTH_SHORT).show();
                            }
                            finally {
                                donarMethods.close();
                            }

                            if (result == -1)
                                Toast.makeText(AddDonarActivity.this, "Couldnt add a Donar", Toast.LENGTH_SHORT).show();
                            else {
                                dbRefer.child(donar.getdContact()).setValue(donar);

                                uDonar.setDonar(1);
                                Log.i("donar", uDonar.getDonar() + "");
                                try {
                                    Log.i("donar", uDonar.getDonar() + "");
                                    uMethods.updateUser(uDonar);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                finally {
                                    donarMethods.close();
                                }
                                Toast.makeText(AddDonarActivity.this, "Successfull ", Toast.LENGTH_SHORT).show();
                                Intent in = new Intent(AddDonarActivity.this, HomeActivity.class);
                                in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(in);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();
                            }
                        } else {
                            //city = dLocation.getText().toString();
                            donar = new Donar(dName.getText().toString(), dob, gen, dContact.getText().toString().replaceAll(" ", ""),city, blood, lastD, dWeight.getText().toString());
                            try {
                                donarMethods.updateDonar(donar);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            finally {
                                donarMethods.close();
                            }
                            dbRefer.child(donar.getdContact()).setValue(donar);
                            //finish();
                            Intent in = new Intent(this, DonarProfile.class);
                            Log.i("did, age ", " " + donar);
                            Bundle args = new Bundle();
                            args.putSerializable("chatobj", (Serializable) donar);
                            in.putExtra("DATA", args);
                            in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(in);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            finish();
                        }
                    }
                    break;
                case  android.R.id.home :
                    onBackPressed();
                    //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                    break;

        }
        return super.onOptionsItemSelected(item);
    }








}
