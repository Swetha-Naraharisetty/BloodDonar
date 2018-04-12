package com.example.swetha_pt1880.blooddonar.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.swetha_pt1880.blooddonar.R;
import com.example.swetha_pt1880.blooddonar.database.Database;
import com.example.swetha_pt1880.blooddonar.database.Donar;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Activity1 extends AppCompatActivity {
    ImageView img;
    TextView text_text;
    AutoCompleteTextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1);
        img = (ImageView)findViewById(R.id.imageView);
        text_text = (TextView)findViewById(R.id.text_text);
        tv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        List<String> placesList = Arrays.asList(Database.places);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Collections.sort(placesList, new Comparator<String>() {
            public int compare(String v1, String v2) {
                return v1.toLowerCase().compareTo(v2.toLowerCase());
            }
        });

        //setting the city adapter
        ArrayAdapter cityAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item,
                placesList);
        tv.setAdapter(cityAdapter);

        tv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                text_text.setText(String.valueOf(adapterView.getItemAtPosition(i)).toLowerCase());
            }
        });
        img = (ImageView)findViewById(R.id.imageView);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Activity1.this, Activity2.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(Activity1.this,text_text , "test_me");
                startActivity(in,  options.toBundle());
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
