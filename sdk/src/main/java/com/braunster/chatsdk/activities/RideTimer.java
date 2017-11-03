package com.braunster.chatsdk.activities;

/**
 * Created by DELL on 10/21/2017.
 */

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

public class RideTimer {


        /** Called when the activity is first created. */

        private long startTime = 0L;
        private Handler myHandler = new Handler();
        long timeInMillies = 0L;
        long timeSwap = 0L;
        long finalTime = 0L;



            public void starttime() {
        startTime = SystemClock.uptimeMillis();
        myHandler.postDelayed(updateTimerMethod, 0);

    }
    public void stoptime() {
                    timeSwap += timeInMillies;
                    myHandler.removeCallbacks(updateTimerMethod);

                }




        private Runnable updateTimerMethod = new Runnable() {

            public void run() {
                timeInMillies = SystemClock.uptimeMillis() - startTime;
                finalTime = timeSwap + timeInMillies;

                int seconds = (int) (finalTime / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                int milliseconds = (int) (finalTime % 1000);
                FirstMainActivity.counttime.setText("" + minutes + ":"
                        + String.format("%02d", seconds) + ":"
                        + String.format("%03d", milliseconds));
                myHandler.postDelayed(this, 0);
            }

        };

    }




