package com.arvid.dtuguide.navigation;

import com.arvid.dtuguide.data.LocationDTO;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by arvid on 15-01-2018.
 */

public class MarkerInfo {
    public LocationDTO location;
    public Marker marker;

    public MarkerInfo(LocationDTO location, Marker marker){
        this.location=location;
        this.marker=marker;

    }
}
