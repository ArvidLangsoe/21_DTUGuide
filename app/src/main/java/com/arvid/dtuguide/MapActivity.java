package com.arvid.dtuguide;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.arvid.dtuguide.navigation.coordinates.GeoPoint;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private Marker currentMarker;
    private GroundOverlay ballerupMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        //Delete this for drawn map
        //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setIndoorEnabled(false);

        mMap.setMinZoomPreference(16);
        mMap.setMaxZoomPreference(20);

        // Add a marker in Sydney and move the camera
        LatLng ballerupSW = new LatLng(55.730327, 12.393678);
        LatLng ballerupNE = new LatLng(55.732781, 12.401019);

        LatLng ballerupCenter = new LatLng(55.731543, 12.396680);


        LatLngBounds ballerupBounds = new LatLngBounds(ballerupSW,ballerupNE);

        currentMarker=mMap.addMarker(new MarkerOptions().position(ballerupCenter).title("Ballerup Campus"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ballerupCenter,16f));

        GroundOverlayOptions options =new GroundOverlayOptions();
        options.image(BitmapDescriptorFactory.fromResource(R.drawable.low_res_dtu_map)).positionFromBounds(ballerupBounds);
        ballerupMap = googleMap.addGroundOverlay(options);


        showLocation(new GeoPoint(12.395511,55.731598));
    }

    public void showLocation(GeoPoint point){
        currentMarker.remove();
        LatLng myPoint = new LatLng(point.getLat(),point.getLong());
        currentMarker=mMap.addMarker(new MarkerOptions().position(myPoint).title("Some Point"));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPoint,19f),3000,null);

    }
}
