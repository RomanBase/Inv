package com.ankhrom.wimb.fire;


import android.support.annotation.Nullable;

import com.ankhrom.base.Base;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public abstract class FireValueListener<T> implements ValueEventListener {

    private final Class clazz;

    public FireValueListener(Class clazz) {

        this.clazz = clazz;
    }

    public abstract void onDataChanged(@Nullable T data);

    @Override
    @SuppressWarnings("unchecked")
    public void onDataChange(DataSnapshot dataSnapshot) {

        Object value = dataSnapshot.getValue();

        if (value == null) {
            onDataChanged(null);
            return;
        }

        try {
            T object = (T) new Gson().fromJson(value.toString(), clazz);
            onDataChanged(object);
        } catch (Exception e) {
            Base.logE(dataSnapshot.getValue());
            e.printStackTrace();
            onCancelled(DatabaseError.fromException(e));
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
