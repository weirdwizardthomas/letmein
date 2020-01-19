package com.via.letmein.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.via.letmein.R;
import com.via.letmein.persistence.model.AdminPromotion;
import com.via.letmein.ui.main_activity.MainActivity;

import static com.via.letmein.persistence.api.Error.ERROR_DATABASE_ERROR;
import static com.via.letmein.persistence.api.Error.ERROR_EXPIRED_SESSION_ID;
import static com.via.letmein.persistence.api.Error.ERROR_LOCKING_DEVICE_NOT_FOUND;
import static com.via.letmein.persistence.api.Error.ERROR_MEMBER_ID_NOT_FOUND;
import static com.via.letmein.persistence.api.Error.ERROR_MEMBER_IS_ALREADY_AN_ADMIN;
import static com.via.letmein.persistence.api.Error.ERROR_MISSING_REQUIRED_PARAMETERS;

public class AlternateRegisterActivity extends AppCompatActivity {

    private static final String TAG = "AlternateRegisterActivity";

    private EditText ipAddressInput;
    private EditText sessionIdInput;
    private Button registerButton;

    private AlternateRegisterViewModel alternateRegisterViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alternate_register);
        alternateRegisterViewModel = ViewModelProviders.of(this).get(AlternateRegisterViewModel.class);


        ipAddressInput = findViewById(R.id.ipAddressInput);
        sessionIdInput = findViewById(R.id.sessionIdInput);
        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(v -> {
            String ipAddress = ipAddressInput.getText().toString();
            String sessionId = sessionIdInput.getText().toString();

            alternateRegisterViewModel
                    .setIpAddress(ipAddress)
                    .setSessionId(sessionId);
            alternateRegisterViewModel.confirmPromotion().observe(this, apiResponse -> {

                if (apiResponse != null) {
                    if (!apiResponse.isError() && apiResponse.getContent() != null) {
                        AdminPromotion admin = (AdminPromotion) apiResponse.getContent();

                        //save the credentials
                        alternateRegisterViewModel
                                .setUsername(admin.getUser_name()) //save the chosen usernameTextView
                                .setPassword(admin.getPassword()) //save the password
                                .setUserID(admin.getId())//save the received password
                                .setRegistered(); //set registered to true
                        openMainActivity();
                    }

                    if (apiResponse.isError() && apiResponse.getErrorMessage() != null)
                        handleErrors(apiResponse.getErrorMessage());
                }
            });
        });
    }

    private void openMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void handleErrors(String errorMessage) {
        switch (errorMessage) {
            case ERROR_MISSING_REQUIRED_PARAMETERS: {
                Log.e(TAG, ERROR_MISSING_REQUIRED_PARAMETERS);
                break;
            }

            case ERROR_EXPIRED_SESSION_ID: {
                Toast.makeText(this, "Session ID expired. Try a new one.", Toast.LENGTH_SHORT).show();
                break;
            }

            case ERROR_MEMBER_IS_ALREADY_AN_ADMIN: {
                Toast.makeText(this, "That member is already an administrator.", Toast.LENGTH_SHORT).show();
                break;
            }

            case ERROR_MEMBER_ID_NOT_FOUND: {
                Log.e(TAG, ERROR_MEMBER_ID_NOT_FOUND);
                break;
            }

            case ERROR_DATABASE_ERROR: {
                Log.e(TAG, ERROR_DATABASE_ERROR);
                break;
            }

            case ERROR_LOCKING_DEVICE_NOT_FOUND: {
                Toast.makeText(this, "There was an error accessing the locking device", Toast.LENGTH_SHORT).show();
                Log.e(TAG, ERROR_LOCKING_DEVICE_NOT_FOUND);
                break;
            }
        }
    }
}
