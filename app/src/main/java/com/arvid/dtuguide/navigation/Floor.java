package com.arvid.dtuguide.navigation;

import com.arvid.dtuguide.data.Landmark;
import com.arvid.dtuguide.data.Location;
import com.arvid.dtuguide.data.LocationDTO;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.Marker;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by arvid on 11-01-2018.
 */

public class Floor {

    ArrayList<GroundOverlay> mapOverlay;
    ArrayList<Location> landmarks;
    ArrayList<Marker> activeMarkers;

    public void showFloor(){
        for(GroundOverlay o : mapOverlay)
            o.setVisible(true);

        if(activeMarkers!=null) {
            for(Marker m : activeMarkers)
                m.setVisible(true);
        }
    }

    public void hideFloor(){
        for(GroundOverlay o : mapOverlay)
            o.setVisible(false);

        if(activeMarkers!=null) {
            for (Marker m : activeMarkers)
                m.setVisible(false);
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
        return this;
    }

    public Floor addOverlay(GroundOverlay overlay){
        if(mapOverlay==null){
            mapOverlay = new ArrayList<GroundOverlay>();
        }
        mapOverlay.add(overlay);
        return this;
    }
}
