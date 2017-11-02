package com.arvid.dtuguide.data;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.arvid.dtuguide.navigation.coordinates.GeoPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peter on 11/1/2017.
 */

public class GPS implements LocationListener{
    private static List<IGPSObserver> observers = new ArrayList<IGPSObserver>();

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    String lat;
    String provider;
    protected String latitude,longitude;
    protected boolean gps_enabled,network_enabled;

    private GeoPoint position;


    public void addObserver(IGPSObserver observer){
        Log.d("GPS", "Observer added");

        observers.add(observer);
    }


    public GeoPoint getPosition(){
         return position;
    }

    @Override
    public void onLocationChanged(Location location) {
        GeoPoint geoLoc = new GeoPoint(location.getLongitude(), location.getLatitude());

        this.position = geoLoc;

        for(IGPSObserver obs:observers){
            obs.updateGPSLocation(geoLoc);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        for(IGPSObserver obs:observers){
            obs.notifyStatusChanged(provider, status, extras);
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        for(IGPSObserver obs:observers){
            obs.notifyProviderStatus(true);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        for (IGPSObserver obs : observers) {
            obs.notifyProviderStatus(false);
        }
    }
}
