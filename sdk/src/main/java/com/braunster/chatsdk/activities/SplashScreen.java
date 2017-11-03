package com.braunster.chatsdk.activities;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.braunster.chatsdk.R;
import com.braunster.chatsdk.activities.WelcomeActivity;

public class SplashScreen extends AppCompatActivity {

    private static SplashTimer timer;
    private final static int TIMER_INTERVAL = 4000; // 4 sec
    private boolean activityStarted;
    private boolean exitAds = false;
    private boolean mWasGetContentIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        Intent intent = getIntent();
        mWasGetContentIntent = intent.getAction().equals(
                Intent.ACTION_GET_CONTENT);

        setContentView(R.layout.activity_splash);
        StartAnimations();



        timer = new SplashTimer();
        timer.sendEmptyMessageDelayed(1, TIMER_INTERVAL);
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    final class SplashTimer extends Handler {
        @Override
        public void handleMessage(Message msg) {
            post(new Runnable() {

                public void run() {
                    timer = null;

                    startHomePageActivity();
                }
            });
        }
    }

    private void startHomePageActivity() {



        if (activityStarted) {
            return;
        }
        activityStarted = true;

        SplashScreen.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {

                startActivity(new Intent(SplashScreen.this, WelcomeActivity.class));
                finish();
            }
        });


    }

    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l=(LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);


//        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
//        anim.reset();
//        iv.clearAnimation();
//        iv.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.rotatelogo);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.imgLogo);

        iv.clearAnimation();
        iv.startAnimation(anim);

    }

}
