package com.ankhrom.wimb.fire;


import android.content.SharedPreferences;

import com.ankhrom.base.common.statics.ObjectHelper;
import com.ankhrom.fire.FireData;
import com.ankhrom.gcm.GcmPrefs;
import com.ankhrom.wimb.FireFactory;
import com.ankhrom.wimb.entity.BooToken;

public class TokenChangedListener implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final FireFactory factory;

    public TokenChangedListener(FireFactory factory) {

        this.factory = factory;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

        if (ObjectHelper.equals(GcmPrefs.NOTIFICATION_TOKEN, s)) {

            if (factory.activeUser != null) {

                String token = sharedPreferences.getString(GcmPrefs.NOTIFICATION_TOKEN, null);

                FireData.init()
                        .root(BooToken.KEY)
                        .get(factory.activeUser.sid)
                        .setValue(token);
            }
        }
    }
}
