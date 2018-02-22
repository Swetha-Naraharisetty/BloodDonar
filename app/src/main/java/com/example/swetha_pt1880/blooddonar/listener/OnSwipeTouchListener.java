package com.example.swetha_pt1880.blooddonar.listener;

/**
 * Created by swetha-pt1880 on 21/2/18.
 */

import android.content.Context;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.example.swetha_pt1880.blooddonar.activity.ProfileActivity;
import com.example.swetha_pt1880.blooddonar.database.User;

import java.util.ArrayList;

public class OnSwipeTouchListener implements OnTouchListener {
    ArrayList<User> users = new ArrayList<>();
    static int count = 0;
    Context context;

    private final GestureDetector gestureDetector;

    public OnSwipeTouchListener (Context ctx, ArrayList<User> users, int pos){
        this.users = users;
        this.context = ctx;
        count = pos;
        gestureDetector = new GestureDetector(ctx, new GestureListener());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                        result = true;
                    }
                }
                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                    result = true;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    public void onSwipeRight() {
    }

    public void onSwipeLeft() {
        Intent in = new Intent(context, ProfileActivity.class);
        in.putExtra("id", users.get(count).getUserId());
        context.startActivity(in);

    }

    public void onSwipeTop() {
    }

    public void onSwipeBottom() {
    }
}