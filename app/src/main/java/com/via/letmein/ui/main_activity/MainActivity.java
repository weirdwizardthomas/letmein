package com.via.letmein.ui.main_activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.amirarcane.lockscreen.activity.EnterPinActivity;
import com.google.android.material.navigation.NavigationView;
import com.via.letmein.R;
import com.via.letmein.persistence.api.Session;
import com.via.letmein.persistence.api.ApiResponse;
import com.via.letmein.ui.register.RegisterActivity;

/**
 * Application's main activity.
 */
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private MainActivityViewModel mainActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        setContentView(R.layout.activity_main);
        initialiseToolbar();
        initialiseNavigationBar();

        openPin();

        if (!isRegistered())
            register();

        if (isRegistered())
            login();
    }

    private void saveSessionID(ApiResponse apiResponse) {
        Session session = Session.getInstance(getApplicationContext());
        session.setSessionId((String) apiResponse.getContent());
    }

    public void login() {
        Session session = Session.getInstance(getApplicationContext());
        String username = session.getUsername();
        String password = session.getPassword();

        mainActivityViewModel.getSessionID(username, password).observe(this, apiResponse -> {
            if (apiResponse != null) {
                if (!apiResponse.isError() && apiResponse.getContent() != null) {
                    saveSessionID(apiResponse);
                    Toast.makeText(getApplicationContext(), "Logged on", Toast.LENGTH_SHORT).show();
                }

                if (apiResponse.isError() && apiResponse.getErrorMessage() != null) {
                    //TODO handle errors
                }
            }
        });

    }

    private void register() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private boolean isRegistered() {
        SharedPreferences preferences = getSharedPreferences(RegisterActivity.REGISTRATION_FILE, MODE_PRIVATE);
        boolean isRegistered = preferences.getBoolean(RegisterActivity.REGISTERED_KEY, false);
        return isRegistered;
    }

    /**
     * Opens a new activity that handles application unlocking
     */
    private void openPin() {
        Intent intent = new Intent(getApplicationContext(), EnterPinActivity.class);
        startActivity(intent);
    }

    /**
     * Initialises a custom toolbar for the activity.
     */
    private void initialiseToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * Initialises the bottom navigation menu
     */
    private void initialiseNavigationBar() {
        DrawerLayout drawer = findViewById(R.id.main_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_history,
                R.id.nav_administration,
                R.id.nav_live,
                R.id.nav_feedback,
                R.id.nav_settings)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

}
