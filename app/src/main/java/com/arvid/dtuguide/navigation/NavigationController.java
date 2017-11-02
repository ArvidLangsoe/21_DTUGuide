package com.arvid.dtuguide.navigation;

import com.arvid.dtuguide.navigation.coordinates.CoordinateConverter;
import com.arvid.dtuguide.navigation.coordinates.GeoPoint;
import com.arvid.dtuguide.navigation.coordinates.MapPoint;

/**
 * Created by arvid on 01-11-2017.
 */

public class NavigationController implements Navigation{

    private GeoPoint myLocation=new GeoPoint(12.395167,55.732010);

    private CoordinateConverter coorconv;

    public NavigationController(){
    }

    public void calibrate(GeoPoint g1, GeoPoint g2,MapPoint m1, MapPoint m2){
        coorconv= new CoordinateConverter(g1,g2,m1,m2);
    }

    @Override
    public MapPoint getMyLocation() {
        MapPoint m =coorconv.geoToMap(myLocation);
        System.out.println(m);
        return m;

    }

}
