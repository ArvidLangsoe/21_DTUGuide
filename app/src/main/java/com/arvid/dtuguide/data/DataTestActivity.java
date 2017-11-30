package com.arvid.dtuguide.data;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.arvid.dtuguide.R;
import com.arvid.dtuguide.navigation.coordinates.GeoPoint;

public class DataTestActivity extends AppCompatActivity implements IGPSObserver {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_test);

        Log.d("MY CURRENT LOCATION: ", " TEST");

        GPS gps = new GPS();

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1L, 0f, gps);

        gps.addObserver(this);

    }

    public void updateGPSLocation(GeoPoint position) {
        Log.d("MY CURRENT LOCATION: ", position+"");
    }

    @Override
    public void notifyProviderStatus(boolean newStatus) {
        Log.d("GPS: ", newStatus+"");
    }

    @Override
    public void notifyStatusChanged(String provider, int status, Bundle extras) {

    }
}
