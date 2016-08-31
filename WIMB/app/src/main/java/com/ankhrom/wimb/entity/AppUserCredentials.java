package com.ankhrom.wimb.entity;

import android.support.annotation.Keep;

@Keep
public class AppUserCredentials {

    public String nickname;
    public String avatar;
    public String location;
    public GeoLatLng geo;
    public long lastUpdate;
}
