package com.ankhrom.gcm;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class GcmPrefs {

    public static final String NOTIFICATION_TOKEN = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";

    private final SharedPreferences prefs;

    public GcmPrefs(Context context) {

        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setToken(String token) {

        prefs.edit().putString(NOTIFICATION_TOKEN, token).apply();
    }

    public String getToken() {

        return prefs.getString(NOTIFICATION_TOKEN, null);
    }

    public boolean getTokenStatus() {

        return prefs.getString(NOTIFICATION_TOKEN, null) != null;
    }
}
