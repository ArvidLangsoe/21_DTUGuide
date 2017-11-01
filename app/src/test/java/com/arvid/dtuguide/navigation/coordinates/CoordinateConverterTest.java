package com.arvid.dtuguide.navigation.coordinates;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by arvid on 26-10-2017.
 */

public class CoordinateConverterTest {

    @Test
    public void givenTwoStartCoordinatesWhenConvertingGeoPointToMapPointReturnCorrectMapPoint() throws Exception {
        GeoPoint geo1 = new GeoPoint();
        geo1.setLat(55.731506);
        geo1.setLong(12.395786);

        GeoPoint geo2 = new GeoPoint();
        geo2.setLat(55.732621);
        geo2.setLong(12.394837);

        GeoPoint geo3 = new GeoPoint();
        geo3.setLat(55.732153);
        geo3.setLong(12.396065);


        MapPoint map1 = new MapPoint();
        map1.setX(673);
        map1.setY(420);

        MapPoint map2 = new MapPoint();
        map2.setX(496);
        map2.setY(51);

        MapPoint map3 = new MapPoint();
        map3.setX(725);
        map3.setY(206);

        CoordinateConverter cc = new CoordinateConverter(geo1,geo2,map1,map2);

        MapPoint aMap3 =cc.geoToMap(geo3);
        System.out.println("actual:" +  aMap3.getX()+" "+aMap3.getY());
        System.out.println("expected:" +map3.getX()+" "+map3.getY());

    }
}
