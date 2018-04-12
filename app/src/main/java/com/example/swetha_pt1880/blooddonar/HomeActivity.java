package com.example.swetha_pt1880.blooddonar.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swetha_pt1880.blooddonar.R;
import com.example.swetha_pt1880.blooddonar.adapter.DonarListAdapter;
import com.example.swetha_pt1880.blooddonar.database.Database;
import com.example.swetha_pt1880.blooddonar.database.Donar;
import com.example.swetha_pt1880.blooddonar.database.DonarDBMethods;
import com.example.swetha_pt1880.blooddonar.database.User;
import com.example.swetha_pt1880.blooddonar.database.UserDBMethods;
import com.example.swetha_pt1880.blooddonar.fragment.AddBottomSheet;
import com.example.swetha_pt1880.blooddonar.fragment.DonarListFragment;
import com.example.swetha_pt1880.blooddonar.fragment.UserListFragment;
import com.example.swetha_pt1880.blooddonar.session.SessionManagement;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    String TAG = "HomeActivity";
    FloatingActionButton fab;
    String credetial  = "";
    static String currentFrag = "";
    TextView login_user;

    ArrayList<Donar> donars = new ArrayList<Donar>();
    ArrayList<User> user = new ArrayList<>();

    ImageView icon;
    NavigationView navigationView;
    SessionManagement session;
    Menu itemNav;
    FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment frag = new Fragment();
    BottomSheetDialogFragment bottomSheetDialogFragment = new AddBottomSheet();
    Toolbar toolbar;
    DonarListAdapter dAdapter;
    DonarDBMethods dMethod ;
    UserDBMethods uMethod;
    SessionManagement sessionManagement;

    CoordinatorLayout coordinatorLayout;


    Database db = new Database(this);
    DatabaseReference dbRefer;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

       super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        session = new SessionManagement(getApplicationContext());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        coordinatorLayout = findViewById(R.id.coordinator);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        toolbar.setTitle("Donar Details");
        sessionManagement = new SessionManagement(this);
        itemNav = navigationView.getMenu();
        setSupportActionBar(toolbar);

        credetial = getIntent().getStringExtra("credential");

        HashMap<String, String> userDetail = sessionManagement.getUserDetails();
        if(userDetail.get(SessionManagement.KEY_ID) != (null)) {

            HashMap<String, String> loginUser = sessionManagement.getUserDetails();
            credetial = loginUser.get(SessionManagement.KEY_ID);
        }
        dbRefer = FirebaseDatabase.getInstance().getReference();



        fragmentManager.beginTransaction()
                .replace(R.id.donar_frame_layout, new DonarListFragment())
                .addToBackStack("donar_fragment")
                .commit();

        dMethod = new DonarDBMethods(this);
        uMethod = new UserDBMethods(this);
        donars = dMethod.getDonarsList();


        dAdapter = new DonarListAdapter(this,donars);
        dAdapter.notifyDataSetChanged();
       // currentFrag = getActiveFragment();
        Log.i(TAG + "active frag", currentFrag + "visible");

        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        if(credetial== "")
            credetial = user.get(SessionManagement.KEY_ID);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_home2);
        //View headerView = navigationView.getHeaderView(R.layout.nav_header_home2);
        login_user = (TextView) headerView.findViewById(R.id.login_user_id);
        login_user.setText(credetial);
        TextView login_phone = (TextView) headerView.findViewById(R.id.login_user_number);
        login_phone.setText(uMethod.getProfile(credetial).getuName());
        icon = (ImageView) headerView.findViewById(R.id.user_profile);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iconClick();
            }
        });








        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG + "fab", "onclick");
                Intent intent;
                frag = fragmentManager.findFragmentById(R.id.donar_frame_layout);

                if(frag instanceof UserListFragment){
                    intent = new Intent(HomeActivity.this, AddUserActivity.class);

                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }else if(frag instanceof DonarListFragment){
                    intent = new Intent(HomeActivity.this, AddDonarActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }


                //bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());

            }
        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);

        drawer.setDrawerListener(new DrawerLayout.DrawerListener(){
            @Override public void onDrawerSlide(View drawerView, float slideOffset) {}
            @Override public void onDrawerOpened(View drawerView) {}
            @Override public void onDrawerClosed(View drawerView) {}
            @Override public void onDrawerStateChanged(int newState) {
                //DeviceUtils.hideVirtualKeyboard(LaunchActivity.this, drawerLayout);
                getActiveFragment();
                final InputMethodManager imm = (InputMethodManager)HomeActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(drawer.getWindowToken(), 0);
            }
        });



    }




    boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onResume() {
        super.onResume();
        // .... other stuff in my onResume ....
        this.doubleBackToExitPressedOnce = false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

       //  currentFrag = getActiveFragment();
        frag = fragmentManager.findFragmentById(R.id.donar_frame_layout);
        Log.i(TAG + " active frag", currentFrag +" on back pressed");
        if (doubleBackToExitPressedOnce) {

            finish();
            return;
        }

        if(frag != null && frag instanceof  DonarListFragment){
            Toast.makeText(this, "Press Back to exit application", Toast.LENGTH_SHORT).show();
           this.doubleBackToExitPressedOnce = true;
        }else
            this.doubleBackToExitPressedOnce = false;


        toolbar.setTitle("Donar Details");
        //currentFrag = getActiveFragment();
        Log.i(TAG + " active frag", currentFrag +" on back pressed");

        itemNav.findItem(R.id.donars).setChecked(true);
        fragmentManager.beginTransaction().replace(R.id.donar_frame_layout, new DonarListFragment(), "donar_fragment")
                .addToBackStack("donar_fragment")
                .commit();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


//        login_user = (TextView) findViewById(R.id.login_user_id);
//        login_user.setText(credetial);
//        TextView login_phone = (TextView) findViewById(R.id.login_user_number);
//        login_phone.setText(uMethod.getProfile(credetial).getuName());
//        icon = (ImageView) findViewById(R.id.user_profile);
//        icon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                iconClick();
//            }
//        });

        manipulatingMenuItems();


        return true;
    }



    public void manipulatingMenuItems(){

        if(credetial.equals("admin")){
            fab.setVisibility(View.VISIBLE);
            Log.i(TAG + "menu item before ",itemNav.getItem(1).getTitle().toString());
            itemNav.findItem(R.id.beADonar).setVisible(false);
            itemNav.findItem(R.id.users).setVisible(true);
            Log.i(TAG + "menu item after",itemNav.getItem(1).getTitle().toString());



        }else{
            fab.setVisibility(View.INVISIBLE);
            User u = uMethod.getProfile(credetial);
            Log.i(TAG + "donar credential", u.getDonar() +"");
            if(u.getDonar() == 1 || credetial.equals("admin"))
                itemNav.findItem(R.id.beADonar).setVisible(false);
            else
                itemNav.findItem(R.id.beADonar).setVisible(true);
            itemNav.findItem(R.id.users).setVisible(false);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);



    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected( final MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.donars) {
            toolbar.setTitle("Donars Details");
             //currentFrag = getActiveFragment();
            Log.i(TAG + "active frag", currentFrag + "navigation");
            frag = fragmentManager.findFragmentById(R.id.donar_frame_layout);
            if(frag != null && frag instanceof DonarListFragment){

            }else {
                fragmentManager.beginTransaction().replace(R.id.donar_frame_layout, new DonarListFragment(), "donar_fragment")
                        .addToBackStack("donar_fragment")
                        .commit();
            }

        }else if (id == R.id.profile) {
            // Handle the camera action
           Intent profileIntent = new Intent(HomeActivity.this, ProfileActivity.class);
           Log.i(TAG +"checking profile id : ", credetial);
           profileIntent.putExtra("id", credetial);
            profileIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
           startActivity(profileIntent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
           //finish();
        } else if (id == R.id.beADonar) {
            Intent beDonarIntent = new Intent(HomeActivity.this, AddDonarActivity.class);
            beDonarIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(beDonarIntent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            //finish();
        }  else if (id == R.id.users) {
           toolbar.setTitle("User List");
            //currentFrag = getActiveFragment();
            this.doubleBackToExitPressedOnce = false;
           Log.i(TAG + "active frag", currentFrag + "navigation");
            frag = fragmentManager.findFragmentById(R.id.donar_frame_layout);
            if(frag != null && frag instanceof UserListFragment){

            }else {
                fragmentManager.beginTransaction().replace(R.id.donar_frame_layout, new UserListFragment(), "user_fragment")
                        .addToBackStack("user_fragment")
                        .commit();
            }

        } else if(id == R.id.share) {


            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
            sendIntent.setType("text/plain");
            item.setChecked(false);

            startActivity(sendIntent);



        }else if (id == R.id.tab_view){
            Intent tabIntent = new Intent(this, TabbedActivity.class);
            startActivity(tabIntent);
        }else if (id == R.id.shared_transition){
            Intent tabIntent = new Intent(this, Activity1.class);
            startActivity(tabIntent);

        }else if (id == R.id.services){
            Intent tabIntent = new Intent(this, ServicesActivity.class);
            startActivity(tabIntent);
        }else if (id == R.id.logout) {
                //logging out the user
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to logout ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    check_internet();
                    donars.clear();
                    donars = dMethod.getDonarsList();

                    for (Donar donar : donars){
                        Log.i(TAG + "donar name val ", donar.getdName() + "");
                        dbRefer.child("Donars").child(donar.getdContact()).setValue(donar);
                    }
                    user = uMethod.getUserList();
                    Log.i(TAG + " user vals ", user+ "");
                    for (User u: user){
                        Log.i(TAG + "donar val ", u.getDonar() + u.getuName());
                        dbRefer.child("Users").child(u.getUserId()).setValue(u);
                    }
                    db.delete();
                    session.logoutUser();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    //itemNav.findItem(R.id.logout).setChecked(false);
                    getActiveFragment();
                }
            });
            // Create the AlertDialog object and return it
            android.app.AlertDialog dialog = builder.create();
            dialog.show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public  void iconClick(){
        Intent profileIntent = new Intent(HomeActivity.this, ProfileActivity.class);
        Log.i(TAG +"checking profile id : ", credetial);
        profileIntent.putExtra("id", credetial);
        profileIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(profileIntent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }



    public void getActiveFragment() {
        frag = fragmentManager.findFragmentById(R.id.donar_frame_layout);
        if(frag instanceof DonarListFragment){
            itemNav.findItem(R.id.donars).setChecked(true);
            Log.i(TAG + "on back ", "donars");
            toolbar.setTitle("Donar Details");
//            fragmentManager.beginTransaction().replace(R.id.donar_frame_layout, new DonarListFragment(), "donar_fragment")
//                    .addToBackStack("donar_fragment")
//                    .commit();
        }
        else
        {
            itemNav.findItem(R.id.users).setChecked(true);
            Log.i(TAG + "on back ", "users");
            toolbar.setTitle("User List");
//            fragmentManager.beginTransaction().replace(R.id.donar_frame_layout, new UserListFragment(), "user_fragment")
//                    .addToBackStack("user_fragment")
//                    .commit();
        }
    }


    void check_internet(){
        if (!isNetworkAvailable(HomeActivity.this)) {
            new AlertDialog.Builder(HomeActivity.this)
                    .setTitle("Connection Alert")
                    .setMessage("You are not connected to internet")
                    .setCancelable(false)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();
        }
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }


    @Override
    protected void onStart() {
        super.onStart();

        User u = uMethod.getProfile(credetial);
        Log.i(TAG, "crede"+ credetial + u.getDonar()+ "");
        if(u.getDonar() == 1 & !credetial.equals("admin"))
            itemNav.findItem(R.id.beADonar).setVisible(false);
       getActiveFragment();
        Log.i(TAG + "active frag", currentFrag +"on start ");
        //ft.remove(fragmentManager.findFragmentById(R.id.donar_frame_layout));


    }

}
