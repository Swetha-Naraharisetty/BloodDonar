package com.example.swetha_pt1880.blooddonar.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swetha_pt1880.blooddonar.R;
import com.example.swetha_pt1880.blooddonar.activity.UserScreenSlideActivity;
import com.example.swetha_pt1880.blooddonar.database.Database;
import com.example.swetha_pt1880.blooddonar.database.User;
import com.example.swetha_pt1880.blooddonar.database.UserDBMethods;
import com.example.swetha_pt1880.blooddonar.fragment.UserListFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by swetha-pt1880 on 26/2/18.
 */

public class CursorAdapterUser extends  RecyclerView.Adapter<CursorAdapterUser.ViewHolder>  implements Filterable, Serializable {

    Cursor cursor, filterCursor;
    Context context;
    public View rowView;
    private ViewHolder holder;
    CursorAdapter cursorAdapter;
    UserDBMethods uMethod ;
    public CursorAdapterUser adapter;
    private UserListFragment uFragment = new UserListFragment();
    String TAG = "Cursor Adapter User";
    DatabaseReference databaseReference;
    String refine;




    public CursorAdapterUser(Context context, Cursor c) {
        this.context = context;
        this.cursor = c;
        this.filterCursor = c;
        this.adapter = this;
        uMethod = new UserDBMethods(context);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        cursorAdapter = new CursorAdapter(context, c, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                rowView = LayoutInflater.from(context)
                        .inflate(R.layout.user_row_layout, parent, false);
                return rowView;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {


            }


        };
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                Log.i(TAG + "filter " , charString + " ");
                refine = uFragment.getSelectedRefine();
                String[] projection = {Database.userId, Database.uName, Database.uDOB, Database.uGender, Database.uContact, Database.uPassword, Database.uPriviledge, Database.uDonar};
                if (charString.isEmpty())
                    filterCursor = cursor;
                 else {
                    Log.i(TAG + " selected refine ", charString + "" + uFragment.getSelectedRefine());
                    refine = uFragment.getSelectedRefine();
                    Cursor c = null;
                    if (refine.equals("uname")) {
                        String[] selArgs = {"" +new String("admin"), new String( "%" +charString.toLowerCase() + "%")};
                        c = uMethod.query(projection,Database.userId + " != ?  AND " +Database.uName.toLowerCase() + " LIKE  ?",selArgs , Database.uName);
                        c.moveToFirst();
                        Log.i(TAG + " filter  ====", c.getCount() + "");
                    } else if (refine.equals("uid")) {
                        String[] selArgs = {"" +new String("admin"), new String( "%" +charString.toLowerCase() + "%")};
                        c = uMethod.query(projection,Database.userId + " != ?  AND " +Database.userId.toLowerCase() + " LIKE  ?",selArgs , Database.uName);
                        c.moveToFirst();
                        Log.i(TAG + " filter  ====", c.getCount() + "");
                    }
                    filterCursor = c;

                }

                FilterResults filterResults = new FilterResults();
                 //setData(filterCursor);
                filterResults.values = filterCursor;
                cursor = filterCursor;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
               setData(cursor);

            }
        };
    }

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





    @Override
    public CursorAdapterUser.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = cursorAdapter.newView(context, cursorAdapter.getCursor(), parent);
        holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(CursorAdapterUser.ViewHolder holder, final int position) {
        if (!(cursor == null)) {

            cursor.moveToPosition(position);
            cursorAdapter.bindView(holder.itemView, context, cursor);
            Log.i(TAG + " bindview before ",cursor.getString(Database.uIdCN) );
            holder.uid.setText("User id : " + cursor.getString(Database.uIdCN));
            holder.uname.setText(cursor.getString(Database.uNameCN));
//                holder.uid.setText("User id : " + "swetha");
//                holder.uname.setText("SN");
            Log.i(TAG + " bindview",holder.uid.getText().toString() );

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                        Intent in = new Intent(context, UserScreenSlideActivity.class);
                        in.putExtra("id", cursor.getString(Database.uIdCN));
                        in.putExtra("position", position);
                        in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        context.startActivity(in);
                    }

            });
        } else
            Toast.makeText(context, "No Users Found", Toast.LENGTH_SHORT).show();

    }


    @Override
    public int getItemCount() {
        if(cursor == null)
            return 0;
        return cursor.getCount() ;
    }

    public void setData(Cursor cursor1) {
        cursor = null;
        cursor = cursor1;
        notifyDataSetChanged();
    }




}
