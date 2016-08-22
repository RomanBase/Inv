package com.ankhrom.wimb.fire;

import android.content.Context;

import com.ankhrom.base.Base;
import com.ankhrom.location.CoarseLocation;
import com.google.firebase.auth.FirebaseUser;

public class FireUser {

    public final FirebaseUser data;

    public FireUser(FirebaseUser data) {
        this.data = data;
    }

    public static void UpdatePosition(Context context) {

        new CoarseLocation(context).setLocationListener(new CoarseLocation.LocationObtainedListener() {
            @Override
            public void onLocationObtained(double lat, double lon) {

                Base.log("location:", lat, lon);
            }
        }).requestLocation();
    }
}
