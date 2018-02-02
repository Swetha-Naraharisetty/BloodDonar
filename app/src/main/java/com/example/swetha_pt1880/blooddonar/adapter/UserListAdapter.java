package com.example.swetha_pt1880.blooddonar.adapter;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swetha_pt1880.blooddonar.R;
import com.example.swetha_pt1880.blooddonar.activity.HomeActivity;
import com.example.swetha_pt1880.blooddonar.database.User;
import com.example.swetha_pt1880.blooddonar.database.User;
import com.example.swetha_pt1880.blooddonar.database.UserDBMethods;
import com.example.swetha_pt1880.blooddonar.fragment.UserListFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by swetha-pt1880 on 16/1/18.
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> implements Filterable, Serializable {
   
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<User> filteredUsers = new ArrayList<>();
    public View rowView;
    private Context context;
    UserDBMethods uMethod ;
   public   UserListAdapter adapter;
   private UserListFragment uFragment = new UserListFragment();
    String TAG = "UserListAdapter";
    DatabaseReference databaseReference;
    String refine;

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView uid;
        public TextView uname;
        public View layout;
        public Button grant;


        public ViewHolder(View v) {
            super(v);
            layout = v;
            uid = (TextView) v.findViewById(R.id.luid);
            uname = (TextView) v.findViewById(R.id.luname);
            grant = (Button)v.findViewById(R.id.lgrant);
        }
    }

    public void add(int position, User newUser) {
        filteredUsers.add(position, newUser);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        filteredUsers.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public UserListAdapter(Context userDetails, ArrayList<User> users) {
        context = userDetails;
        this.users = users;
        this.filteredUsers = users;
        this.adapter = this;
        uMethod = new UserDBMethods(context);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
    }
    @Override
    public UserListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        rowView = inflater.inflate(R.layout.user_row_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(rowView);
        return vh;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onBindViewHolder(final UserListAdapter.ViewHolder holder, final int position) {
        // set the data
        Log.i(TAG + "user id",filteredUsers.get(position).getUserId() );

        holder.uid.setText("Id : " +filteredUsers.get(position).getUserId());
        holder.uname.setText("Name : "+filteredUsers.get(position).getuName());

        if (users.get(position).getuPriviledge().equals("zero")) {
            holder.grant.setText("Grant");
            holder.grant.setTextColor(-16711936);
        } else if (users.get(position).getuPriviledge().equals("one")) {
            holder.grant.setText("Revoke");
            holder.grant.setTextColor(-65536);

        }


        holder.grant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure to "+holder.grant.getText() +" " + filteredUsers.get(position).getuName() + " Priviledges  ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (holder.grant.getText().equals("Grant")) {
                            // grant the  priviledges
                            filteredUsers.get(position).setuPriviledge("one");
                            adapter.notifyDataSetChanged();
                            Toast.makeText(context, "Granted", Toast.LENGTH_LONG).show();
                        } else if (holder.grant.getText().equals("Revoke")) {
                            //revoke the priviledges
                            filteredUsers.get(position).setuPriviledge("zero");
                            adapter.notifyDataSetChanged();
                            Toast.makeText(context, "Revoked", Toast.LENGTH_LONG).show();
                        }

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        //no action
                        Toast.makeText(context, "no action", Toast.LENGTH_LONG).show();
                    }
                });
                // Create the AlertDialog object and return it



                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.i(TAG + "on data changed", "firebase" + filteredUsers.get(position));
                        databaseReference.child(filteredUsers.get(position).getUserId()).setValue(filteredUsers.get(position));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.i(TAG + " failed", databaseError.getDetails());

                    }
                });

                Intent in = new Intent(context, HomeActivity.class);
                context.startActivity(in);
                android.app.AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        //long press delete
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to delete " + filteredUsers.get(position).getuName() + " ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        try {
                            uMethod.delUser(filteredUsers.get(position).getUserId());

                            Query query = databaseReference.child(filteredUsers.get(position).getUserId());
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
                            Toast.makeText(context, "Couldn't delete !!", Toast.LENGTH_LONG).show();
                        }
                        filteredUsers.remove(position);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(context, "Deleted", Toast.LENGTH_LONG).show();
                        Intent in = new Intent(context, HomeActivity.class);
                        context.startActivity(in);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        //no action
                        Toast.makeText(context, "no action", Toast.LENGTH_LONG).show();
                    }
                });
                // Create the AlertDialog object and return it
                android.app.AlertDialog dialog = builder.create();
                dialog.show();

            }
        });


    }

    @Override
    public int getItemCount() {
        return users.size();
    }



    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                Log.i(TAG + "filter " , charString + "null");
                refine = uFragment.getSelectedRefine();
                if (charString.isEmpty()) {
                    Log.i(TAG + "empty " , charString + "null");
                    filteredUsers = users;
                } else {
                    Log.i(TAG + " selected refine " , charString + "" + uFragment.getSelectedRefine());

                    ArrayList<User> filtered = new ArrayList<>();
                    for (User user : users) {
                        Log.i(TAG + " query " , charString + users);
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if(refine.equals("uname")){
                            if (user.getuName().toLowerCase().contains(charString.toLowerCase())) {
                                filtered.add(user);
                                Log.i(TAG + "query add" , user.getuName());
                            }
                        }
                        else if (refine.equals("uid")){
                            if (user.getUserId().toLowerCase().contains(charString.toLowerCase())) {
                                filtered.add(user);
                                Log.i(TAG + "query add" , user.getuName());
                            }
                        }

                    }
                    //filteredUsers.clear();
                    Log.i("filteredUsers fil  ", filtered.toString() + "null");
                    filteredUsers = filtered;
                    Log.i("filteredUsers  ", filteredUsers.toString() + "null");
                    adapter.notifyDataSetChanged();


                    Log.i("filtered  not null ", filteredUsers.toString());

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredUsers;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredUsers = (ArrayList<User>) filterResults.values;
               //Log.i(TAG + " filtered User", filteredUsers.toString());
                adapter.notifyDataSetChanged();

            }
        };
    }

    public ArrayList<User> getFilteredUsers(){
        Log.i(TAG + " filtered User", filteredUsers.toString());
        return filteredUsers;
    }


}
