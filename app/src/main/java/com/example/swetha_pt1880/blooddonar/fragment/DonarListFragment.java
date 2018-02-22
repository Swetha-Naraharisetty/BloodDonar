package com.example.swetha_pt1880.blooddonar.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.swetha_pt1880.blooddonar.R;
import com.example.swetha_pt1880.blooddonar.adapter.DonarListAdapter;
import com.example.swetha_pt1880.blooddonar.database.Donar;
import com.example.swetha_pt1880.blooddonar.database.DonarDBMethods;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;


/**
 * Created by swetha-pt1880 on 18/1/18.
 */

public class DonarListFragment extends Fragment {


    public ArrayList<Donar> donarDetails = new ArrayList<>();
    public ArrayList<Donar> donars = new ArrayList<>();
    public ArrayList<Donar> filteredDonars = new ArrayList<>();
    DonarDBMethods dMethod ;
    private DonarListAdapter Vadapter;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    String TAG = "DonarListFragment";
    static String selectedRefine= "name";
    View view;
    Menu menu;
    Handler mHandler;

    private MenuItem mSearchAction;
    private SearchView searchView;
    private android.app.ActionBar actionBar;
    CoordinatorLayout coordinatorLayout;
    private boolean isSearchOpened = false;
    SwipeRefreshLayout refreshLayout;
    DatabaseReference donarRefer;
    DonarDBMethods dbMethods;
    TextView no_results;
    Context context;
   ActionBar.LayoutParams params ;//= new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT);


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dMethod = new DonarDBMethods(getActivity());
        view = inflater.inflate(R.layout.donar_fragment, container, false);
        //view.setVisibility(View.VISIBLE);
        coordinatorLayout = view.findViewById(R.id.coordinator);
        recyclerView = (RecyclerView) view.findViewById(R.id.donarList);
        donarRefer = FirebaseDatabase.getInstance().getReference("Donars");
        dbMethods = new DonarDBMethods(getActivity());
        recyclerView.setHasFixedSize(true);
        no_results = (TextView) view.findViewById(R.id.no_results_found);
        context = getActivity();

        mHandler = new Handler();

        //recyclerView.setTextFilterEnabled(true);
        // use a linear layout manager

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(getActivity(), VERTICAL);
        recyclerView.addItemDecoration(decoration);
        donarDetails = dMethod.getDonarsList();
        params = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT);



//        Collections.sort(donarDetails, new Comparator<Donar>() {
//            public int compare(Donar v1, Donar v2) {
//                Log.i(TAG + "sort", v1.getdName()+ v2.getdName());
//                return v1.getdName().toLowerCase().compareTo(v2.getdName().toLowerCase());
//            }
//        });

        refreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.donar_refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                Log.i(TAG +"refresh true","refreshing ..");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        Log.i(TAG +"refresh false","refreshing .." );
                        donars.clear();
                        donarRefer.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Log.i(TAG + "data shot ", dataSnapshot.getKey());
                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    Log.i(TAG + "data shot ", data.getKey());
                                    Donar donar = data.getValue(Donar.class);
                                    Log.i(TAG + " add donar ", donar.getdName() + "  ");
                                    if(donar != null)
                                        donars.add(donar);
                                }

//                                try {
                                    // db.delete();
                                Log.i(TAG + "populate donar", "populate" + donars);
                                donarDetails.clear();
                                donarDetails.addAll(donars);
                                Log.e("swe",donarDetails+"\n"+",,,");
//                                Collections.sort(donarDetails, new Comparator<Donar>() {
//                                    public int compare(Donar v1, Donar v2) {
//                                        Log.i(TAG + "sort", v1.getdName()+ v2.getdName());
//                                        return v1.getdName().toLowerCase().compareTo(v2.getdName().toLowerCase());
//                                    }
//                                });
                                if(!((Activity) context).isFinishing()) {

                                    adapter = new DonarListAdapter((Activity) context, donarDetails);
                                    recyclerView.setAdapter(adapter);
                                }
                               adapter.notifyDataSetChanged();
