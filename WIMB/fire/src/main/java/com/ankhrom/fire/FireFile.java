package com.ankhrom.fire;


import android.support.annotation.NonNull;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FireFile {

    private final String url;
    private final FirebaseStorage storage;

    private String folder;

    private FireFile(String url) {

        this.url = url;
        this.storage = FirebaseStorage.getInstance();
    }

    public static FireFile with(String url) {

        return new FireFile(url);
    }

    public FireFile folder(@NonNull String name) {

        if (folder == null) {
            folder = name;
        } else {
            folder += name;
        }

        if (!folder.endsWith("/")) {
            folder += "/";
        }

        return this;
    }

    public StorageReference get() {

        StorageReference ref;

        if (url == null) {
            ref = storage.getReference();
        } else {
            ref = storage.getReferenceFromUrl(url);
        }

        if (folder == null) {
            return ref;
        }

        return ref.child(folder);
    }

    public StorageReference file(@NonNull String name) {

        return get().child(name);
    }
}
