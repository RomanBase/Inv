package com.ankhrom.wimb.debug;


import com.ankhrom.base.Base;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.firebase.database.DatabaseError;

public class DebugGeoQueryListener implements GeoQueryEventListener {

    @Override
    public void onKeyEntered(String key, GeoLocation location) {
        Base.log("enter", key, location);
    }

    @Override
    public void onKeyExited(String key) {
        Base.log("exit", key);
    }

    @Override
    public void onKeyMoved(String key, GeoLocation location) {
        Base.log("move", key, location);
    }

    @Override
    public void onGeoQueryReady() {
        Base.log("ready");
    }

    @Override
    public void onGeoQueryError(DatabaseError error) {
        Base.log("error", error.getMessage());
    }
}
