package com.example.swetha_pt1880.blooddonar.fragment;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
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

import com.example.swetha_pt1880.blooddonar.R;
import com.example.swetha_pt1880.blooddonar.activity.LoginActivity;
import com.example.swetha_pt1880.blooddonar.adapter.DonarListAdapter;
import com.example.swetha_pt1880.blooddonar.adapter.UserListAdapter;

import com.example.swetha_pt1880.blooddonar.database.Donar;
import com.example.swetha_pt1880.blooddonar.database.User;
import com.example.swetha_pt1880.blooddonar.database.UserDBMethods;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;


public class UserListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
   static String selectedRefine = "uname";

    ArrayList<User> user = new ArrayList<>();
    ArrayList<User> filteredUsers = new ArrayList<>();
    User newUser = new User();
    UserDBMethods uMethod ;//= new UserDBMethods(getActivity());
    UserListAdapter adapter ;
    private MenuItem mSearchAction;
    private SearchView searchView;
    private android.app.ActionBar actionBar;
    CoordinatorLayout coordinatorLayout;
    private boolean isSearchOpened = false;
    String TAG = "UserListFragment";
    View view;
    Menu menu;
    SwipeRefreshLayout refreshLayout;
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
        DividerItemDecoration decoration = new DividerItemDecoration(getActivity(), VERTICAL);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setLayoutManager(layoutManager);
        refreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swiperefresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                Log.i(TAG +"refresh true","refreshing ..");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        Log.i(TAG +"refresh false","refreshing ..");
                    }
                }, 1000);
            }
        });
        refreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        user = uMethod.getUserList();
        //user = ((MyApplication) getActivity().getApplication()).getUsers();
        for(User u : user){
            if(u.getUserId().equals("admin")) {
                newUser = u;
                break;
            }
        }
        Log.i(TAG + " nothing ", newUser.getUserId() + "null");
        user.remove(newUser);
        adapter = new UserListAdapter(getActivity(), user);
        mAdapter = new UserListAdapter(getActivity(),user);




        recyclerView.setAdapter(mAdapter);
        return view;
    }

    public void onRefresh(ArrayList<User> filteredData) {
        Log.i(TAG + " onRefresh ", filteredData.toString());
        mAdapter = new UserListAdapter(getActivity(),filteredData);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
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
        int id = item.getItemId();
        switch (id)
        {
            case R.id.refine_uid:
                selectedRefine = "uid";
                Log.i(TAG + "refine", selectedRefine);
                item.setChecked(true);
                break;
            case R.id.search:
                handleMenuSearch();
                break;


            case R.id.refine_uname:
                selectedRefine = "uname";
                Log.i(TAG + "refine", selectedRefine);
                item.setChecked(true);
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
                Log.i("xdgfdhhjnn", "gfcgfgdryr5y");
                params.width=700;
                searchView.setLayoutParams(params);
                menu.findItem(R.id.search).expandActionView();

                menu.setGroupVisible(R.id.refine_blood_group, false);
                menu.setGroupVisible(R.id.refine_user_group, true);
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                params.width=200;
                searchView.setLayoutParams(params);
                menu.setGroupVisible(R.id.refine_user_group, false);
                return false;
            }
        });



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String query) {

                Log.i(TAG + " onsubmit ", query);
                 adapter.getFilter().filter(query);

                filteredUsers = ( adapter.getFilteredUsers());
                Log.i(TAG + " filter s ", filteredUsers.toString());
                if(query.isEmpty())
                    filteredUsers = user;
                onRefresh(filteredUsers);
                 adapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed

                Log.i(TAG + " onChange ", query);
                 adapter.getFilter().filter(query);
                filteredUsers= ( adapter.getFilteredUsers());
                Log.i(TAG + " filter  c ", filteredUsers.toString());
                if(query.isEmpty())
                    filteredUsers = user;
                onRefresh(filteredUsers);

                 adapter.notifyDataSetChanged();
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

            // add the search icon in the action bar
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_search));

            isSearchOpened = false;
        } else { // open the search entry
            if(actionBar != null)
            {
                actionBar.setDisplayShowCustomEnabled(true); //enable it to display a
                actionBar.setDisplayShowTitleEnabled(false); //hide the title
            }
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_search));

            isSearchOpened = true;
        }
    }

    public String getSelectedRefine(){
        return selectedRefine;
    }

}
