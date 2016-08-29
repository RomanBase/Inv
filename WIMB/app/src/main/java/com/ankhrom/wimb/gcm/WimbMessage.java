package com.ankhrom.wimb.gcm;


import android.content.Context;
import android.preference.PreferenceManager;

import com.ankhrom.gcm.GcmMessage;
import com.ankhrom.gcm.logic.Message;
import com.ankhrom.wimb.R;
import com.ankhrom.wimb.fire.FireEntity;
import com.google.gson.Gson;

public class WimbMessage {

    private final Message.Builder message;
    private final Context context;

    private WimbMessage(Context context, String key) {
        this.context = context;

        message = new Message.Builder()
                .addData(GcmMessageReceiver.KEY, key)
                .addData(FireEntity.SID, PreferenceManager.getDefaultSharedPreferences(context).getString(FireEntity.SID, null));
    }

    public static WimbMessage with(Context context, String key) {

        return new WimbMessage(context, key);
    }

    public WimbMessage data(String key, String data) {

        message.addData(key, data);

        return this;
    }

    public WimbMessage data(String key, Object data) {

        return data(key, new Gson().toJson(data));
    }

    public void sendTo(String token) {

        new GcmMessage(context)
                .send(
                        context.getString(R.string.fire_app_key),
                        token,
                        message.build()
                );
    }
}
