package com.example.swetha_pt1880.blooddonar.activity;


import android.app.ActivityManager;
import android.app.DatePickerDialog;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;


public class AddUserActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {
    EditText userId, uName, uContact,  admin_priv;
    TextView donarText;
    Spinner gender, privledge, uDonar;
    TextView uDate;
    ImageButton DOB;
    Database db = new Database(this);
    String gen, contact;
    String priv;
    User updateUser = new User();
    UserDBMethods uMethod = new UserDBMethods(this);
    DonarDBMethods dbMethods = new DonarDBMethods(this);
    String TAG = "AddUser";
    String dateView;
    Toolbar toolbar;
    DatabaseReference dbRefer;
    Bundle args;
    DatabaseReference databaseReference;
    ArrayList<User> user = new ArrayList<>();
    int beDonar = 0;
    DatePickerDialog dialog;
    TextView appName ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_user);

        gender = (Spinner) findViewById(R.id.uGen);
        privledge = (Spinner) findViewById(R.id.uPriv);
        DOB = (ImageButton)findViewById(R.id.uCal);
        uDate = (TextView)findViewById(R.id.uDOB);
        userId = (EditText)findViewById(R.id.puid);
        uName = (EditText)findViewById(R.id.uName);
        uContact = (EditText)findViewById(R.id.uContact);
        uDonar = (Spinner) findViewById(R.id.u_donar);
        donarText = (TextView)findViewById(R.id.ublooddonar);
        admin_priv = (EditText) findViewById(R.id.admin_priv);

        final Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH);
        final int day = c.get(Calendar.DAY_OF_MONTH);
        dialog = new DatePickerDialog(AddUserActivity.this,myDateListener,year, month,day);


        showDate(day, month + 1, year);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        appName = (TextView)toolbar.findViewById(R.id.app_name);
        //toolbar.setTitle("");
        //toolbar.setTitle("User Registration");

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");




        // Enabling Up / Back navigation
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbRefer  = FirebaseDatabase.getInstance().getReference();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter);
        gender.setOnItemSelectedListener(this);


        //setting the donar value of user
        ArrayAdapter<CharSequence> bool_adapter = ArrayAdapter.createFromResource(this,
                R.array.donar_bool, android.R.layout.simple_spinner_item);
        bool_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        uDonar.setAdapter(bool_adapter);
        uDonar.setOnItemSelectedListener(this);

        //setting the blood types adapter
        ArrayAdapter<CharSequence> typesAdapter = ArrayAdapter.createFromResource(this,
                R.array.Priviledges, android.R.layout.simple_spinner_item);
        typesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        privledge.setAdapter(typesAdapter);
        privledge.setOnItemSelectedListener(this);

        Intent intent = getIntent();
        args = intent.getBundleExtra("DATA");
        if(args != null) {

            updateUser = (User) args.getSerializable("chatobj");
            userId.setText(updateUser.getUserId());
            userId.setEnabled(false);
            uName.requestFocus();

            // AIzaSyAnbeYC0ULD_KpMoVo6_FzT-kLNn56y-Ck
            uName.setText(updateUser.getuName());
            uName.setSelection(uName.getText().length());
            uDate.setText(updateUser.getuDOB());
            dateView = updateUser.getuDOB();
            String dateValues[] = dateView.split("/");
            dialog.updateDate(Integer.parseInt(dateValues[2]), Integer.parseInt(dateValues[1]) - 1, Integer.parseInt(dateValues[0]) );
            uContact.setText(updateUser.getuContact());
            contact = updateUser.getuContact();
            if(updateUser.getUserId().equals("admin")){
                admin_priv.setText("Super User");
                priv = "two";
                admin_priv.setVisibility(View.VISIBLE);
                privledge.setVisibility(View.GONE);
            }else {
                if (updateUser.getuPriviledge().equals("one"))
                    privledge.setSelection(typesAdapter.getPosition("grant"));
                else
                    privledge.setSelection(typesAdapter.getPosition("revoke"));
            }
            gender.setSelection(adapter.getPosition(updateUser.getuGender()));
            uDonar.setVisibility(View.VISIBLE);
            if(updateUser.getDonar() == 0)
                uDonar.setSelection(bool_adapter.getPosition("No"));
            else
                uDonar.setSelection(bool_adapter.getPosition("Yes"));
            donarText.setVisibility(View.VISIBLE);

        }


        DOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 dialog.show();
            }
        });

        uDate.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 dialog.show();
             }
         }
        );
    }



    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            showDate(dayOfMonth, monthOfYear + 1, year);
        }

    };
    void showDate(int day, int month, int year){
        dateView = (new StringBuilder().append(day).append("/").append(month).append("/").append(year)).toString();
        uDate.setText(dateView);

        //Toast.makeText(this, dateView, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d("id",String.valueOf(adapterView.getItemAtPosition(i)));
        String result = String.valueOf(adapterView.getItemAtPosition(i));
        if(result.equalsIgnoreCase("Male") || result.equalsIgnoreCase("Female") || result.equalsIgnoreCase("Other")){
            gen= adapterView.getItemAtPosition(i).toString();
        }else  if (result.equalsIgnoreCase("No") )
            beDonar = 0;
        else if (result.equalsIgnoreCase("Yes"))
            beDonar = 1;
        else{
            priv = adapterView.getItemAtPosition(i).toString();
            if (priv.equals("grant"))
                priv = "one";
            else  if (priv.equals("revoke"))
                    priv = "zero";
            else
                priv = "two";
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.delete).setVisible(false);
        menu.findItem(R.id.edit).setVisible(false);
        menu.findItem(R.id.tick).setVisible(true);
        if(args == null)
            appName.setText("User Registration");
        else
            appName.setText("User Updation");
        appName.setVisibility(View.VISIBLE);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click

            switch (item.getItemId()) {
                case R.id.tick:
                    long result = -1;
                    if (userId.getText().toString().isEmpty()) {
                        userId.requestFocus();
                        userId.setError("Field cannot be empty");
                    } else if (uName.getText().toString().isEmpty()) {
                        uName.requestFocus();
                        uName.setError("Field cannot be empty");
                    } else if (uContact.getText().toString().isEmpty() || uContact.getText().toString().replaceAll(" ", "").length() != 10) {
                        uContact.requestFocus();
                        uContact.setError("Contact number must be of 10 numbers");
                    } else if (db.getAge(dateView) < 18 || db.getAge(dateView) > 80) {
                        DOB.requestFocus();
                        Toast.makeText(this, " Must be of age above 18", Toast.LENGTH_SHORT).show();
                    } else {
                        if (args == null) {

                            Toast.makeText(this, db.getAge(dateView) + " null ", Toast.LENGTH_LONG).show();
                            try {
                                updateUser = new User(userId.getText().toString(), uName.getText().toString(), dateView, gen, uContact.getText().toString().replaceAll(" ", ""), userId.getText().toString(), priv, 0);
                                result = uMethod.addUser(userId.getText().toString(), uName.getText().toString(), dateView, gen, uContact.getText().toString().replaceAll(" ", ""), userId.getText().toString(), priv, 0);
                            } catch (SQLException e) {
                                e.printStackTrace();
                                Toast.makeText(AddUserActivity.this, "Exception", Toast.LENGTH_LONG).show();
                            }
                            finally {
                                uMethod.close();
                            }
                            if (result == -1)
                                Toast.makeText(AddUserActivity.this, "Couldnt add a user", Toast.LENGTH_LONG).show();
                            else {
                                databaseReference.child(updateUser.getUserId()).setValue(updateUser);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();
                                Toast.makeText(AddUserActivity.this, "Successfull", Toast.LENGTH_SHORT).show();
                            }

                        } else {

                            updateUser = new User(userId.getText().toString(), uName.getText().toString(), dateView, gen, uContact.getText().toString(), userId.getText().toString(), priv, beDonar);

                            try {

                                uMethod.updateUser(updateUser);
                                databaseReference.child(updateUser.getUserId()).setValue(updateUser);
                                Log.i(TAG + "updating ", "update");
                            } catch (SQLException e) {
                                Log.i(TAG + "update error", e.getMessage());
                                e.printStackTrace();
                            }
                            finally {
                                uMethod.close();
                            }
                            Intent in = new Intent(this, ProfileActivity.class);
                            in.putExtra("id", updateUser.getUserId());
                            in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            //supportFinishAfterTransition();
                            setResult(2, in);
                            //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            finish();


                        }
                    }
                    break;

                case  android.R.id.home :
                    onBackPressed();
                    supportFinishAfterTransition();
                    //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    break;


        }
        return false;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (updateUser.getDonar() == 1){
            Donar donar = new Donar();
            databaseReference = FirebaseDatabase.getInstance().getReference("Donars");
            donar = dbMethods.getDonarProfile(contact);
            if (donar != null & donar.getdName() != null) {
                donar.setdName(updateUser.getuName());
                donar.setdDOB(updateUser.getuDOB());
                donar.setdGender(updateUser.getuGender());
                Log.i(TAG + "donar update", donar.getdContact() + updateUser.getuContact());
                if (!donar.getdContact().equals(updateUser.getuContact())) {

                    databaseReference.child(donar.getdContact()).getRef().removeValue();
                    donar.setdContact(updateUser.getuContact());
                    try {
                        dbMethods.delDonar(donar.getdContact());
                        dbMethods.addDonar(donar.getdName(), donar.getdDOB(), donar.getdGender(), donar.getdContact(), donar.getdCurrentLoc(), donar.getdBloodType(), donar.getdLastDonated(), donar.getdWeight());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    finally {
                        dbMethods.close();
                    }

                } else {
                    try {
                        dbMethods.updateDonar(donar);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    finally {
                        dbMethods.close();
                    }
                }
                databaseReference.child(donar.getdContact()).setValue(donar);
            }
        }
        ActivityManager m = (ActivityManager) getSystemService( ACTIVITY_SERVICE );
        List<ActivityManager.RunningTaskInfo> runningTaskInfoList = m.getRunningTasks(10);
        Iterator<ActivityManager.RunningTaskInfo> itr = runningTaskInfoList.iterator();
        while (itr.hasNext()) {
            ActivityManager.RunningTaskInfo runningTaskInfo = (ActivityManager.RunningTaskInfo) itr.next();
            int id = runningTaskInfo.id;
            CharSequence desc = runningTaskInfo.description;
            int numOfActivities = runningTaskInfo.numActivities;
            String topActivity = runningTaskInfo.topActivity
                    .getShortClassName();
            Log.i(TAG + "Debug " + id, numOfActivities + " " +topActivity + " data");

        }

    }
}
