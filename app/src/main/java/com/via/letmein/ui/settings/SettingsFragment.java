package com.via.letmein.ui.settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.amirarcane.lockscreen.activity.EnterPinActivity;
import com.via.letmein.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        PreferenceScreen preferenceScreen = getPreferenceScreen();
        Preference preference = preferenceScreen.findPreference("reset_pin");
        if (preference != null)
            preference.setOnPreferenceClickListener(preference1 -> {
                Intent intent = EnterPinActivity.getIntent(getContext(), true);
                startActivity(intent);
                return true;
            });

    }
}