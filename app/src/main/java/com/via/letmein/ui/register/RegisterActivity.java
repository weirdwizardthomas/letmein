package com.via.letmein.ui.register;

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

import static com.via.letmein.persistence.repository.SessionRepository.ERROR_ADMIN_ALREADY_EXISTS;
import static com.via.letmein.persistence.repository.SessionRepository.ERROR_DATABASE_ERROR;
import static com.via.letmein.persistence.repository.SessionRepository.ERROR_MISSING_REQUIRED_PARAMETERS;
import static com.via.letmein.persistence.repository.SessionRepository.ERROR_NAME_ALREADY_IN_USE;
import static com.via.letmein.persistence.repository.SessionRepository.ERROR_SHORT_USERNAME;
import static com.via.letmein.persistence.repository.SessionRepository.ERROR_WRONG_SERIAL_ID;

/**
 * An activity that handles device registration and pairing with this application.
 */
//todo document
public class RegisterActivity extends AppCompatActivity {

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

    private void listenForIp() {
        new IPListenAsyncTask(this).execute();
    }

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
            if (!name.isEmpty()) //TODO show a prompt for non empty usernameTextView
                register(name, serial);
        });
    }

    public void register(final String name, String serialNumber) {
        registerViewModel.register(name, serialNumber).observe(this, apiResponse -> {
            if (apiResponse != null) {
                if (!apiResponse.isError() && apiResponse.getContent() != null) {

                    String password = (String) apiResponse.getContent();

                    //save the credentials
                    registerViewModel
                            .setUsername(name) //save the chosen usernameTextView
                            .setPassword(password) //save the received password
                            .setRegistered(); //set registered to true

                    finish();
                }

                if (apiResponse.isError() && apiResponse.getErrorMessage() != null)
                    handleError(apiResponse.getErrorMessage());
            }
        });
    }

    private void handleError(String errorMessage) {

        switch (errorMessage) {
            case ERROR_SHORT_USERNAME: {
                usernameTextView.setVisibility(View.VISIBLE);
            }
            case ERROR_MISSING_REQUIRED_PARAMETERS: {
                Log.i("RegisterActivity", getString(R.string.missingParameters));
            }
            case ERROR_DATABASE_ERROR: {
                Toast.makeText(this, getString(R.string.databaseError), Toast.LENGTH_SHORT).show();
            }
            case ERROR_ADMIN_ALREADY_EXISTS: {
                Toast.makeText(this, getString(R.string.adminAlreadyExists), Toast.LENGTH_SHORT).show();
            }
            case ERROR_WRONG_SERIAL_ID: {
                Toast.makeText(this, getString(R.string.wrongSerialId), Toast.LENGTH_SHORT).show();
            }
            case ERROR_NAME_ALREADY_IN_USE: {
                Toast.makeText(this, getString(R.string.nameInUse), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onIpReceived(String ipAddress) {
        //save the ip address
        registerViewModel.setIpAddress(ipAddress);
        //allow button clicking
        registerButton.setEnabled(true);
        //show ip address
        ipAddressTextView.setVisibility(View.VISIBLE);
        ipAddressTextView.setText("Found IP Address:" + ipAddress);
    }


}
