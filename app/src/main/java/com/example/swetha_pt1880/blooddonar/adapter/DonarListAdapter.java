package com.example.swetha_pt1880.blooddonar.adapter;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.swetha_pt1880.blooddonar.R;
import com.example.swetha_pt1880.blooddonar.activity.DonarProfile;
import com.example.swetha_pt1880.blooddonar.activity.HomeActivity;
import com.example.swetha_pt1880.blooddonar.database.Donar;
import com.example.swetha_pt1880.blooddonar.database.DonarDBMethods;
import com.example.swetha_pt1880.blooddonar.fragment.DonarListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swetha-pt1880 on 16/1/18.
 */

public class DonarListAdapter extends RecyclerView.Adapter<DonarListAdapter.ViewHolder> implements Filterable {

    private ArrayList<Donar> donars = new ArrayList<Donar>();
    private ArrayList<Donar> filteredDonar = new ArrayList<Donar>();
    public View donarView;
    private Context context;
    DonarDBMethods dMethod ;
    private DonarListAdapter adapter;
    int age;
    static  int update = 0;
    String TAG = "DonarListAdapter";
    private DonarListFragment dFragment = new DonarListFragment();





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
        this.dMethod = new DonarDBMethods(context);
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
        Donar donar = filteredDonar.get(position);
        final int id = donar.getdId();
        final String name = donar.getdName();
        Log.i(TAG + " populating adapter " , name );
        holder.donar_blood.setText( "Blood Group : " +donar.getdBloodType());
        holder.donar_name.setText("Donar Name : " +donar.getdName());
        age = dMethod.getAge(donar.getdId());
        Log.i(TAG + "age",  +age+ "" + id);
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
                final int id = donar.getdId();
                Intent in = new Intent(context, DonarProfile.class);
                Log.i(TAG +"did, age ",id+ " " + age);
                in.putExtra("id", id);
                in.putExtra("age", age);
                context.startActivity(in);

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
                if (charString.isEmpty()) {
                    Log.i(TAG + "empty " , charString + "null");
                    filteredDonar = donars;
                } else {
                    ArrayList<Donar> filtered = new ArrayList<>();
                    for (Donar donar : donars) {
                        Log.i(TAG + " query " , charString);
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (donar.getdName().toLowerCase().contains(charString.toLowerCase()) || donar.getdBloodType().toLowerCase().contains(charString.toLowerCase())) {
                            filtered.add(donar);
                            Log.i(TAG + "query add" , donar.getdName());
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





}