//                                } catch (SQLException e) {
//                                    Toast.makeText(getActivity(), "couldnt push the data", Toast.LENGTH_SHORT).show();
//                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                }, 5000);
            }
        });
        refreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);

        Vadapter = new DonarListAdapter((Activity) context, donarDetails);
        adapter = new DonarListAdapter((Activity) context, donarDetails);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return view;

    }


    public void onRefresh(ArrayList<Donar> filteredData) {
        Log.i(TAG + " onRefresh", filteredData.toString());
        adapter = new DonarListAdapter(getActivity(),filteredData);
        if (filteredData.isEmpty())
        {
            Log.i(TAG + "no results", "enabled");
          no_results.setVisibility(View.VISIBLE);
        }else
            no_results.setVisibility(View.INVISIBLE);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        // Refresh Your Fragment
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home, menu);
        this.menu = menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.refine_age:
                selectedRefine = "age";
                Log.i(TAG + "refine", selectedRefine);
                item.setChecked(true);
                break;
            case R.id.refine_blood:
                selectedRefine = "blood";
                Log.i(TAG + "refine", selectedRefine);
                item.setChecked(true);
                break;
            case R.id.refine_city:
                selectedRefine = "city";
                Log.i(TAG + "refine", selectedRefine);
                item.setChecked(true);
                break;
            case R.id.refine_name:
                selectedRefine = "name";
                Log.i(TAG + "refine", selectedRefine);
                item.setChecked(true);
                break;


            case R.id.search:
                handleMenuSearch();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(final Menu menu) {

        super.onPrepareOptionsMenu(menu);
        mSearchAction = menu.findItem(R.id.search);
        searchView = (SearchView) mSearchAction.getActionView();


        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                params.width=700;
                searchView.setLayoutParams(params);
                menu.findItem(R.id.search).expandActionView();
                Log.i("xdgfdhhjnn", "gfcgfgdryr5y");

                menu.setGroupVisible(R.id.refine_blood_group, true);
                menu.setGroupVisible(R.id.refine_user_group, false);
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                params.width=100;
                searchView.setLayoutParams(params);
                menu.setGroupVisible(R.id.refine_blood_group, false);
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                Log.i(TAG + " onsubmit ", query);
                Vadapter.getFilter().filter(query);
                    filteredDonars = (Vadapter.getFilteredDonar());
                    Log.i(TAG + " filter s ", filteredDonars.toString());
                    if(query.isEmpty())
                        filteredDonars = donarDetails;
                   onRefresh(filteredDonars);

                Vadapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(final String query) {
                // filter recycler view when text is changed
                Log.i(TAG + " onChange ", query);
                Vadapter.getFilter().filter(query);
                mHandler.removeCallbacksAndMessages(null);

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Put your call to the server here (with mQueryString)
                        filteredDonars= (Vadapter.getFilteredDonar());
                        Log.i(TAG + " filter  c ", filteredDonars.toString());
                        if(query.isEmpty())
                            filteredDonars = donarDetails;
                        onRefresh(filteredDonars);

                        Vadapter.notifyDataSetChanged();
                    }
                }, 100);


                return true;
            }
        });
    }

    private void handleMenuSearch() {
        actionBar = getActivity().getActionBar(); //get the actionbar
        params.width=100;

        if(isSearchOpened){ //test if the search is open
            if(actionBar != null)
            {
                actionBar.setDisplayShowCustomEnabled(false);   //disable a custom view inside the actionbar
                actionBar.setDisplayShowTitleEnabled(true);     //show the title in the action bar
            }

            // add the search icon in the action bar
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_search));

            isSearchOpened = false;
        } else { // open the search entry
            if(actionBar != null)
            {
                actionBar.setDisplayShowCustomEnabled(true); //enable it to display a
                // custom view in the action bar.
                // action.setCustomView(R.layout.search_bar);//add the custom view

                actionBar.setDisplayShowTitleEnabled(false); //hide the title
            }
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_search));

            isSearchOpened = true;
        }
    }

    public String getSelectedRefine(){
        return  selectedRefine;
    }


    }
