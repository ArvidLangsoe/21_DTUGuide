package com.arvid.dtuguide.navigation;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.arvid.dtuguide.data.LocationDAO;
import com.arvid.dtuguide.data.LocationDTO;
import com.arvid.dtuguide.data.MARKTYPE;
import com.arvid.dtuguide.data.Person;
import com.arvid.dtuguide.data.Searchable;
import com.arvid.dtuguide.navigation.coordinates.GeoPoint;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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
    private static List<Searchable> historyList;
    private static List<Searchable> favorite;
    private Context context;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Searchable");

    final String HISTORYPREF = "History_list";
    final String FAVORITEPREF = "Favorite_list";

    // create a reference to the shared preferences object
    SharedPreferences mySharedPreferences;
    SharedPreferences mySharedPreferencesFav;

    SharedPreferences.Editor myEditor;

    public NavigationController(){
    }

    public NavigationController(LocationDAO dao, Context context){
        this.dao = dao;
        this.context = context;

        mySharedPreferences = context.getSharedPreferences(HISTORYPREF, 0);
        mySharedPreferencesFav = context.getSharedPreferences(FAVORITEPREF, 0);

        historyList = new ArrayList<Searchable>();
        favorite = new ArrayList<Searchable>();
        //savePrefs();

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
                HashMap<String, HashMap<String, Object>> persons = map.get("Persons");

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

                for(HashMap<String, Object> person:persons.values()){
                    Person dto = null;
                    try {
                        dto = (Person) new Person()
                                .setdescription((String)person.get("description"))
                                .setEmail((String)person.get("email"))
                                .setPictureURL((String)person.get("pictureURL"))
                                .setRole((String)person.get("role"))
                                .setRoom((LocationDTO) dao.getData((String)person.get("roomName")))
                                .setName((String)person.get("name"));

                        dao.saveData(dto);
                    } catch (LocationDAO.DAOException e) {
                        e.printStackTrace();
                        System.out.println((String)person.get("name")
                                +" Object Data is rejected because the Room "
                                +(String)person.get("roomName")+" does not exist.");
                    }

                }

                try {
                    retrievePrefs();
                } catch (LocationDAO.DAOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void retrievePrefs() throws LocationDAO.DAOException{
        historyList.clear();

        System.out.println("WARN "+mySharedPreferences.getAll().values());

        System.out.println("WARN 2 :"+mySharedPreferences.getString("0",""));

        for(int i=0;i<mySharedPreferences.getAll().values().size();++i)
            historyList.add(dao.getData(mySharedPreferences.getString(i+"","")));

        retrieveFavorite();

    }

    private void savePrefs() {
        myEditor = mySharedPreferences.edit();
        for(int i = 0; i<historyList.size();++i){
            myEditor.putString(i+"", historyList.get(i).getName());
        }
        myEditor.commit();
    }

    private void retrieveFavorite() throws LocationDAO.DAOException{
        favorite.clear();

        System.out.println("WARN 3 :"+mySharedPreferencesFav.getString("0",""));

        for(int i=0;i<mySharedPreferencesFav.getAll().values().size();++i)
            favorite.add(dao.getData(mySharedPreferencesFav.getString(i+"","")));

    }

    private void saveFavorite(){
        myEditor = mySharedPreferencesFav.edit();
        for(int i = 0; i<favorite.size();++i){
            myEditor.putString(i+"", favorite.get(i).getName());
        }
        myEditor.commit();
    }

    public void clearFavorite(){
        myEditor = mySharedPreferencesFav.edit();
        myEditor.clear();
        myEditor.commit();
    }

    public void removeFavorite(Searchable itemTORemove){
        myEditor = mySharedPreferencesFav.edit();

        for(int i=0; i<favorite.size();++i){
            if(favorite.get(i).equals(itemTORemove)){
                myEditor.remove(i+"");
                try {
                    retrieveFavorite();
                } catch (LocationDAO.DAOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void addFavorite(Searchable item){
        favorite.add(item);
        saveFavorite();
    }

    public List<Searchable> getFavorite(){return favorite;}


    public Searchable getSearchableItem(String name) throws LocationDAO.DAOException {
        Searchable dto = dao.getData(name);

        if(historyList.contains(dto))
            historyList.remove(dto);

        else if(historyList.size()==10)
            historyList.remove(historyList.get(0));

        historyList.add(dto);
        savePrefs();
        System.out.println("HISTORYLIST: " + historyList);
        return dto;
    }

    public List<Searchable> searchMatch(String matchString) throws LocationDAO.DAOException {
        List<Searchable> searchData = new ArrayList<Searchable>();
        matchString = matchString.replace(" ", "");

        //Search with name
        for(Searchable dto : dao.getAllData().values()){
            if(dto.getName().replace(".", "").toLowerCase().matches("(.*)"+matchString+"(.*)")
                    || dto.getName().replace(".", "").toLowerCase().matches("(.*)"+matchString+"(.*)")
                    || dto.getName().replace(" ", "").toLowerCase().matches("(.*)"+matchString+"(.*)")){
                searchData.add(dto);
            }
        }

        //If nothing is found, search with tag
        if(searchData.size()==0)
            searchData=searchWithTag(matchString);

        Collections.sort(searchData);
        return searchData;
    }

    public ArrayList<Searchable> searchWithTag(String tag) throws LocationDAO.DAOException {
        ArrayList<Searchable> tags = new ArrayList<Searchable>();

        for(Searchable dto:dao.getAllData().values()){
            if(dto.getClass().isAssignableFrom(LocationDTO.class)) {
                LocationDTO loc = (LocationDTO) dto;

                if (loc.getTags() != null) {
                    for (String t : loc.getTags()) {
                        if (t.toLowerCase().matches("(.*)" + tag + "(.*)")) {
                            tags.add(loc);
                        }
                    }
                }
            }
        }

        return tags;
    }

    public List<Searchable> getHistoryList(){
        List<Searchable> list = new ArrayList<Searchable>();

        for(Searchable item:historyList) {
            list.add(item);
        }
        Collections.reverse(list);

        return list;
    }

    public List<LocationDTO> getLandmarks() throws Exception {
        ArrayList<LocationDTO> Landmarks = new ArrayList<LocationDTO>();

        for(Searchable item:dao.getAllData().values()){
            if(item.getClass().isAssignableFrom(LocationDTO.class)){
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
