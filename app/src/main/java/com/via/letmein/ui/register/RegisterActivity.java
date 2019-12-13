package com.via.letmein.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.via.letmein.R;
import com.via.letmein.ui.main_activity.MainActivity;

import static com.via.letmein.persistence.api.Errors.ERROR_ADMIN_ALREADY_EXISTS;
import static com.via.letmein.persistence.api.Errors.ERROR_DATABASE_ERROR;
import static com.via.letmein.persistence.api.Errors.ERROR_MISSING_REQUIRED_PARAMETERS;
import static com.via.letmein.persistence.api.Errors.ERROR_NAME_ALREADY_IN_USE;
import static com.via.letmein.persistence.api.Errors.ERROR_USERNAME_TOO_SHORT;
import static com.via.letmein.persistence.api.Errors.ERROR_WRONG_SERIAL_ID;

/**
 * An activity that handles device registration and pairing with this application.
 */
public class RegisterActivity extends AppCompatActivity implements IPListenAsyncTask.IpListener {

    public static final String TAG = "RegisterActivity";

    private RegisterViewModel registerViewModel;

    private EditText usernameTextView;
    private EditText serialIdTextView;
    private Button registerButton;
    private TextView ipAddressTextView;
    private TextView nameTooShortTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initialiseLayout();
        listenForIp();

    }

    /**
     * Starts a background task that listens to network's broadcast to find the locking device
     */
    private void listenForIp() {
        new IPListenAsyncTask(this).execute();
    }

    /**
     * Initialises layout's components.
     */
    private void initialiseLayout() {
        usernameTextView = findViewById(R.id.nameTextView);
        usernameTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (nameTooShortTextView.getVisibility() == View.VISIBLE)
                    nameTooShortTextView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        serialIdTextView = findViewById(R.id.serialIdTextView);

        ipAddressTextView = findViewById(R.id.ipAddressTextView);
        ipAddressTextView.setVisibility(View.INVISIBLE);

        nameTooShortTextView = findViewById(R.id.nameTooShortTextView);
        nameTooShortTextView.setVisibility(View.INVISIBLE);

        registerButton = findViewById(R.id.registerButton);
        registerButton.setEnabled(false);
        registerButton.setOnClickListener(v -> {
            String name = usernameTextView.getText().toString();
            String serial = serialIdTextView.getText().toString();
            if (name.isEmpty())
                Toast.makeText(this, "Name cannot be empty.", Toast.LENGTH_SHORT).show();

            if (!name.isEmpty())
                register(name, serial);
        });
    }

    /**
     * Registers a new administrator on the server
     *
     * @param name     username of the administrator
     * @param serialId Serial number of the device
     */
    public void register(final String name, String serialId) {
        registerViewModel.register(name, serialId).observe(this, apiResponse -> {
            if (apiResponse != null) {
                if (!apiResponse.isError() && apiResponse.getContent() != null) {

                    String password = (String) apiResponse.getContent();

                    //save the credentials
                    registerViewModel
                            .setUsername(name) //save the chosen usernameTextView
                            .setPassword(password) //save the received password
                            .setRegistered(); //set registered to true
                    openMainActivity();
                }

                if (apiResponse.isError() && apiResponse.getErrorMessage() != null)
                    handleError(apiResponse.getErrorMessage());
            }
        });
    }

    private void openMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    /**
     * Handles error responses from the server
     *
     * @param errorMessage Error message received from the server
     */
    private void handleError(String errorMessage) {

        switch (errorMessage) {
            case ERROR_USERNAME_TOO_SHORT: {
                usernameTextView.setVisibility(View.VISIBLE);
                break;
            }
            case ERROR_MISSING_REQUIRED_PARAMETERS: {
                Log.d(TAG, ERROR_MISSING_REQUIRED_PARAMETERS);
                break;
            }
            case ERROR_DATABASE_ERROR: {
                Toast.makeText(this, getString(R.string.databaseError), Toast.LENGTH_SHORT).show();
                break;
            }
            case ERROR_ADMIN_ALREADY_EXISTS: {
                Toast.makeText(this, getString(R.string.adminAlreadyExists), Toast.LENGTH_SHORT).show();
                break;
            }
            case ERROR_WRONG_SERIAL_ID: {
                Toast.makeText(this, getString(R.string.wrongSerialId), Toast.LENGTH_SHORT).show();
                break;
            }
            case ERROR_NAME_ALREADY_IN_USE: {
                Toast.makeText(this, getString(R.string.nameInUse), Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    /**
     * Stores the received ip address of the server
     *
     * @param ipAddress The ip address of the server
     */
    @Override
    public void onTaskCompleted(String ipAddress) {
        //save the ip address
        registerViewModel.setIpAddress(ipAddress);
        //allow button clicking
        registerButton.setEnabled(true);
        //show ip address
        ipAddressTextView.setVisibility(View.VISIBLE);
        ipAddressTextView.setText(getString(R.string.labelIpAddressFound) + ipAddress);

    }
}
