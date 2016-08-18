package com.ankhrom.wimb.fire;


import android.support.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public abstract class FireQueryListener<T> implements ValueEventListener {

    private final Class clazz;

    public FireQueryListener(Class clazz) {

        this.clazz = clazz;
    }

    public abstract void onDataChanged(@Nullable List<T> data);

    @Override
    @SuppressWarnings("unchecked")
    public void onDataChange(DataSnapshot dataSnapshot) {

        if (dataSnapshot.getChildrenCount() == 0) {
            onDataChanged(null);
            return;
        }

        try {
            List<T> data = new ArrayList<>();

            Iterable<DataSnapshot> snapshots = dataSnapshot.getChildren();
            for (DataSnapshot snapshot : snapshots) {
                Object value = snapshot.getValue();
                if (value != null) {
                    T object = (T) new Gson().fromJson(value.toString(), clazz);
                    data.add(object);
                }
            }

            if (data.size() > 0) {
                onDataChanged(data);
            } else {
                onDataChanged(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            onCancelled(DatabaseError.fromException(e));
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
