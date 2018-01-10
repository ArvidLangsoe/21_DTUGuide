package com.arvid.dtuguide.navigation;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.arvid.dtuguide.data.LocationDAO;
import com.arvid.dtuguide.data.LocationDTO;
import com.arvid.dtuguide.data.MARKTYPE;
import com.arvid.dtuguide.data.Searchable;
import com.arvid.dtuguide.navigation.coordinates.CoordinateConverter;
import com.arvid.dtuguide.navigation.coordinates.GeoPoint;
import com.arvid.dtuguide.navigation.coordinates.MapPoint;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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



    private LocationDAO dao;
    private static List<String> historyList = new ArrayList<String>();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Searchable");

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

                HashMap<String, HashMap<String, HashMap<String, Object>>> map = (HashMap<String, HashMap<String, HashMap<String, Object>>>) dataSnapshot.getValue();

                dao.setData(new HashMap<String, Searchable>());

                //for(String location : map){
                //    dao.saveLocation((dao.parseToDTO(location)));
                //}

                System.out.println("TEST DEBUG : "+map+"");

                HashMap<String, HashMap<String, Object>> locations = map.get("Locations");

                    for(HashMap<String, Object> location:locations.values()) {

                        LatLng geo = new LatLng(
                                ((HashMap<String, Double>) location.get("position")).get("latitude"),
                                ((HashMap<String, Double>) location.get("position")).get("longitude")
                        );

                            LocationDTO dto = (LocationDTO) new LocationDTO()
                                    .setPosition(geo)
                                    .setFloor(Integer.parseInt((String)location.get("floor")))
                                    .setDescription((String)location.get("description"))
                                    .setLandmark(MARKTYPE.valueOf((String)location.get("landmark")))
                                    .setTags((ArrayList<String>) location.get("tags"))
                                    .setName((String) location.get("name"));



                        dao.saveData(dto);
                    }





            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }



    public Searchable getSearchableItem(String name) throws LocationDAO.DAOException {
        Searchable dto = dao.getData(name);

        if(historyList.contains(dto.getName()))
            historyList.remove(dto.getName());

        else if(historyList.size()==10)
            historyList.remove(historyList.get(0));

        historyList.add(dto.getName());
        System.out.println("HISTORYLIST: " + historyList);
        return dto;
    }

    public List<String> searchMatch(String matchString) throws LocationDAO.DAOException {
        List<String> searchData = new ArrayList<String>();
        matchString = matchString.replace(" ", "");

        //Search with name
        for(Searchable dto : dao.getAllData().values()){
            if(dto.getName().replace(".", "").toLowerCase().matches("(.*)"+matchString+"(.*)")
                    || dto.getName().replace(".", "").toLowerCase().matches("(.*)"+matchString+"(.*)")
                    || dto.getName().replace(" ", "").toLowerCase().matches("(.*)"+matchString+"(.*)")){
                searchData.add(dto.getName());
            }
        }

        //If nothing is found, search with tag
        if(searchData.size()==0)
            searchData=searchWithTag(matchString);

        Collections.sort(searchData);
        return searchData;
    }

    public ArrayList<String> searchWithTag(String tag) throws LocationDAO.DAOException {
        ArrayList<String> tags = new ArrayList<String>();

        for(Searchable dto:dao.getAllData().values()){
            if(dto.getClass().isAssignableFrom(LocationDTO.class)) {
                LocationDTO loc = (LocationDTO) dto;

                if (loc.getTags() != null) {
                    for (String t : loc.getTags()) {
                        if (t.toLowerCase().matches("(.*)" + tag + "(.*)")) {
                            tags.add(loc.getName());
                        }
                    }
                }
            }
        }

        return tags;
    }

    public List<String> getHistoryList(){
        Collections.reverse(historyList);

        return historyList;
    }

    public List<LocationDTO> getLandmarks() throws Exception {
        ArrayList<LocationDTO> Landmarks = new ArrayList<LocationDTO>();

        for(Searchable item:dao.getAllData().values()){
            if(item.getClass().isAssignableFrom(LocationDTO.class))){
                if(!(((LocationDTO) item).getLandmark().equals(MARKTYPE.NONE))){
                    Landmarks.add((LocationDTO) item);
                }
            }
        }
        if(Landmarks.size()>0)
            return Landmarks;
        else
            throw new Exception("No landmarks found !");
    }


}
