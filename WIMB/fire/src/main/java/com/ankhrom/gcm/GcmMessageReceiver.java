package com.ankhrom.gcm;


import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

public class GcmMessageReceiver extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle bundle) {

        Log.i(PlayService.GCM, "gcm message from: " + from);
        Log.i(PlayService.GCM, "gcm message data: " + bundle.getString("0"));
    }

    @Override
    public void onMessageSent(String s) {

        Log.i(PlayService.GCM, "send: " + s);
    }

    @Override
    public void onSendError(String s, String s1) {

        Log.e(PlayService.GCM, "send error: " + s + " " + s1);
    }
}
