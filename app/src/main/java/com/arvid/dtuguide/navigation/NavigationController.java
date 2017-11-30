package com.arvid.dtuguide.navigation;

import android.location.Location;
import android.util.Log;

import com.arvid.dtuguide.data.LocationDAO;
import com.arvid.dtuguide.data.LocationDTO;
import com.arvid.dtuguide.navigation.coordinates.CoordinateConverter;
import com.arvid.dtuguide.navigation.coordinates.GeoPoint;
import com.arvid.dtuguide.navigation.coordinates.MapPoint;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import static com.arvid.dtuguide.Main2Activity.TAG;

/**
 * Created by arvid on 01-11-2017.
 */

public class NavigationController implements Navigation{

    private GeoPoint myLocation=new GeoPoint(12.395167,55.732010);

    private CoordinateConverter coorconv;

    private LocationDAO dao;
    private List<String> historyList = new ArrayList<String>();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Locations");

    public NavigationController(){
    }

    public NavigationController(LocationDAO dao){
        this.dao = dao;
        updateDataFromFireBase();
    }


    public void updateDataFromFireBase(){
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {};
                ArrayList<String> map = (ArrayList<String>) dataSnapshot.getValue();

                dao.setLocations(new HashMap<String, LocationDTO>());

                for(String location : map){
                    dao.saveLocation((dao.parseToDTO(location)));
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
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

        if(historyList.contains(dto.getName()))
            historyList.remove(dto.getName());

        else if(historyList.size()==10)
            historyList.remove(historyList.get(0));

        historyList.add(dto.getName());

        return dto;
    }

    public List<String> searchMatch(String matchString) throws LocationDAO.DAOException {
        List<String> locations = new ArrayList<String>();

        for(LocationDTO dto : dao.getLocations().values()){
            if(dto.getName().toLowerCase().matches("(.*)"+matchString+"(.*)")){
                locations.add(dto.getName());
            }
        }

        Collections.sort(locations);
        return locations;
    }

    public List<String> getHistoryList(){
        Collections.reverse(historyList);

        return historyList;
    }


}
