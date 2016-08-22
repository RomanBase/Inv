package com.ankhrom.location;


import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class CoarseLocation implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient googleApiClient;
    private LocationObtainedListener locationListener;

    public CoarseLocation(Context context) {

        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void requestLocation() {

        googleApiClient.connect();
    }

    public CoarseLocation setLocationListener(LocationObtainedListener locationListener) {

        this.locationListener = locationListener;

        return this;
    }

    @Override
    public void onConnected(Bundle bundle) {

        //noinspection MissingPermission
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {

            if (locationListener != null) {
                locationListener.onLocationObtained(location.getLatitude(), location.getLongitude());
            }

            googleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public interface LocationObtainedListener {

        void onLocationObtained(double lat, double lon);
    }
}
