package com.ankhrom.gcm;


import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

public class GcmTokenID extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {

        startService(new Intent(this, GcmRegistration.class));
    }
}
