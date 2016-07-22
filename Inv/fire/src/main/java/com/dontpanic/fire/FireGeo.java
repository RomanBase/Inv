package com.dontpanic.fire;


import android.support.annotation.NonNull;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.LocationCallback;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

public class FireGeo {

    public final GeoFire geo;
    private GeoStatusListener listener;

    private FireGeo(GeoFire geo) {

        this.geo = geo;
    }

    public static FireGeo with(DatabaseReference ref) {

        return new FireGeo(new GeoFire(ref));
    }

    public FireGeo listener(GeoStatusListener listener) {

        this.listener = listener;

        return this;
    }

    public FireGeo set(@NonNull String key, @NonNull GeoLocation location) {

        final GeoLocation geoLocation = location;

        geo.setLocation(key, location, new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {

                if (listener == null) {
                    return;
                }

                if (error == null) {
                    listener.onFireLocation(key, geoLocation);
                } else {
                    listener.onFireLocationError(key, error);
                }
            }
        });

        return this;
    }

    public FireGeo get(@NonNull String key) {

        final String geoKey = key;

        geo.getLocation(key, new LocationCallback() {
            @Override
            public void onLocationResult(String key, GeoLocation location) {

                if (listener != null) {
                    listener.onFireLocation(key, location);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                if (listener != null) {
                    listener.onFireLocationError(geoKey, databaseError);
                }
            }
        });

        return this;
    }

    public FireGeo remove(@NonNull String key) {

        geo.removeLocation(key, new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {

                if (listener == null) {
                    return;
                }

                if (error == null) {
                    listener.onFireLocation(key, null);
                } else {
                    listener.onFireLocationError(key, error);
                }
            }
        });

        return this;
    }

    public static interface GeoStatusListener {

        void onFireLocation(String key, GeoLocation location);

        void onFireLocationError(String key, DatabaseError error);
    }
}
