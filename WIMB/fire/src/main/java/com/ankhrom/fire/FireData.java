package com.ankhrom.fire;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FireData {

    public final FirebaseDatabase database;
    public DatabaseReference root;

    private ValueEventListener listener;
    private ValueEventListener subscribeListener;

    private FireData(FirebaseDatabase database) {

        this.database = database;
    }

    public static String uid() {

        DatabaseReference uidRef = init().database.getReference().push();

        String uid = uidRef.getKey();

        uidRef.removeValue();

        return uid;
    }

    public static FireData init() {

        return with(FirebaseDatabase.getInstance());
    }

    public static FireData with(@NonNull FirebaseDatabase database) {

        return new FireData(database);
    }

    public static FireData with(@Nullable DatabaseReference reference) {

        FireData data = new FireData(reference == null ? FirebaseDatabase.getInstance() : reference.getDatabase());
        data.root = reference;

        return data;
    }

    public FireData listener(ValueEventListener listener) {

        this.listener = listener;

        return this;
    }

    public FireData subscribe(ValueEventListener listener) {

        this.subscribeListener = listener;

        return this;
    }

    public FireData root(String key) {

        if (root == null) {
            root = database.getReference(key);
        } else {
            root = root.child(key);
        }

        return this;
    }

    public DatabaseReference get(String key) {

        DatabaseReference ref;

        if (root == null) {
            ref = database.getReference(key);
        } else {
            ref = root.child(key);
        }

        if (listener != null) {
            ref.addListenerForSingleValueEvent(listener);
            listener = null;
        }

        if (subscribeListener != null) {
            ref.addValueEventListener(subscribeListener);
            subscribeListener = null;
        }

        return ref;
    }

    public FireGeo geo() {

        DatabaseReference ref;

        if (root == null) {
            ref = database.getReference();
        } else {
            ref = root;
        }

        return geo(ref);
    }

    public FireGeo geo(String key) {

        DatabaseReference ref;

        if (root == null) {
            ref = database.getReference(key);
        } else {
            ref = root.child(key);
        }

        return geo(ref);
    }

    public FireGeo geo(DatabaseReference ref) {

        return FireGeo.with(ref);
    }

    public FireQuery search(String field) {

        DatabaseReference ref;

        if (root == null) {
            ref = database.getReference();
        } else {
            ref = root;
        }

        return search(field, ref);
    }

    public FireQuery search(String field, DatabaseReference ref) {

        FireQuery query = FireQuery.with(ref.orderByChild(field));
        query.listener(listener);

        return query;
    }

    public static String asString(Object data) {

        return "\"" + data.toString() + "\"";
    }
}
