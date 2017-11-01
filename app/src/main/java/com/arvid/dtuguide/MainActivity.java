package com.arvid.dtuguide;

import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.alexvasilkov.gestures.views.interfaces.GestureView;

public class MainActivity extends AppCompatActivity {
    private GestureView map;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //map = (GestureView) findViewById(R.id.map);
        //map.getController().getSettings().setDoubleTapEnabled(false); // Falls back to max zoom level
    }
}
