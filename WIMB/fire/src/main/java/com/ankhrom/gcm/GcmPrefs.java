package com.ankhrom.gcm;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class GcmPrefs {

    public static final String NOTIFICATION_TOKEN = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";

    public final SharedPreferences shared;

    public GcmPrefs(Context context) {

        shared = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setToken(String token) {

        shared.edit().putString(NOTIFICATION_TOKEN, token).apply();
    }

    public String getToken() {

        return shared.getString(NOTIFICATION_TOKEN, null);
    }

    public boolean getTokenStatus() {

        return shared.getString(NOTIFICATION_TOKEN, null) != null;
    }
}
