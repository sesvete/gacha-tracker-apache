package com.sesvete.gachatrackerapache;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;


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
                String userId = sharedPref.getString("userId", null);
                long expiresAt = sharedPref.getLong("expiresAt", 0);

                if (userId != null && System.currentTimeMillis() / 1000 < expiresAt) {
                    // Session is valid, navigate to main activity
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    intent = new Intent(SplashActivity.this, SignInWithPasswordActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}