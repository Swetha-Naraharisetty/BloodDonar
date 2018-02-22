package com.example.swetha_pt1880.blooddonar.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.swetha_pt1880.blooddonar.R;
import com.example.swetha_pt1880.blooddonar.activity.ProfileActivity;
import com.example.swetha_pt1880.blooddonar.activity.UserScreenSlideActivity;
import com.example.swetha_pt1880.blooddonar.database.User;
import com.example.swetha_pt1880.blooddonar.database.UserDBMethods;
import com.example.swetha_pt1880.blooddonar.fragment.ProfileFragment;
import com.example.swetha_pt1880.blooddonar.fragment.UserListFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;

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
    Activity activity;

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView uid;
        public TextView uname;
        public View layout;
        public ImageButton delete;
        public RelativeLayout viewBackground, viewForeground;


        public ViewHolder(View v) {
            super(v);
            layout = v;
            uid = (TextView) v.findViewById(R.id.luid);
            uname = (TextView) v.findViewById(R.id.luname);
            viewBackground = v.findViewById(R.id.view_background);
            viewForeground = v.findViewById(R.id.view_foreground);
            //delete = (ImageButton)v.findViewById(R.id.delete_user);
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
    public UserListAdapter(Activity userDetails, ArrayList<User> users) {
        context = userDetails;
        this.users = users;
        this.activity = userDetails;
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

        holder.uid.setText("User id : " +filteredUsers.get(position).getUserId());
        holder.uname.setText(filteredUsers.get(position).getuName());


//        holder.delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//            }
//        });


        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 Intent in = new Intent(context, UserScreenSlideActivity.class);
                 in.putExtra("id", filteredUsers.get(position).getUserId());
                 in.putExtra("position", position);
                in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                 context.startActivity(in);
                activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

//
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
