package com.via.letmein.ui.register;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.via.letmein.R;
import com.via.letmein.persistence.api.ApiResponse;
import com.via.letmein.persistence.api.Session;

/**
 * An activity that handles device registration and pairing with this application.
 */
//todo document
public class RegisterActivity extends AppCompatActivity {

    public static final String REGISTERED_KEY = "registered";
    public static final String REGISTRATION_FILE = "registration";

    private EditText username;
    private EditText serialNumber;
    private Button registerButton;
    private RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initialiseLayout();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = username.getText().toString();
                String serial = serialNumber.getText().toString();

                register(name, serial);

            }
        });


    }

    private void initialiseLayout() {
        username = findViewById(R.id.nameInput);
        serialNumber = findViewById(R.id.serialNumberInput);
        registerButton = findViewById(R.id.registerButton);
    }

    public void register(final String name, String serialNumber) {

        registerViewModel.register(name, serialNumber).observe(this, apiResponse -> {
            if (apiResponse != null) {
                if (!apiResponse.isError() && apiResponse.getContent() != null) {

                    String password = (String) apiResponse.getContent();
                    //save the chosen username
                    saveUsername(name);
                    //save the received password
                    savePassword(password);
                    //set registered to true
                    setRegistered();

                    finish();
                }

                if (apiResponse.isError() && apiResponse.getErrorMessage() != null) {
                    //TODO handle errors
                }
            }
        });
    }

    private void saveUsername(String username) {
        Session session = Session.getInstance(getApplicationContext());
        session.setUsername(username);
    }

    private void savePassword(String password) {
        Session session = Session.getInstance(getApplicationContext());
        session.setPassword(password);
    }


    private void setRegistered() {
        SharedPreferences preferences = getSharedPreferences(REGISTRATION_FILE, MODE_PRIVATE);
        preferences.edit().putBoolean(REGISTERED_KEY, true).apply();
    }

}
