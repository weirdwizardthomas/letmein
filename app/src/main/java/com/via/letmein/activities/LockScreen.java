package com.via.letmein.activities;

import android.widget.Toast;

import com.github.omadahealth.lollipin.lib.managers.AppLockActivity;

public class LockScreen extends AppLockActivity {

    public static final int PIN_LENGTH = 4;

    @Override
    public void showForgotDialog() {
        Toast.makeText(this, "Forgot PIN", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPinFailure(int attempts) {
        Toast.makeText(this, "You've entered PIN wrong " + attempts, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPinSuccess(int attempts) {
        Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getPinLength() {
        return PIN_LENGTH;
    }
}
