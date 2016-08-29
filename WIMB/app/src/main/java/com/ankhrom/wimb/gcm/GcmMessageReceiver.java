package com.ankhrom.wimb.gcm;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ankhrom.base.Base;
import com.ankhrom.base.common.NotificationHelper;
import com.ankhrom.base.common.statics.StringHelper;
import com.ankhrom.gcm.PlayService;
import com.ankhrom.wimb.MainActivity;
import com.ankhrom.wimb.R;
import com.ankhrom.wimb.fire.FireEntity;
import com.ankhrom.wimb.fire.FireLocation;
import com.google.android.gms.gcm.GcmListenerService;

public class GcmMessageReceiver extends GcmListenerService {

    public static final String KEY = "key";

    @Override
    public void onMessageReceived(String from, Bundle bundle) {

        String key = bundle.getString(KEY, null);
        String sid = bundle.getString(FireEntity.SID, null);

        Base.logI(PlayService.GCM, "gcm message from: " + from);
        Base.logI(PlayService.GCM, "gcm message key: " + key);

        if (StringHelper.isEmpty(key)) {
            return;
        }

        switch (key) {
            case FireEntity.GEO:
                FireLocation.update(this, sid);
                break;
            case FireEntity.GEO_RESULT:
                Intent intent = new Intent(GeoBooResultReceiver.INTENT_KEY);
                intent.putExtra(FireEntity.SID, sid);
                getApplicationContext().sendBroadcast(intent);
                break;
            case FireEntity.NOTIFY:
                String title = bundle.getString(FireEntity.CREDENTIALS, null);
                String message = bundle.getString(FireEntity.MESSAGE, null);
                NotificationHelper.notify(getBaseContext(), MainActivity.class, null, R.mipmap.ic_launcher, R.color.colorAccent, R.mipmap.ic_launcher, title, message, 0);
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
