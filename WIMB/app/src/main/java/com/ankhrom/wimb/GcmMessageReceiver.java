package com.ankhrom.wimb;


import android.os.Bundle;
import android.util.Log;

import com.ankhrom.base.Base;
import com.ankhrom.base.common.statics.StringHelper;
import com.ankhrom.gcm.PlayService;
import com.ankhrom.wimb.entity.BooGeo;
import com.ankhrom.wimb.fire.FireUser;
import com.google.android.gms.gcm.GcmListenerService;

public class GcmMessageReceiver extends GcmListenerService {

    private static final String KEY = "key";

    @Override
    public void onMessageReceived(String from, Bundle bundle) {

        String key = bundle.getString(KEY, null);

        Base.logI(PlayService.GCM, "gcm message from: " + from);
        Base.logI(PlayService.GCM, "gcm message key: " + key);

        if (StringHelper.isEmpty(key)) {
            return;
        }

        switch (key) {
            case BooGeo.KEY:
                FireUser.UpdatePosition(this);
                break;
        }
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
