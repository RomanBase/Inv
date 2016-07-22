package com.dontpanic.inv.debug;


import com.dontpanic.base.Base;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class DebugValueListener implements ValueEventListener {

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        Base.log(dataSnapshot.getKey());
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

        Base.logE(databaseError.getMessage());
    }
}
