package com.ankhrom.wimb.fire;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ankhrom.base.common.statics.StringHelper;
import com.ankhrom.fire.FireData;
import com.ankhrom.location.CoarseLocation;
import com.ankhrom.wimb.entity.GeoLatLng;
import com.ankhrom.wimb.gcm.WimbMessage;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ServerValue;

import java.io.IOException;
import java.util.List;

public class FireLocation {

    public static void update(@NonNull final Context context, @Nullable final String sidToken) {

        new CoarseLocation(context).setLocationListener(new CoarseLocation.LocationObtainedListener() {
            @Override
            public void onLocationObtained(double lat, double lng) {

                final String sid = PreferenceManager.getDefaultSharedPreferences(context).getString(FireEntity.SID, null);

                if (StringHelper.isEmpty(sid)) {
                    return;
                }

                FireData.init()
                        .root(FireEntity.GEO)
                        .geo()
                        .set(sid, new GeoLocation(lat, lng));

                FireData credentials = FireData.init().root(FireEntity.CREDENTIALS).root(sid);

                String address = null;
                Geocoder geo = new Geocoder(context);
                try {
                    List<Address> addresses = geo.getFromLocation(lat, lng, 1);
                    if (addresses.isEmpty()) {

                    } else {
                        Address location = addresses.get(0);
                        address = StringHelper.buildStruct(", ", location.getAddressLine(0), location.getAddressLine(1));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (StringHelper.isEmpty(address)) {
                    address = lat + " " + lng;
                }

                credentials.get(FireEntity.LOCATION).setValue(FireData.asString(address));
                credentials.get(FireEntity.GEO).setValue(GeoLatLng.init(lat, lng));
                credentials
                        .listener(sidToken == null ? null : new FireValueAdapterListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                FireData.init()
                                        .listener(new FireValueListener<String>(String.class) {

                                            @Override
                                            public void onDataChanged(@Nullable String token) {

                                                WimbMessage.with(context, FireEntity.GEO_RESULT)
                                                        .data(FireEntity.CREDENTIALS, sid)
                                                        .sendTo(token);
                                            }
                                        })
                                        .root(FireEntity.TOKEN)
                                        .get(sidToken);
                            }
                        })
                        .get(FireEntity.LAST_UPDATE)
                        .setValue(ServerValue.TIMESTAMP);
            }
        }).requestLocation();
    }
}
