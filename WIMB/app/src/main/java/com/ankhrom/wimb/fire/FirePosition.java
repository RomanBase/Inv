package com.ankhrom.wimb.fire;


import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.ankhrom.base.common.statics.StringHelper;
import com.ankhrom.fire.FireData;
import com.ankhrom.location.CoarseLocation;
import com.ankhrom.wimb.entity.AppUser;
import com.ankhrom.wimb.entity.BooGeo;
import com.firebase.geofire.GeoLocation;

public class FirePosition {

    public static void update(@NonNull final Context context) {

        new CoarseLocation(context).setLocationListener(new CoarseLocation.LocationObtainedListener() {
            @Override
            public void onLocationObtained(double lat, double lon) {

                String sid = PreferenceManager.getDefaultSharedPreferences(context).getString(AppUser.SID, null);

                if (StringHelper.isEmpty(sid)) {
                    return;
                }

                FireData.init()
                        .root(BooGeo.KEY)
                        .geo()
                        .set(sid, new GeoLocation(lat, lon));
            }
        }).requestLocation();
    }
}
