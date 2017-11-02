package com.arvid.dtuguide.navigation.coordinates;

/**
 * Created by arvid on 23-10-2017.
 */

public class GeoPoint {
    private double latitude;
    private double longtitude;

    public GeoPoint(){

    }

    public GeoPoint(double longtitude,double latitude){
        this.latitude=latitude;
        this.longtitude= longtitude;
    }

    public double getLat() {
        return latitude;
    }

    public void setLat(double latitude) {
        this.latitude = latitude;
    }

    public double getLong() {
        return longtitude;
    }

    public void setLong(double longtitude) {
        this.longtitude = longtitude;
    }
}
