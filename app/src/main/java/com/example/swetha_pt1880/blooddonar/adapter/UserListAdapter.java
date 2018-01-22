package com.example.swetha_pt1880.blooddonar.adapter;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swetha_pt1880.blooddonar.R;
import com.example.swetha_pt1880.blooddonar.activity.HomeActivity;
import com.example.swetha_pt1880.blooddonar.database.UserDBMethods;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by swetha-pt1880 on 16/1/18.
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    private List<String> userIds;
    private List<String> userNames;
    private List<String> userPrivs;
    public View rowView;
    private Context context;
    UserDBMethods uMethod ;
    private  UserListAdapter adapter;

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

    public void add(int position, String item) {
        userIds.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        userIds.remove(position);
        userNames.remove(position);
        userPrivs.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public UserListAdapter(Context userDetails, List<String> userId, List<String> userName, List<String> userPriv) {
        context = userDetails;
        userIds = userId;
        userNames = userName;
        userPrivs = userPriv;
        this.adapter = this;
        uMethod = new UserDBMethods(context);
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
        final String id = userIds.get(position);
        final String name = userNames.get(position);
        final String priv = userPrivs.get(position);
        holder.uid.setText(id);
        holder.uname.setText(name);

        if(priv.equals("zero")){
            holder.grant.setText("Grant");
            holder.grant.setTextColor(-16711936);
        } else if(priv.equals("one")){
            holder.grant.setText("Revoke");
            holder.grant.setTextColor( -65536 );

        }

        holder.grant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                if(holder.grant.getText().equals("Grant")){
                    // grant the  priviledges

                        uMethod.updatePreviledge(userIds.get(position), "Grant");
                        userPrivs.set(position,"one");
                    adapter.notifyDataSetChanged();
                    Toast.makeText(context, "Granted", Toast.LENGTH_LONG).show();
                }else if(holder.grant.getText().equals("Revoke")){
                    //revoke the priviledges
                    uMethod.updatePreviledge(userIds.get(position), "Revoke");
                    userPrivs.set(position,"zero");
                    adapter.notifyDataSetChanged();
                    Toast.makeText(context, "Revoked", Toast.LENGTH_LONG).show();
                }
                } catch (SQLException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                }
                
                Intent in = new Intent(context, HomeActivity.class);
                context.startActivity(in);
            }
        });


        //long press delete
       rowView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               AlertDialog.Builder builder = new AlertDialog.Builder(context);
               builder.setMessage("Do you want to delete " + userIds.get(position) + " ?");
               builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       //delete the user from the database
                       try {
                           uMethod.delUser(userIds.get(position));
                       } catch (SQLException e) {
                           e.printStackTrace();
                       }
                       remove(position);
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
        return userIds.size();
    }
}
