package com.aniXification.copypaste;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class Splash extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
 
        setContentView(R.layout.splash);
 
        /** Creates a count down timer, which will be expired after 3000 milliseconds */
        new CountDownTimer(3000,1000) {
 
            @Override
            public void onFinish() {
                Intent intent = new Intent();
 
                intent.setClass(Splash.this, StarterActivity.class);
                startActivity(intent);
                finish();
            }
 
            /** This method will be invoked in every 1000 milli seconds until
            * this timer is expired.Because we specified 1000 as tick time
            * while creating this CountDownTimer
            */
            @Override
            public void onTick(long millisUntilFinished) {
            }
        }.start();
 
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.splash, menu);
        return true;
    }
}
