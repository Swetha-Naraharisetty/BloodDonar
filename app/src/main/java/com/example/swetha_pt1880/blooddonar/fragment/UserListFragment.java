package com.example.swetha_pt1880.blooddonar.fragment;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.swetha_pt1880.blooddonar.R;
import com.example.swetha_pt1880.blooddonar.adapter.UserListAdapter;
import com.example.swetha_pt1880.blooddonar.database.UserDBMethods;

import java.util.ArrayList;
import java.util.List;


public class UserListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    List<String> ids= new ArrayList<>();
    List<String> names= new ArrayList<>();
    List<String> privs= new ArrayList<>();
    UserDBMethods uMethod ;//= new UserDBMethods(getActivity());

    String TAG = "UserListFragment";
    View view;
    //private OnFragmentInteractionListener mListener;

    public UserListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view = inflater.inflate(R.layout.fragment_user_list, container, false);
        uMethod = new UserDBMethods(getActivity());
        view.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView) view.findViewById(R.id.userList);

        recyclerView.setHasFixedSize(true);
        //recyclerView.setTextFilterEnabled(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        Cursor cursor = uMethod.getUsers();
        if(cursor.getCount() > 0) {
            Log.i( TAG + "count", String.valueOf(cursor.getCount()));

            if (cursor.moveToFirst()) {

                do {
                    Log.i(TAG + "id", cursor.getString(0));
                    ids.add(cursor.getString(0));
                    names.add(cursor.getString(1));
                    privs.add(cursor.getString(6));


                } while (cursor.moveToNext());

            }
        }

        mAdapter = new UserListAdapter(getActivity(),ids, names, privs);
        recyclerView.setAdapter(mAdapter);


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
