package com.arvid.dtuguide.navigation;

import android.util.Log;

import com.arvid.dtuguide.data.LocationDAO;
import com.arvid.dtuguide.data.LocationDTO;
import com.arvid.dtuguide.navigation.coordinates.GeoPoint;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.arvid.dtuguide.Main2Activity.TAG;

/**
 * Created by arvid on 01-11-2017.
 */

public class NavigationController implements Navigation{

    private GeoPoint myLocation=new GeoPoint(12.395167,55.732010);



    private LocationDAO dao;
    private static List<String> historyList = new ArrayList<String>();

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



    public LocationDTO getLocation(String name) throws LocationDAO.DAOException {
        LocationDTO dto = dao.getLocation(name);

        if(historyList.contains(dto.getName()))
            historyList.remove(dto.getName());

        else if(historyList.size()==10)
            historyList.remove(historyList.get(0));

        historyList.add(dto.getName());
        System.out.println("HISTORYLIST: " + historyList);
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
