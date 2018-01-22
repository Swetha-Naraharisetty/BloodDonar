package com.example.swetha_pt1880.blooddonar.fragment;

import android.app.Fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.swetha_pt1880.blooddonar.R;
import com.example.swetha_pt1880.blooddonar.activity.HomeActivity;
import com.example.swetha_pt1880.blooddonar.adapter.DonarListAdapter;
import com.example.swetha_pt1880.blooddonar.database.Database;
import com.example.swetha_pt1880.blooddonar.database.Donar;
import com.example.swetha_pt1880.blooddonar.database.DonarDBMethods;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by swetha-pt1880 on 18/1/18.
 */

public class DonarListFragment extends Fragment {

    List<Integer> ids= new ArrayList<>();
    List<String> names= new ArrayList<>();
    List<String> blood= new ArrayList<>();
    public ArrayList<Donar> donarDetails = new ArrayList<>();
    public Donar donarinstance = new Donar();
    DonarDBMethods dMethod ;
    private DonarListAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    String TAG = "DonarListFragment";
    View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dMethod = new DonarDBMethods(getActivity());
        view = inflater.inflate(R.layout.donar_fragment, container, false);
        //view.setVisibility(View.VISIBLE);

        recyclerView = (RecyclerView) view.findViewById(R.id.donarList);

        recyclerView.setHasFixedSize(true);
        //recyclerView.setTextFilterEnabled(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        donarDetails = dMethod.getDonarsList();
        mAdapter = new DonarListAdapter(getActivity(), donarDetails);
        adapter = new DonarListAdapter(getActivity(), donarDetails);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        ((HomeActivity)getActivity()).setFragmentRefreshListener(new HomeActivity.FragmentRefreshListener() {
            @Override
            public void onRefresh(ArrayList<Donar> filteredData) {
                Log.i(TAG + " onRefresh", filteredData.toString());
                mAdapter = new DonarListAdapter(getActivity(),filteredData);
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                // Refresh Your Fragment
            }
        });

        return view;

    }
}
