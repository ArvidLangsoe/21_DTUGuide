package com.arvid.dtuguide.navigation;

import android.content.Context;

import com.arvid.dtuguide.R;
import com.arvid.dtuguide.Settings;
import com.arvid.dtuguide.data.LocationDTO;
import com.arvid.dtuguide.data.MARKTYPE;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by arvid on 11-01-2018.
 */

public class Floor {

    ArrayList<GroundOverlay> mapOverlay;
    ArrayList<MarkerInfo> landMarks;
    ArrayList<Marker> activeMarkers;
    GoogleMap map;
    Context appContext;

    public static HashMap<Marker,MarkerInfo> markerInfoLookup;

    public Floor(Context appContext){
        this.appContext=appContext;
    }


    public void showFloor(){
        if(map==null){
            return;
        }
        for(GroundOverlay o : mapOverlay)
            o.setVisible(true);

        if(activeMarkers!=null) {
            for(Marker m : activeMarkers)
                m.setVisible(true);
        }
        if(landMarks!=null) {
            Settings settings = Settings.getInstance(appContext);
            if(map.getCameraPosition().zoom> settings.getGoogleZoom()) {
                for (MarkerInfo l : landMarks) {
                    System.out.println("LANDMARK: " + l.location.getLandmark());
                    if (settings.isVisible(l.location.getLandmark()))
                        l.marker.setVisible(true);
                }
            }
        }

    }

    public void hideFloor(){
        for(GroundOverlay o : mapOverlay)
            o.setVisible(false);


        if(activeMarkers!=null) {
            for (Marker m : activeMarkers)
                m.setVisible(false);
        }

        if(landMarks!=null) {
            for (MarkerInfo l : landMarks)
                l.marker.setVisible(false);
        }



    }

    public Floor addMarker(Marker activeMarker,LocationDTO location){
        if(activeMarkers==null){
            activeMarkers=new ArrayList<Marker>();
        }
        activeMarkers.add(activeMarker);
        markerInfoLookup.put(activeMarker,new MarkerInfo(location, activeMarker));
        return this;
    }

    public Floor removeMarkers(){
        if(activeMarkers!=null) {
            for (Marker m : activeMarkers) {
                m.remove();
                markerInfoLookup.remove(m);
            }
            activeMarkers=null;
        }

        return this;
    }

    public Floor addLandmark(GoogleMap map, LocationDTO loc){
        this.map=map;
        if(map== null){
            return null;
        }
        if(landMarks==null){
            landMarks = new ArrayList<MarkerInfo>();
        }
        if(markerInfoLookup==null){
            markerInfoLookup = new HashMap<Marker, MarkerInfo>();
        }

        Marker marker = map.addMarker(new MarkerOptions()
                .position(loc.getPosition())
                .title(loc.getName())
                .icon(BitmapDescriptorFactory.fromResource(getPictureId(loc.getLandmark()))).alpha(0.9f)
        );
        marker.setVisible(false);
        MarkerInfo landMark = new MarkerInfo(loc,marker);
        landMarks.add(new MarkerInfo(loc,marker));
        markerInfoLookup.put(marker,landMark);
        return this;
    }

    public Floor addOverlay(GroundOverlay overlay){
        if(mapOverlay==null){
            mapOverlay = new ArrayList<GroundOverlay>();
        }
        mapOverlay.add(overlay);
        return this;
    }

    private int getPictureId(MARKTYPE landmark){

        switch(landmark){
            case WC:
                return R.drawable.landmark_wc;
            case WC_HANDICAP:
                return R.drawable.landmark_wc_handicap;
            case SHOP:
                return R.drawable.landmark_shop;
            case CANTEEN:
                return R.drawable.landmark_resturant;
            case KITCHEN:
                return R.drawable.landmark_kitchen;
            case LIBRARY:
                return R.drawable.landmark_library;
            case WATER_FOUNTAIN:
                return R.drawable.landmark_water;
            case ENTRANCE:
                return R.drawable.landmark_entrance;
            case STAIRS_UP:
                return R.drawable.landmark_stairs_up;
            case STAIRS_DOWN:
                return R.drawable.landmark_stairs_down;
            case STAIRS_UP_DOWN:
                return R.drawable.landmark_stairs_up_down;
            case ELEVATOR_UP:
                return R.drawable.landmark_elevator_up;
            case ELEVATOR_DOWN:
                return R.drawable.landmark_elevator_down;
            case ELEVATOR_UP_DOWN:
                return R.drawable.landmark_elevator;
            default:
                return R.drawable.landmark;
        }

    }
}
