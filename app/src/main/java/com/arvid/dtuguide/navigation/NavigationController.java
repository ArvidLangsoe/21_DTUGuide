package com.arvid.dtuguide.navigation;

import android.location.Location;

import com.arvid.dtuguide.data.LocationDAO;
import com.arvid.dtuguide.data.LocationDTO;
import com.arvid.dtuguide.navigation.coordinates.CoordinateConverter;
import com.arvid.dtuguide.navigation.coordinates.GeoPoint;
import com.arvid.dtuguide.navigation.coordinates.MapPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by arvid on 01-11-2017.
 */

public class NavigationController implements Navigation{

    private GeoPoint myLocation=new GeoPoint(12.395167,55.732010);

    private CoordinateConverter coorconv;

    private LocationDAO dao;
    private List<LocationDTO> historyList = new ArrayList<LocationDTO>();

    public NavigationController(){
    }

    public NavigationController(LocationDAO dao){
        this.dao = dao;
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

    public LocationDTO getLocation(String name) throws LocationDAO.DAOException {
        LocationDTO dto = dao.getLocation(name);

        if(historyList.contains(dto))
            historyList.remove(dto);

        else if(historyList.size()==10)
            historyList.remove(historyList.get(0));

        historyList.add(dto);

        return dto;
    }

    public List<LocationDTO> searchMatch(String matchString) throws LocationDAO.DAOException {
        List<LocationDTO> locations = new ArrayList<LocationDTO>();

        for(LocationDTO dto : dao.getLocations().values()){
            if(dto.getName().matches("(.*)"+matchString+"(.*)")){
                locations.add(dto);
            }
        }

        Collections.sort(locations);
        return locations;
    }

    public List<LocationDTO> getHistoryList(){
        Collections.reverse(historyList);

        return historyList;
    }


}
