package com.ankhrom.wimb.fire;


import android.support.annotation.Nullable;

import com.ankhrom.base.Base;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public abstract class FireQuerySingleListener<T> implements ValueEventListener {

    private final Class clazz;

    public FireQuerySingleListener(Class clazz) {

        this.clazz = clazz;
    }

    public abstract void onDataChanged(@Nullable T data);

    @Override
    @SuppressWarnings("unchecked")
    public void onDataChange(DataSnapshot dataSnapshot) {

        if (dataSnapshot.getChildrenCount() != 1) {
            onDataChanged(null);
            return;
        }

        try {
            T object = (T) new Gson().fromJson(dataSnapshot.getChildren().iterator().next().getValue().toString(), clazz);
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
