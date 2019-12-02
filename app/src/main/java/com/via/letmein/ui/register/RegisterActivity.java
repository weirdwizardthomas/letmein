package com.via.letmein.ui.register;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.via.letmein.R;
import com.via.letmein.persistence.api.ServiceGenerator;
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

        //find the ip
        new IPListenAsyncTask(this).execute();

        initialiseLayout();

        //todo add a textview with the ip address

        registerButton.setOnClickListener(v -> {
            String name = username.getText().toString();
            String serial = serialNumber.getText().toString();
            if (!name.isEmpty()) //TODO show a prompt for non empty username
                register(name, serial);
        });


    }

    private void initialiseLayout() {
        username = findViewById(R.id.nameInput);
        serialNumber = findViewById(R.id.serialNumberInput);
        registerButton = findViewById(R.id.registerButton);
        registerButton.setEnabled(false);
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
        Session session = Session.getInstance(getApplicationContext());
        session.setRegistered(true);
    }

    public void onIpReceived(String s) {
        saveIPAddress(s);
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void saveIPAddress(String ipAddress) {
        SharedPreferences preferences = getSharedPreferences(REGISTRATION_FILE, MODE_PRIVATE);
        preferences.edit().putString("ip_address", ipAddress).apply();
        registerButton.setEnabled(true);
        ServiceGenerator.BASE_URL = ipAddress;
    }
}
