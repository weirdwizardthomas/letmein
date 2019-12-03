package com.via.letmein.ui.main_activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.amirarcane.lockscreen.activity.EnterPinActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.via.letmein.R;
import com.via.letmein.persistence.api.Session;
import com.via.letmein.ui.register.RegisterActivity;

import static com.via.letmein.persistence.repository.SessionRepository.ERROR_MISSING_REQUIRED_PARAMETERS;
import static com.via.letmein.persistence.repository.SessionRepository.ERROR_SHORT_USERNAME;
import static com.via.letmein.persistence.repository.SessionRepository.ERROR_WRONG_USER_PASSWORD;

/**
 * Application's main activity.
 */
public class MainActivity extends AppCompatActivity {

    public static final int REGISTER_REQUEST_CODE = 1;
    private AppBarConfiguration appBarConfiguration;
    private MainActivityViewModel mainActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        setContentView(R.layout.activity_main);
        initialiseToolbar();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_history,
                R.id.nav_administration,
                R.id.nav_live,
                R.id.nav_feedback,
                R.id.nav_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


        // openPin();

        if (!isRegistered())
            register();

        if (isRegistered())
            login();
    }

    //todo change to accept string
    private void saveSessionID(String sessionId) {
        Session session = Session.getInstance(getApplicationContext());
        session.setSessionId(sessionId);
    }

    public void login() {
        Session session = Session.getInstance(getApplicationContext());
        String username = session.getUsername();
        String password = session.getPassword();

        mainActivityViewModel.getSessionID(username, password).observe(this, apiResponse -> {
            if (apiResponse != null) {
                if (!apiResponse.isError() && apiResponse.getContent() != null) {
                    saveSessionID((String) apiResponse.getContent());
                }

                if (apiResponse.isError() && apiResponse.getErrorMessage() != null) {
                    handleErrors(apiResponse.getErrorMessage());
                }
            }
        });

    }

    private void handleErrors(String errorMessage) {
        switch (errorMessage) {
            case ERROR_MISSING_REQUIRED_PARAMETERS: {
                //TODO ??
            }
            case ERROR_SHORT_USERNAME: {
                //TODO ??
            }
            case ERROR_WRONG_USER_PASSWORD: {
                //TODO ??
            }
        }
    }

    private void register() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent, REGISTER_REQUEST_CODE);
    }

    private boolean isRegistered() {
        Session session = Session.getInstance(getApplicationContext());
        return session.isRegistered();
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        switch (item.getItemId()) {

            case R.id.nav_settings: {
                navController.navigate(R.id.nav_settings);
                return true;
            }
            case R.id.nav_feedback: {
                navController.navigate(R.id.nav_feedback);
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check which request we're responding to
        switch (requestCode) {
            // Make sure the request was successful
            case REGISTER_REQUEST_CODE:
                if (resultCode == RESULT_OK)
                    login();
        }
    }

}
