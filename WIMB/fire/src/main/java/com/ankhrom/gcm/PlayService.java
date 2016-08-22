package com.ankhrom.gcm;


import android.app.Activity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public final class PlayService {

    public static final String GCM = "GCM";

    public static boolean isAvailable(Activity activity) {

        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(activity, resultCode, 9330).show();
            } else {
                Log.e(PlayService.GCM, "This device is not supported.");
            }
            return false;
        }
        return true;
    }

    public static void checkToken() {

    }
}
