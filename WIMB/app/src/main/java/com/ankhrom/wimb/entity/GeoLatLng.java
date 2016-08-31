package com.ankhrom.wimb.entity;

public class GeoLatLng {

    public double lat;
    public double lng;

    public static GeoLatLng init(double lat, double lng) {

        GeoLatLng geo = new GeoLatLng();
        geo.lat = lat;
        geo.lng = lng;

        return geo;
    }
}
