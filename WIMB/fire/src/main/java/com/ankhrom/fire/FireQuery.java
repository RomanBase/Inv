package com.ankhrom.fire;


import android.support.annotation.NonNull;

import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FireQuery {

    private final Query query;
    private ValueEventListener listener;

    private FireQuery(Query query) {
        this.query = query;
    }

    public static FireQuery with(Query query) {

        return new FireQuery(query);
    }

    public FireQuery listener(ValueEventListener listener) {

        this.listener = listener;

        return this;
    }

    public Query find(@NonNull String key) {

        Query ref = query.equalTo(key);

        if (listener != null) {
            ref.addListenerForSingleValueEvent(listener);
        }

        return ref;
    }
}
