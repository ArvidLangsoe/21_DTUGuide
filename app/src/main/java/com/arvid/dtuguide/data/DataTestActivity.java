package com.arvid.dtuguide.data;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.arvid.dtuguide.R;

public class DataTestActivity extends AppCompatActivity implements IGPSObserver {
    MarshmallowPermission marshMallowPermission;

    private boolean hasGPSPermission() {
        marshMallowPermission = new MarshmallowPermission(DataTestActivity.this);

        if (!marshMallowPermission.checkPermissionForExternalStorage()) {
            marshMallowPermission.requestPermissionForExternalStorage(MarshmallowPermission.EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE_BY_LOAD_PROFILE);
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MarshmallowPermission.EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE_BY_LOAD_PROFILE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //permission granted successfully

                } else {

                    //permission denied

                }
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_test);

        Log.d("MY CURRENT LOCATION: ", " TEST");

            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            GPS testgps = new GPS(locationManager);

            testgps.addObserver(this);

            Log.d("MY CURRENT LOCATION: ", " TEST6");

    }

    public void updateGPSLocation(String position) {
        Log.d("MY CURRENT LOCATION: ", position);
    }
}
