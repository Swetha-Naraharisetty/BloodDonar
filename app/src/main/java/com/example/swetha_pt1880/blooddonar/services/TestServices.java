package com.example.swetha_pt1880.blooddonar.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.swetha_pt1880.blooddonar.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by swetha-pt1880 on 26/2/18.
 */

public class TestServices extends Service {
    static int count = 0;
    private Timer timer;
    private TimerTask timerTask;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(getBaseContext(), "Service started ", Toast.LENGTH_SHORT).show();
        Log.i("services ", count + " ");
        //count++;
        startTimer();

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stoptimertask();
        Toast.makeText(getBaseContext(), "Service stopped  ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        count = 0;

    }


    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 1000, 1000); //
    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                count++;
                Log.i("in timer", "in timer ++++  "+ (count));
            }
        };
    }


    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public int getCount(){
        return count;
    }

}
