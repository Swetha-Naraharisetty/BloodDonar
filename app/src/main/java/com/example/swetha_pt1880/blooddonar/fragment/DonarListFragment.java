package com.example.swetha_pt1880.blooddonar.fragment;

import android.app.Fragment;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.swetha_pt1880.blooddonar.R;
import com.example.swetha_pt1880.blooddonar.activity.HomeActivity;
import com.example.swetha_pt1880.blooddonar.activity.LoginActivity;
import com.example.swetha_pt1880.blooddonar.adapter.DonarListAdapter;

import com.example.swetha_pt1880.blooddonar.database.Database;
import com.example.swetha_pt1880.blooddonar.database.Donar;
import com.example.swetha_pt1880.blooddonar.database.DonarDBMethods;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;
import static android.support.v7.widget.LinearLayoutManager.HORIZONTAL;


/**
 * Created by swetha-pt1880 on 18/1/18.
 */

public class DonarListFragment extends Fragment {


    public ArrayList<Donar> donarDetails = new ArrayList<>();
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
    private MenuItem mSearchAction;
    private SearchView searchView;
    private android.app.ActionBar actionBar;
    CoordinatorLayout coordinatorLayout;
    private boolean isSearchOpened = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dMethod = new DonarDBMethods(getActivity());
        view = inflater.inflate(R.layout.donar_fragment, container, false);
        //view.setVisibility(View.VISIBLE);
        coordinatorLayout = view.findViewById(R.id.coordinator);
        recyclerView = (RecyclerView) view.findViewById(R.id.donarList);

        recyclerView.setHasFixedSize(true);

        //recyclerView.setTextFilterEnabled(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(getActivity(), VERTICAL);
        recyclerView.addItemDecoration(decoration);
        donarDetails = dMethod.getDonarsList();

        //donarDetails = ((MyApplication) getActivity().getApplication()).getDonars();

        Vadapter = new DonarListAdapter(getActivity(), donarDetails);
        adapter = new DonarListAdapter(getActivity(), donarDetails);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return view;

    }


    public void onRefresh(ArrayList<Donar> filteredData) {
        Log.i(TAG + " onRefresh", filteredData.toString());
        adapter = new DonarListAdapter(getActivity(),filteredData);
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
        final ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT);


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
                params.width=200;
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
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                Log.i(TAG + " onChange ", query);
               Vadapter.getFilter().filter(query);
                    filteredDonars= (Vadapter.getFilteredDonar());
                    Log.i(TAG + " filter  c ", filteredDonars.toString());
                    if(query.isEmpty())
                        filteredDonars = donarDetails;
                    onRefresh(filteredDonars);

                Vadapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    private void handleMenuSearch() {
        actionBar = getActivity().getActionBar(); //get the actionbar

        if(isSearchOpened){ //test if the search is open
            if(actionBar != null)
            {
                actionBar.setDisplayShowCustomEnabled(false);   //disable a custom view inside the actionbar
                actionBar.setDisplayShowTitleEnabled(true);     //show the title in the action bar
            }

            // hides the keyboard
//            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);

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
            // open the keyboard focused in the edtSearch
//            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.showSoftInput(etSearch, InputMethodManager.SHOW_IMPLICIT);
//            // add the close icon
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_search));

            isSearchOpened = true;
        }
    }

    public String getSelectedRefine(){
        return  selectedRefine;
    }
}
