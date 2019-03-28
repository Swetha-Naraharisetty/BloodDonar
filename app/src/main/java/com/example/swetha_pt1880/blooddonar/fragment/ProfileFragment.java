package com.example.swetha_pt1880.blooddonar.fragment;

import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
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

public class ProfileFragment extends Fragment {

    TextView id, gen,name,dob,age, priv, donar,contact, password;
    ImageButton editPass, editPhone, editPriv, editName, editDonar;
    UserDBMethods uMethod = new UserDBMethods(getActivity());
    String uId, loggedUser;
    ArrayList<User> users = new ArrayList<>();
    String TAG = "ProfileActivity";
    Database db = new Database(getActivity());
    User user = new User();
    DatabaseReference databaseReference;
    SessionManagement sessionManagement;
    Toolbar toolbar;
    EditText edittext ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.user_profile, container, false);

        
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("Your Profile");
        getActivity().setActionBar(toolbar);

        uId = getActivity().getIntent().getStringExtra("id");
        Log.i(TAG +" profile id", uId);
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

        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);


        users = uMethod.getUserList();
        sessionManagement = new SessionManagement(getActivity().getApplication());
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
                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                edittext = new EditText(getActivity());
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

                            final AlertDialog.Builder warning = new AlertDialog.Builder(getActivity());
//
                            warning.setMessage("enter a valid number");
                           warning.show();
                            Toast.makeText(getActivity(),  "Enter a valid number",Toast.LENGTH_SHORT).show();
                        } else {
                            contact.setText(newContact);
                            user.setuContact(newContact);
                            Toast.makeText(getActivity(),  newContact,Toast.LENGTH_SHORT).show();
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

                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setMessage("please enter new Password");
                alert.setTitle("New Password");
                edittext = new EditText(getActivity());
                edittext.setText(user.getuPassWord());
                alert.setView(edittext);

                alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //What ever you want to do with the value
                        String newPassword = edittext.getText().toString();
                        if(newPassword.equals(user.getuPassWord())){
                            final AlertDialog.Builder warning = new AlertDialog.Builder(getActivity());
//
                            warning.setMessage("Password remains unchanged");
                            warning.show();
                        }

                        password.setText(newPassword);
                        user.setuPassWord(newPassword);

                        Toast.makeText(getActivity(),  newPassword,Toast.LENGTH_SHORT).show();
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

        OnSwipeTouchListener swipe = new OnSwipeTouchListener(getActivity(),users, users.indexOf(user) );
        swipe.onSwipeLeft();

        return rootView;


    }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            super.onCreateOptionsMenu(menu, inflater);
            if (!loggedUser.equals(uId)) {
                menu.findItem(R.id.delete).setVisible(true);
                toolbar.setTitle("User Profile");

            }
            if (loggedUser.equals("admin"))
                menu.findItem(R.id.edit).setVisible(true);

        }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.delete){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                        Toast.makeText(getActivity(), "Couldn't delete !!", Toast.LENGTH_LONG).show();
                    }
                    Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_LONG).show();
                    
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                    //no action
                    Toast.makeText(getActivity(), "no action", Toast.LENGTH_LONG).show();
                }
            });
            // Create the AlertDialog object and return it
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if(item.getItemId() == R.id.edit) {
            Log.i(TAG + "edit profile", "im editting" + user.getUserId());
            if (loggedUser.equals("admin")) {
                Intent in = new Intent(getActivity(), AddUserActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                Bundle args = new Bundle();
                args.putSerializable("chatobj", (Serializable) user);
                in.putExtra("DATA", args);
                startActivityForResult(in, 2);
            }
        }

        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2){

            uId = getActivity().getIntent().getStringExtra("id");
            setUserData();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(loggedUser.equals(uId)) {
            try {
                uMethod.updateUser(user);
                databaseReference.child(user.getUserId()).setValue(user);
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
