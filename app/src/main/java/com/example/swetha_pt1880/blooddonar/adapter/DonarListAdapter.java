package com.example.swetha_pt1880.blooddonar.adapter;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swetha_pt1880.blooddonar.R;
import com.example.swetha_pt1880.blooddonar.activity.DonarProfile;
import com.example.swetha_pt1880.blooddonar.activity.HomeActivity;
import com.example.swetha_pt1880.blooddonar.database.Database;
import com.example.swetha_pt1880.blooddonar.database.Donar;
import com.example.swetha_pt1880.blooddonar.database.DonarDBMethods;
import com.example.swetha_pt1880.blooddonar.fragment.DonarListFragment;
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
import java.util.List;

/**
 * Created by swetha-pt1880 on 16/1/18.
 */

public class DonarListAdapter extends RecyclerView.Adapter<DonarListAdapter.ViewHolder> implements Filterable , Serializable{

    private ArrayList<Donar> donars = new ArrayList<Donar>();
    private ArrayList<Donar> filteredDonar = new ArrayList<Donar>();
    public View donarView;
    private Context context;
    DonarDBMethods dMethod ;
    Database db;
    private DonarListAdapter adapter;
    private  DonarListFragment dFragment = new DonarListFragment();
    int age;
    String refine;
    static  int update = 0;
    String TAG = "DonarListAdapter";
    String userId;

    DatabaseReference databaseReference;

    SessionManagement sessionManagement;




    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView donar_blood;
        public TextView donar_name;
        public View layout;
        public TextView dage;


        public ViewHolder(View v) {
            super(v);
            layout = v;
            donar_name = (TextView) v.findViewById(R.id.dlName);
            dage = (TextView) v.findViewById(R.id.dlAge);
            donar_blood= (TextView) v.findViewById(R.id.dlBlood);
        }
    }



    // Provide a suitable constructor (depends on the kind of dataset)
    public DonarListAdapter(Context donarDetails, ArrayList<Donar> donarList) {
        this.context = donarDetails;
        this.donars = donarList;
        filteredDonar = donarList;
        this.adapter = this;
        db = new Database(context);
        this.dMethod = new DonarDBMethods(context);
        sessionManagement = new SessionManagement(context);
        databaseReference = FirebaseDatabase.getInstance().getReference("Donars");

        update = 0;
    }
    @Override
    public DonarListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        donarView = inflater.inflate(R.layout.donar_row_layout, parent, false);
        DonarListAdapter.ViewHolder vh = new DonarListAdapter.ViewHolder(donarView);
        return vh;
    }



    public void onBindViewHolder(final DonarListAdapter.ViewHolder holder, final int position) {
        // set the data
        Log.i("filteredDonarData ", filteredDonar.toString());
        final Donar donar = filteredDonar.get(position);
        final String name = donar.getdName();
        Log.i(TAG + " populating adapter " , name );
        holder.donar_blood.setText( "Blood Group : " +donar.getdBloodType());
        holder.donar_name.setText(donar.getdName());
        age = db.getAge(donar.getdDOB());
        Log.i(TAG + "age",  +age+ "" );
        try {
            holder.dage.setText( "Age : " +age);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
        }

        //long press delete
        donarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Donar donar = filteredDonar.get(position);
                Intent in = new Intent(context, DonarProfile.class);
                Log.i(TAG +"did, age ", " " + donar);
                Bundle args = new Bundle();
                args.putSerializable("chatobj",(Serializable)donar);
                in.putExtra("DATA",args);
                context.startActivity(in);

            }
        });


        donarView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                HashMap<String, String> user = sessionManagement.getUserDetails();
                if (user.get(SessionManagement.KEY_ID) != (null)) {

                    HashMap<String, String> loginUser = sessionManagement.getUserDetails();
                    userId = loginUser.get(SessionManagement.KEY_ID);

                    if (userId.equals("admin")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Do you want to delete " + donars.get(position).getdName() + " ?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                try {
                                    dMethod.delDonar(donars.get(position).getdContact());
                                    Query query = databaseReference.child(donars.get(position).getdContact() + "");
                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
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
                                donars.remove(position);
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
                }
                return true;

            }
        });


    }

    @Override
    public int getItemCount() {
        return donars.size();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                Log.i(TAG + "filter " , charString + "null");
                refine = dFragment.getSelectedRefine();
                if (charString.isEmpty()) {
                    Log.i(TAG + "empty " , charString + "null");
                    filteredDonar = donars;
                } else {
                    Log.i(TAG + " query " , charString + filteredDonar);

                    ArrayList<Donar> filtered = new ArrayList<>();
                    for (Donar donar : donars) {
                        Log.i(TAG + " query " , charString + donars);

                        if (refine.equals("name")){
                            if (donar.getdName().toLowerCase().contains(charString.toLowerCase())) {
                                filtered.add(donar);
                                Log.i(TAG + "query add" , donar.getdName());
                            }
                        }else if(refine.equals("blood")){
                            if (donar.getdBloodType().toLowerCase().contains(charString.toLowerCase())) {
                                filtered.add(donar);
                                Log.i(TAG + "query add" , donar.getdName());
                            }
                        }else if(refine.equals("age")){
                            if (db.getAge(donar.getdDOB()) == Integer.valueOf(charString)) {
                                filtered.add(donar);
                                Log.i(TAG + "query add" , donar.getdName());
                            }
                        }else if(refine.equals("city")){
                            if (donar.getdCurrentLoc().toLowerCase().contains(charString.toLowerCase())) {
                                filtered.add(donar);
                                Log.i(TAG + "query add" , donar.getdName());
                            }
                        }

                    }
                    //filteredDonar.clear();
                    Log.i("filteredDonar  ", filteredDonar.toString() + "null");
                    filteredDonar = filtered;
                    Log.i("filteredDonar  ", filteredDonar.toString() + "null");
                    adapter.notifyDataSetChanged();
                    update = 1;

                    Log.i("filtered  not null ", filteredDonar.toString());

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredDonar;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredDonar = (ArrayList<Donar>) filterResults.values;
                Log.i(TAG + " filtered Donar", filteredDonar.toString());
                 adapter.notifyDataSetChanged();

            }
        };
    }

    public ArrayList<Donar> getFilteredDonar(){
            return filteredDonar;
    }


    public void removeItem(int position) {
        filteredDonar.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Donar item, int position) {
        filteredDonar.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }


}




