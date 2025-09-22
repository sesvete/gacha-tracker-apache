package com.sesvete.gachatrackerapache;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.button.MaterialButton;
import com.sesvete.gachatrackerapache.fragment.CounterFragment;
import com.google.android.material.navigation.NavigationView;
import com.sesvete.gachatrackerapache.fragment.HistoryFragment;
import com.sesvete.gachatrackerapache.fragment.SettingsFragment;
import com.sesvete.gachatrackerapache.fragment.StatsFragment;
import com.sesvete.gachatrackerapache.helper.AuthenticationHelperApache;
import com.sesvete.gachatrackerapache.helper.DialogHelper;
import com.sesvete.gachatrackerapache.helper.LocaleHelper;
import com.sesvete.gachatrackerapache.helper.SettingsHelper;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private FragmentManager fragmentManager;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            long timerAutoLoginStart = intent.getExtras().getLong("timerStart", 0);
            long timerAutoLoginEnd = System.nanoTime();
            long timerAutologinResult = (timerAutoLoginEnd - timerAutoLoginStart)/1000000;

            Log.i("Timer Automatic login", Long.toString(timerAutologinResult ) + " " + "ms");
        }

        toolbar = findViewById(R.id.toolbar);
        if (savedInstanceState == null){
            toolbar.setTitle(R.string.counter);
        }
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        fragmentManager = getSupportFragmentManager();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null){
            fragmentManager.beginTransaction().replace(R.id.fragment_container, CounterFragment.class, null).setReorderingAllowed(true).commit();
            navigationView.setCheckedItem(R.id.nav_counter);
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_counter){
                    toolbar.setTitle(R.string.counter);
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, CounterFragment.class, null).setReorderingAllowed(true).commit();
                }
                else if (id == R.id.nav_history){
                    toolbar.setTitle(R.string.history);
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, HistoryFragment.class, null).setReorderingAllowed(true).commit();
                }
                else if (id == R.id.nav_statistics){
                    toolbar.setTitle(R.string.statistics);
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, StatsFragment.class, null).setReorderingAllowed(true).commit();
                }
                else if (id == R.id.nav_settings){
                    toolbar.setTitle(R.string.settings);
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, SettingsFragment.class, null).setReorderingAllowed(true).commit();
                } else if (id == R.id.nav_logout) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    LayoutInflater dialogInflater = getLayoutInflater();
                    View dialogView = dialogInflater.inflate(R.layout.logout_dialog, null);
                    builder.setView(dialogView);
                    AlertDialog dialog = builder.create();

                    DialogHelper.buildAlertDialogWindow(dialog, MainActivity.this, MainActivity.this);

                    MaterialButton btnLogoutCancel = dialogView.findViewById(R.id.btn_logout_cancel);
                    MaterialButton btnLogoutConfirm = dialogView.findViewById(R.id.btn_logout_confirm);

                    btnLogoutCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    btnLogoutConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            long timerLogoutStart = System.nanoTime();
                            AuthenticationHelperApache.logoutUser(getResources(), MainActivity.this);
                        }
                    });
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        updateNavHeaderUser(navigationView);
        updateNavHeader(navigationView);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    finishAffinity();
                }
            }
        });

    }

    private void updateNavHeaderUser(NavigationView navigationView){
        if (navigationView != null) {
            View navHeaderView = navigationView.getHeaderView(0);
            TextView txtNavHeaderUserName = navHeaderView.findViewById(R.id.txt_nav_header_user_name);
            SharedPreferences sharedPref = getSharedPreferences("GachaTrackerPrefs", getBaseContext().MODE_PRIVATE);
            String userName = sharedPref.getString("username", null);
            txtNavHeaderUserName.setText(userName);
        }
    }

    public void updateNavHeader(NavigationView navigationView){
        if (navigationView != null) {
            View navHeaderView = navigationView.getHeaderView(0);
            TextView txtNavHeaderGame = navHeaderView.findViewById(R.id.txt_nav_header_game);
            TextView txtNavHeaderBanner = navHeaderView.findViewById(R.id.txt_nav_header_banner);

            txtNavHeaderGame.setText(SettingsHelper.getEntryFromValue(this, "game", "genshin_impact"));
            txtNavHeaderBanner.setText(SettingsHelper.getEntryFromValue(this, "banner", "limited"));
        }
    }
}