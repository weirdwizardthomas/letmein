package com.via.letmein.ui.opening_activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.amirarcane.lockscreen.activity.EnterPinActivity;
import com.via.letmein.R;
import com.via.letmein.persistence.api.Session;
import com.via.letmein.ui.main_activity.MainActivity;
import com.via.letmein.ui.register.RegisterActivity;

/**
 * Opening activity that handles registration and application unlocking
 *
 * @author Tomas Koristka: 291129@via.dk
 */
public class OpeningActivity extends AppCompatActivity {

    private static final int PIN_REQUEST_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);
    }

    @Override
    protected void onStart() {
        super.onStart();
        openPin();
    }

    /**
     * Opens a new activity that handles application unlocking
     */
    private void openPin() {
        Intent intent = new Intent(getApplicationContext(), EnterPinActivity.class);
        startActivityForResult(intent, PIN_REQUEST_CODE);
    }

    /**
     * Determines whether an administrator has already been paired and registered
     *
     * @return true if is registered, false otherwise
     */
    private boolean isRegistered() {
        return Session.getInstance(getApplicationContext()).isRegistered();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check which request we're responding to
        if (requestCode == PIN_REQUEST_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                startActivity(new Intent(this, isRegistered() ? MainActivity.class : RegisterActivity.class));
                finish();
                // Do something with the contact here (bigger example below)
            }
        }
    }
}
