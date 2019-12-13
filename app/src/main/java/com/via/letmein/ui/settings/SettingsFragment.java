package com.via.letmein.ui.settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.amirarcane.lockscreen.activity.EnterPinActivity;
import com.via.letmein.R;
import com.via.letmein.persistence.api.Session;
import com.via.letmein.ui.main_activity.MainActivity;
import com.via.letmein.ui.opening_activity.OpeningActivity;

import java.util.Objects;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        PreferenceScreen preferenceScreen = getPreferenceScreen();
        Preference resetPin = preferenceScreen.findPreference("reset_pin");
        if (resetPin != null)
            resetPin.setOnPreferenceClickListener(preference1 -> {
                Intent intent = EnterPinActivity.getIntent(getContext(), true);
                startActivity(intent);
                return true;
            });

        Preference resetSessionID = preferenceScreen.findPreference("reset_session_id");
        if (resetSessionID != null)
            resetSessionID.setOnPreferenceClickListener(preference -> {
                ((MainActivity) Objects.requireNonNull(getActivity())).login();
                return true;
            });


        Preference resetSession = preferenceScreen.findPreference("reset_app");
        if (resetSession != null)
            resetSession.setOnPreferenceClickListener(preference -> {
                Session.getInstance(getContext()).wipeSession();
                startActivity(new Intent(getContext(), OpeningActivity.class));
                getFragmentManager().popBackStack();
                return true;
            });

    }
}