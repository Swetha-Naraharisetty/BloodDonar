package com.example.swetha_pt1880.blooddonar.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
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
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swetha_pt1880.blooddonar.R;
import com.example.swetha_pt1880.blooddonar.adapter.RecyclerUserTouchHelper;
import com.example.swetha_pt1880.blooddonar.adapter.UserListAdapter;
import com.example.swetha_pt1880.blooddonar.adapter.CursorAdapterUser;
import com.example.swetha_pt1880.blooddonar.contentProvider.UserContentProvider;
import com.example.swetha_pt1880.blooddonar.database.Database;
import com.example.swetha_pt1880.blooddonar.database.UserDBMethods;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;


public class UserListFragment extends Fragment  implements  RecyclerUserTouchHelper.RecyclerItemTouchHelperListener, LoaderManager.LoaderCallbacks<Cursor> {

    // TODO: Rename parameter arguments, choose names that match
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    UserContentProvider cp = new UserContentProvider(getActivity());
    private RecyclerView.LayoutManager layoutManager;
    static String selectedRefine = "uname";
    Context context;
    String[] projection = {Database.userId, Database.uName, Database.uDOB, Database.uGender, Database.uContact, Database.uPassword, Database.uPriviledge, Database.uDonar};
    String[] selArgs = {"" +new String("admin")};
    String selection =  Database.userId + " != ? ";
    String sort = Database.uName;
    UserDBMethods uMethod ;//= new UserDBMethods(getActivity());
    UserListAdapter adapter ;
    private MenuItem mSearchAction;
    private SearchView searchView;
    private android.app.ActionBar actionBar;
    FrameLayout frameLayout;
    private boolean isSearchOpened = false;
    String TAG = "UserListFragment";
    View view;
    Menu menu;
    SwipeRefreshLayout refreshLayout;
    Handler handler = new Handler();
    ContentResolver contentResolver;
    CursorAdapterUser cursorAdapterUser;
    Cursor cursor;
    TextView no_results;
    private static final String AUTHORITY =
            UserContentProvider.class.getCanonicalName();

    public static final Uri CONTENT_URI_DETAIL_ITEM =
            Uri.parse("content://" + AUTHORITY + "/Users");

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
        frameLayout = (FrameLayout) view.findViewById(R.id.user_frame_frag);
        recyclerView = (RecyclerView) view.findViewById(R.id.userList);
        no_results = (TextView) view.findViewById(R.id.no_results_found);
        recyclerView.setHasFixedSize(true);
        //recyclerView.setTextFilterEnabled(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        DividerItemDecoration decoration = new DividerItemDecoration(getActivity(), VERTICAL);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        refreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.user_refresh);
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
                cursor  = uMethod.query(projection, selection, selArgs, sort);
                cursorAdapterUser.setData(cursor);
            }
        });
        refreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);



        context = getActivity().getApplicationContext();
        contentResolver = context.getContentResolver();
        recyclerView.setAdapter(cursorAdapterUser);

        cursorAdapterUser = new CursorAdapterUser(getActivity(), null);

        recyclerView.setAdapter(cursorAdapterUser);
        getActivity().getSupportLoaderManager().initLoader(1, null,this);




//        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerUserTouchHelper(0, ItemTouchHelper.LEFT, this);
//        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
//
//
//
//
//        ItemTouchHelper.SimpleCallback itemTouchHelperCallback1 = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP) {
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//
//                // Row is swiped from recycler view
//                // remove it from adapter
//            }
//
//            @Override
//            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//            }
//        };
//
//        // attaching the touch helper to recycler view
//        new ItemTouchHelper(itemTouchHelperCallback1).attachToRecyclerView(recyclerView);
        return view;
    }

    public void onRefresh(Cursor cursor1) {
        Log.i(TAG + " onRefresh ", cursor1.getCount()+ "");
        if (cursor1 == null)
            Toast.makeText(getActivity(), "No Users Found", Toast.LENGTH_SHORT).show();
       cursorAdapterUser.setData(cursor1);
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
                params.width=100;
                searchView.setLayoutParams(params);
                menu.setGroupVisible(R.id.refine_user_group, false);
                return false;
            }
        });



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String query) {

                Log.i(TAG + " onsubmit ", query);
                 cursorAdapterUser.getFilter().filter(query);
                 //recyclerView.getRecycledViewPool().clear();
                 cursorAdapterUser.notifyDataSetChanged();
//                cursor = cursorAdapterUser.getData();
//                Log.i(TAG + " filter  c ", cursor.getCount() + " ");

                if(query.isEmpty())
                    cursor = uMethod.query(projection, selection, selArgs, sort);
               // onRefresh(cursor);
                 cursorAdapterUser.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String query) {
                // filter recycler view when text is changed

                Log.i(TAG + " onChange ", query);
                 cursorAdapterUser.getFilter().filter(query);
                //recyclerView.getRecycledViewPool().clear();
                 cursorAdapterUser.notifyDataSetChanged();
//                handler.removeCallbacksAndMessages(null);
//
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//               cursor = cursorAdapterUser.getData();
//                Log.i(TAG + " filter  c ", cursor.getCount() + " ");
                    if(query.isEmpty()){

                        no_results.setVisibility(View.VISIBLE);

                    }
//                    }
//                }, 100);
              return true;
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


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof UserListAdapter.ViewHolder) {
            // get the removed item name to display it in snack bar


            // remove the item from recycler view
            //mAdapter.(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(frameLayout,  " removed !", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                  //  mAdapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String queryUri = UserContentProvider.CONTENT_URI_DETAIL_ITEM.toString();

        return new CursorLoader(context, Uri.parse(queryUri), projection,selection ,selArgs , sort);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        cursorAdapterUser.setData(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        cursorAdapterUser.setData(null);
    }
//


}
