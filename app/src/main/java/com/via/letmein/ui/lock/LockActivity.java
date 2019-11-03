package com.via.letmein.ui.lock;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.beautycoder.pflockscreen.PFFLockScreenConfiguration;
import com.beautycoder.pflockscreen.fragments.PFLockScreenFragment;
import com.via.letmein.R;
import com.via.letmein.ui.MainActivity;

public class LockActivity extends AppCompatActivity {

    //TODO ADD OPTION TO RESET PASSWORD IN SETTINGS
    //TODO ADD AN OBSERVER TO THE CODE FILE?
    //TODO ADD CONFIRMATION?
    //TODO reset pin code input on failure
    public static final int PIN_CODE_LENGTH = 4;
    private PFLockScreenFragment.OnPFLockScreenLoginListener loginListener;
    private PFLockScreenFragment.OnPFLockScreenCodeCreateListener createListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialiseListeners();
        loadPinLock();
    }

    private void initialiseListeners() {
        loginListener = new PFLockScreenFragment.OnPFLockScreenLoginListener() {

            @Override
            public void onCodeInputSuccessful() {
                Toast.makeText(LockActivity.this, "Code successful", Toast.LENGTH_SHORT).show();
                openMainActivity();
            }

            @Override
            public void onFingerprintSuccessful() {
                Toast.makeText(LockActivity.this, "Fingerprint successful", Toast.LENGTH_SHORT).show();
                openMainActivity();
            }

            @Override
            public void onPinLoginFailed() {
                Toast.makeText(LockActivity.this, "Pin failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFingerprintLoginFailed() {
                Toast.makeText(LockActivity.this, "Fingerprint failed", Toast.LENGTH_SHORT).show();
            }
        };
        createListener = new PFLockScreenFragment.OnPFLockScreenCodeCreateListener() {
            @Override
            public void onCodeCreated(String encodedCode) {
                Toast.makeText(LockActivity.this, "Pin created", Toast.LENGTH_SHORT).show();
                PreferencesSettings.saveToPref(LockActivity.this, encodedCode);
                openMainActivity();
            }
        };
    }

    private void loadPinLock() {
        boolean pinExists = !PreferencesSettings.getCode(LockActivity.this).equals("");

        PFLockScreenFragment fragment = new PFLockScreenFragment();
        PFFLockScreenConfiguration.Builder builder = new PFFLockScreenConfiguration.Builder(this)
                .setCodeLength(PIN_CODE_LENGTH)
                .setTitle(pinExists ?
                        "Enter code" :
                        "Create code")
                .setMode(pinExists ?
                        PFFLockScreenConfiguration.MODE_AUTH :
                        PFFLockScreenConfiguration.MODE_CREATE);


        fragment.setConfiguration(builder.build());
        if (pinExists) {
            fragment.setEncodedPinCode(PreferencesSettings.getCode(this));
            fragment.setLoginListener(loginListener);
        } else
            fragment.setCodeCreateListener(createListener);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_layout, fragment)
                .commit();
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
