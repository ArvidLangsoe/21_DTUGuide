package com.arvid.dtuguide.navigation;

import com.arvid.dtuguide.MapChangeObserver;
import com.arvid.dtuguide.navigation.coordinates.MapPoint;

import java.util.ArrayList;

/**
 * Created by arvid on 01-11-2017.
 */

public class NavigationController implements Navigation{

    private MapPoint myLocation=new MapPoint(50,50);

    private ArrayList<MapChangeObserver> myObservers= new ArrayList<MapChangeObserver>();


    @Override
    public MapPoint getMyLocation() {
        return myLocation;
    }

    public void setRandomMyLocation(){
        myLocation=new MapPoint((int)(Math.random()*4000+100),(int)(Math.random()*1000+500));
        System.out.println(myLocation);
    }

}
