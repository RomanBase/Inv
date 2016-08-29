package com.ankhrom.wimb.gcm;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;

import com.ankhrom.base.BaseActivity;
import com.ankhrom.base.interfaces.viewmodel.ViewModelObserver;
import com.ankhrom.wimb.fire.FireArgCode;
import com.ankhrom.wimb.fire.FireEntity;

public class GeoBooResultReceiver extends BroadcastReceiver {

    public static final String INTENT_KEY = "wimb_geo_result";

    private final BaseActivity activity;

    private boolean isRegistered;

    public GeoBooResultReceiver(@NonNull BaseActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        ViewModelObserver observer = activity.getViewModelObserver();

        if (observer != null) {
            observer.postArgsToViewModel(FireArgCode.USER_DATA_CHANGED, intent.getStringExtra(FireEntity.SID));
        }
    }

    public void register() {

        if (!isRegistered) {
            isRegistered = true;
            activity.registerReceiver(this, new IntentFilter(INTENT_KEY));
        }
    }

    public void unregister() {

        if (isRegistered) {
            isRegistered = false;
            activity.unregisterReceiver(this);
        }
    }
}
