package com.via.letmein.ui;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by aleksandr on 2018/02/09.
 */

class PreferencesSettings {

    private static final String PREF_FILE = "codes";

    static void saveToPref(Context context, String str) {
        final SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("code", str);
        editor.apply();
    }

    static String getCode(Context context) {
        final SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        final String defaultValue = "";
        return sharedPref.getString("code", defaultValue);
    }

}
