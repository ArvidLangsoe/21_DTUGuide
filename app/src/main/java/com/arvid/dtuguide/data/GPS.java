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

    private String position;


    public void addObserver(IGPSObserver observer){
        Log.d("MY CURRENT LOCATION ", "Observer added");

        observers.add(observer);
    }


    public String getPosition(){
         return this.position;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("MY CURRENT LOCATION ", "Location changed called");
        String loc = "Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude();
        Log.d("MY CURRENT LOCATION ", loc);

        this.position = loc;

        for(IGPSObserver obs:observers){
            obs.updateGPSLocation(loc);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    Log.d("Latitude","status");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }
}
