package com.ankhrom.gcm;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ankhrom.gcm.logic.GcmServerSideSender;
import com.ankhrom.gcm.logic.LoggingService;
import com.ankhrom.gcm.logic.Message;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

public class GcmMessage {

    private final Context context;
    private final GoogleCloudMessaging gcm;

    public GcmMessage(Context context) {

        this.context = context;
        gcm = GoogleCloudMessaging.getInstance(context);
    }

    public void send(@NonNull final String id, @NonNull final String messageId, @Nullable final Bundle data) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                try {
                    Log.i(PlayService.GCM, "send message");
                    gcm.send(id + "@gcm.googleapis.com", messageId, data);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }.execute();
    }

    public void send(@NonNull final String apiKey, @NonNull final String id) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                try {
                    GcmServerSideSender sender = new GcmServerSideSender(apiKey, new LoggingService.Logger(context));

                    sender.sendHttpPlaintextDownstreamMessage(id, new Message.Builder().addData("0", "s msg").build());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }.execute();


    }
}
