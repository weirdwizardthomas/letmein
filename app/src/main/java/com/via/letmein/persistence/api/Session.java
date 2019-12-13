package com.via.letmein.persistence.api;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Session ID & username loading and saving handling. Both are stored in phone's SharedPreferences.
 *
 * @author Tomas Koristka: 291129@via.dk
 */
public class Session {

    //Field names in the shared preferences file
    private static final String SESSION = "session";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String IP_ADDRESS = "ip_address";
    private static final String REGISTERED_KEY = "registered";

    /**
     * Saving and loading of the session id and username.
     */
    private final SharedPreferences preferences;

    /**
     * Single instance of the class
     */
    private static Session instance;

    /**
     * Returns the class' single instance.
     *
     * @return Singleton instance of this class.
     */
    public static synchronized Session getInstance(Context context) {
        if (instance == null)
            instance = new Session(context);
        return instance;
    }

    private Session(Context context) {
        preferences = context.getSharedPreferences(SESSION, MODE_PRIVATE);
    }

    public String getIpAddress() {
        return preferences.getString(IP_ADDRESS, null);
    }

    public void setIpAddress(String ipAddress) {
        preferences.edit().putString(IP_ADDRESS, ipAddress).apply();
    }

    public String getPassword() {
        return preferences.getString(PASSWORD, null);
    }

    public void setPassword(String password) {
        preferences.edit().putString(PASSWORD, password).apply();
    }

    public String getSessionId() {
        return preferences.getString(SESSION, null);
    }

    public void setSessionId(String id) {
        preferences.edit().putString(SESSION, id).apply();
    }

    public boolean isRegistered() {
        return preferences.getBoolean(REGISTERED_KEY, false);
    }

    public void setRegistered(boolean registered) {
        preferences.edit().putBoolean(REGISTERED_KEY, registered).apply();
    }

    public String getUsername() {
        return preferences.getString(USERNAME, null);
    }

    public void setUsername(String username) {
        preferences.edit().putString(USERNAME, username).apply();
    }

    /**
     * Removes all stored records of the session
     */
    public void wipeSession() {
        preferences.edit().clear().apply();
    }


}
