package com.ankhrom.gcm;


import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.ankhrom.gcm.logic.GcmServerSideSender;
import com.ankhrom.gcm.logic.LoggingService;
import com.ankhrom.gcm.logic.Message;

import java.io.IOException;

public class GcmMessage {

    private final Context context;

    public GcmMessage(Context context) {

        this.context = context;
    }

    public void send(@NonNull final String apiKey, @NonNull final String id, @NonNull final Message message) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                try {
                    GcmServerSideSender sender = new GcmServerSideSender(apiKey, new LoggingService.Logger(context));

                    sender.sendHttpPlaintextDownstreamMessage(id, message);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }.execute();


    }
}
