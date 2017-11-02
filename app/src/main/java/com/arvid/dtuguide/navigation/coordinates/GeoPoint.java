package com.arvid.dtuguide.navigation.coordinates;

import java.io.Serializable;

/**
 * Created by arvid on 23-10-2017.
 */

public class GeoPoint implements Serializable {
    private static final long serialVersionUID = 995587864322854456L;

    private double latitude;
    private double longtitude;

    public GeoPoint(double latitude, double longtitude){
        this.latitude=latitude;
        this.longtitude=longtitude;
    }

    public GeoPoint(){}

    public String toString(){
        return "[Lat:"+latitude+"; Long:"+longtitude+"]";
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
