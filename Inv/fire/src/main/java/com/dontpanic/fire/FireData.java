package com.dontpanic.fire;


import android.support.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FireData {

    public final FirebaseDatabase database;
    public DatabaseReference root;

    private ValueEventListener listener;

    private FireData(FirebaseDatabase database) {

        this.database = database;
    }

    public static FireData init() {

        return with(FirebaseDatabase.getInstance());
    }

    public static FireData with(@NonNull FirebaseDatabase database) {

        return new FireData(database);
    }

    public static FireData with(DatabaseReference reference) {

        FireData data = new FireData(reference == null ? FirebaseDatabase.getInstance() : reference.getDatabase());
        data.root = reference;

        return data;
    }

    public FireData listener(ValueEventListener listener) {

        this.listener = listener;

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
            ref = database.getReference();
        } else {
            ref = root.child(key);
        }

        if (listener != null) {
            ref.removeEventListener(listener);
            ref.addValueEventListener(listener);
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
}
