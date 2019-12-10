package com.via.letmein.ui.main_activity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

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
import com.via.letmein.service.NotificationService;
import com.via.letmein.ui.register.RegisterActivity;

import static com.via.letmein.persistence.api.Errors.ERROR_EXPIRED_SESSION_ID;
import static com.via.letmein.persistence.api.Errors.ERROR_MISSING_REQUIRED_PARAMETERS;
import static com.via.letmein.persistence.api.Errors.ERROR_USERNAME_TOO_SHORT;
import static com.via.letmein.persistence.api.Errors.ERROR_WRONG_USER_PASSWORD;

/**
 * Application's main activity.
 */
public class MainActivity extends AppCompatActivity {

    public static final int REGISTER_REQUEST_CODE = 1;
    public static final int INTERVAL_MILLIS = 900000;
    public static final int JOB_ID = 123;

    private static final String TAG = "MainActivity";

    private AppBarConfiguration appBarConfiguration;
    private MainActivityViewModel mainActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        setContentView(R.layout.activity_main);
        initialiseToolbar();
        initialiseNavigation();

        openPin();

    /*    if (isRegistered())
            login();
        else
            register();*/

    }

    private void initialiseNavigation() {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_history,
                R.id.nav_administration,
                R.id.nav_live)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    public void login() {
        Session session = Session.getInstance(getApplicationContext());
        String username = session.getUsername();
        String password = session.getPassword();

        mainActivityViewModel.getSessionID(username, password).observe(this, apiResponse -> {
            if (apiResponse != null) {
                if (!apiResponse.isError() && apiResponse.getContent() != null) {
                    mainActivityViewModel.setSessionID((String) apiResponse.getContent());
                    startNotificationListening();
                }

                if (apiResponse.isError() && apiResponse.getErrorMessage() != null)
                    handleErrors(apiResponse.getErrorMessage());

            }
        });
    }

    /**
     * Starts a {@see NotificationService} background service to listen for server's notifications
     */
    private void startNotificationListening() {
        ComponentName componentName = new ComponentName(this, NotificationService.class);
        JobInfo info = new JobInfo.Builder(JOB_ID, componentName)
                .setPeriodic(INTERVAL_MILLIS)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build();
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler != null ? scheduler.schedule(info) : JobScheduler.RESULT_FAILURE;
        Log.i("Notifications", resultCode == JobScheduler.RESULT_SUCCESS ? "Job scheduled" : "Job scheduling failed");
    }

    /**
     * Handles error responses from the server
     *
     * @param errorMessage Error response to be handled
     */
    private void handleErrors(String errorMessage) {
        switch (errorMessage) {
            case ERROR_MISSING_REQUIRED_PARAMETERS: {
                Log.i(TAG, ERROR_MISSING_REQUIRED_PARAMETERS);
                break;
            }
            case ERROR_USERNAME_TOO_SHORT: {
                Toast.makeText(this, getString(R.string.messageUsernameTooShort), Toast.LENGTH_SHORT).show();
                break;
            }
            case ERROR_WRONG_USER_PASSWORD: {
                mainActivityViewModel.wipeSession();
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.registerActivity);
                finish();
                break;
            }
            case ERROR_EXPIRED_SESSION_ID: {
                login();
                break;
            }
        }
    }

    /**
     * Opens {@see RegisterActivity} for new administrator's registration
     */
    private void register() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent, REGISTER_REQUEST_CODE);
    }

    /**
     * Determines whether an administrator has already been paired and registered
     *
     * @return true if is registered, false otherwise
     */
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
        // Make sure the request was successful
        if (requestCode == REGISTER_REQUEST_CODE) {
            if (resultCode == RESULT_OK)
                login();
        }
    }

}
