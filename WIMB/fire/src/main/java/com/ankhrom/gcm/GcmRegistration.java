package com.ankhrom.gcm;


import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.ankhrom.fire.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

public class GcmRegistration extends IntentService {

    public GcmRegistration() {
        super("wimb_n_r");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        GcmPrefs prefs = new GcmPrefs(this);

        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_sender_id), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            onTokenObtained(token);
            prefs.setToken(token);
        } catch (IOException e) {
            e.printStackTrace();
            prefs.setToken(null);
        }

        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(GcmPrefs.REGISTRATION_COMPLETE));
    }

    private void onTokenObtained(String token) throws IOException {

        Log.v(PlayService.GCM, "gcm token: " + token);
    }

    public static void start(Context context) {

        context.startService(new Intent(context, GcmRegistration.class));
    }
}
