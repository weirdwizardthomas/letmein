package com.via.letmein.ui.settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
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

    public static final String RESET_PIN = "reset_pin";
    public static final String RESET_SESSION_ID = "reset_session_id";
    public static final String RESET_APP = "reset_app";

    private SettingsViewModel settingsViewModel;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);

        setPreferencesFromResource(R.xml.preferences, rootKey);

        PreferenceScreen preferenceScreen = getPreferenceScreen();
        Preference resetPin = preferenceScreen.findPreference(RESET_PIN);
        if (resetPin != null)
            resetPin.setOnPreferenceClickListener(preference -> {
                Intent intent = EnterPinActivity.getIntent(getContext(), true);
                startActivity(intent);
                return true;
            });

        Preference resetSessionID = preferenceScreen.findPreference(RESET_SESSION_ID);
        if (resetSessionID != null)
            resetSessionID.setOnPreferenceClickListener(preference -> {
                ((MainActivity) Objects.requireNonNull(getActivity())).login();
                return true;
            });


        Preference resetSession = preferenceScreen.findPreference(RESET_APP);
        if (resetSession != null)
            resetSession.setOnPreferenceClickListener(preference -> {
                Session.getInstance(getContext()).wipeSession();
                startActivity(new Intent(getContext(), OpeningActivity.class));
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
                return true;
            });


        Preference ipAddress = preferenceScreen.findPreference("ip_address");
        if (ipAddress != null) {
            ipAddress.setCopyingEnabled(true);
            ipAddress.setSelectable(false);
            ipAddress.setSummary(settingsViewModel.getIPAddress());
        }
    }
}