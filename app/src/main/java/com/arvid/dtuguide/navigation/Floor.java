package com.arvid.dtuguide.navigation;

import com.arvid.dtuguide.R;
import com.arvid.dtuguide.Settings;
import com.arvid.dtuguide.data.Landmark;
import com.arvid.dtuguide.data.Location;
import com.arvid.dtuguide.data.LocationDTO;
import com.arvid.dtuguide.data.MARKTYPE;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by arvid on 11-01-2018.
 */

public class Floor {

    ArrayList<GroundOverlay> mapOverlay;
    ArrayList<LandMarkInfo> landMarks;
    ArrayList<Marker> activeMarkers;
    GoogleMap map;


    class LandMarkInfo{
        public LocationDTO location;
        public Marker marker;

        public LandMarkInfo(LocationDTO location, Marker marker){
            this.location=location;
            this.marker=marker;
        }
    }

    public void showFloor(){
        for(GroundOverlay o : mapOverlay)
            o.setVisible(true);

        if(activeMarkers!=null) {
            for(Marker m : activeMarkers)
                m.setVisible(true);
        }
        if(landMarks!=null) {
            Settings settings = Settings.getInstance();
            if(map.getCameraPosition().zoom>17.5) {
                for (LandMarkInfo l : landMarks) {
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
            for (LandMarkInfo l : landMarks)
                l.marker.setVisible(false);
        }



    }

    public Floor addMarker(Marker activeMarker){
        if(activeMarkers==null){
            activeMarkers=new ArrayList<Marker>();
        }
        activeMarkers.add(activeMarker);
        return this;
    }

    public Floor removeMarkers(){
        if(activeMarkers!=null) {
            for (Marker m : activeMarkers) {
                m.remove();
            }
            activeMarkers=null;
        }

        return this;
    }

    public Floor addLandmark(GoogleMap map, LocationDTO loc){
        this.map=map;
        if(landMarks==null){
            landMarks = new ArrayList<LandMarkInfo>();
        }
        Marker marker = map.addMarker(new MarkerOptions()
                .position(loc.getPosition())
                .title(loc.getName())
                .icon(BitmapDescriptorFactory.fromResource(getPictureId(loc.getLandmark()))).alpha(0.9f)
        );
        marker.setVisible(false);
        landMarks.add(new LandMarkInfo(loc,marker));
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
