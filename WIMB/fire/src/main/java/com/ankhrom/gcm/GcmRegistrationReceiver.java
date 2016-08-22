package com.ankhrom.gcm;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class GcmRegistrationReceiver extends BroadcastReceiver {

    private boolean isRegistered;

    @Override
    public void onReceive(Context context, Intent intent) {

        GcmPrefs prefs = new GcmPrefs(context);

        boolean isRegistered = prefs.getTokenStatus();
        Log.v(PlayService.GCM, "token status: " + isRegistered);
    }

    public void register(Context context) {

        if (!isRegistered) {
            isRegistered = true;
            LocalBroadcastManager.getInstance(context).registerReceiver(this, new IntentFilter(GcmPrefs.REGISTRATION_COMPLETE));
        }
    }

    public void unregister(Context context) {

        if (isRegistered) {
            isRegistered = false;
            LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
        }
    }
}
