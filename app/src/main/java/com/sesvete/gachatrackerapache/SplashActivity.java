package com.sesvete.gachatrackerapache;

import android.content.Intent;
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

        // TODO: preveri, če je uporabnik prijavljen v aplikacijo
        // naj bo to token ali kar koli


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*
                if (currentUser == null) {
                    intent = new Intent(SplashActivity.this, SignInActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                }

                 */
                intent = new Intent(SplashActivity.this, SignInWithPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}