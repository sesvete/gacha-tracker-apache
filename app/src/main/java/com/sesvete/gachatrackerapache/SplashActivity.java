package com.sesvete.gachatrackerapache;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.sesvete.gachatrackerapache.helper.AuthenticationHelperApache;


public class SplashActivity extends AppCompatActivity {

    private Intent intent;
    // to lahko še prilagodiš glede na potrebe
    private static final long SPLASH_DISPLAY_LENGTH = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check for an existing, unexpired session
                SharedPreferences sharedPref = getSharedPreferences("GachaTrackerPrefs", getBaseContext().MODE_PRIVATE);
                String jwtToken = sharedPref.getString("token", null);
                long expireTime = sharedPref.getLong("expireTime", 0);

                if (jwtToken != null && System.currentTimeMillis() / 1000 < expireTime) {
                    long timerAutoLoginStart = System.nanoTime();
                    AuthenticationHelperApache.refreshToken(getBaseContext(), getResources(), SplashActivity.this, timerAutoLoginStart);
                } else {
                    intent = new Intent(SplashActivity.this, SignInWithPasswordActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}