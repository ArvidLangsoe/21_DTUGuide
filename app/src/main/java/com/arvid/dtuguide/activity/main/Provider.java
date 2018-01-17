package com.arvid.dtuguide.activity.main;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import com.arvid.dtuguide.data.LocationDAO;
import com.arvid.dtuguide.data.LocationDTO;
import com.arvid.dtuguide.data.Person;
import com.arvid.dtuguide.data.Searchable;
import com.arvid.dtuguide.navigation.NavigationController;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeppe on 30-11-2017.
 */

public class Provider extends ContentProvider {
    public static final String AUTHORITY = "com.arvid.dtuguide.activity.main.Provider";
    public static final String CONTENT_URL = "content://" + AUTHORITY + "/rooms/";
    public static final Uri CONTENT_URI = Uri.parse(CONTENT_URL);


    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference myRef = database.getReference("Locations");
    public static final String TAG = "";
    private static LocationDAO dao;
    private static NavigationController controller;

    public enum CURSOR_COLUMNS {
        _ID,
        NAME,
        TYPE,
        SUBTEXT,
        RECENT,
        FAVORITE
    }

    @Override
    public boolean onCreate() {
        dao = new LocationDAO();
        controller = new NavigationController(dao, getContext(),null);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        String search = selectionArgs[0];
        int id = 0;

        MatrixCursor suggestionsCursor =
                new MatrixCursor(new String[]{ CURSOR_COLUMNS._ID.toString() , CURSOR_COLUMNS.NAME.toString(), CURSOR_COLUMNS.TYPE.toString(),
                        CURSOR_COLUMNS.SUBTEXT.toString(), CURSOR_COLUMNS.RECENT.toString(), CURSOR_COLUMNS.FAVORITE.toString()});

        if(search.isEmpty()) {
            for (Searchable item : controller.getHistoryList()) {
                String type = item.getType();
                String subText = "";
                if(type.equals("Person")) {
                    subText = ((Person) item).getRole();
                }
                else {
                    subText = item.getDescription();
                    if(subText.isEmpty()){
                        ArrayList<Person> people=((LocationDTO)item).getPersons();
                        subText="";
                        for(Person p : people){
                            subText+=""+p.getName()+",";
                        }
                        if(!subText.isEmpty()) {
                            subText = subText.substring(0, subText.length() - 1);
                        }

                    }
                }
                Object[] obj = {id, item.getName(), item.getType(), subText, true, controller.checkFavorite(item) };
                id++;
                suggestionsCursor.addRow(obj);
            }
        }
        else{
            try {
                List<Searchable> suggestionsList = controller.searchMatch(search);

                for (Searchable item :  suggestionsList) {
                    String type = item.getType();
                    String subString = "";
                    if(type.equals("Person")) {
                        subString = ((Person) item).getRole();
                    }
                    else {
                        subString = item.getDescription();
                        if(subString.isEmpty()){
                            ArrayList<Person> people=((LocationDTO)item).getPersons();
                            subString="";
                            for(Person p : people){
                                subString+=""+p.getName()+",";

                            }
                            if(!subString.isEmpty()) {
                                subString = subString.substring(0, subString.length() - 1);
                            }

                        }
                    }
                    Object[] obj = {id, item.getName(), item.getType(), subString, controller.checkHistory(item), controller.checkFavorite(item) };
                    id++;
                    suggestionsCursor.addRow(obj);
                }
            } catch (LocationDAO.DAOException e) {
                e.printStackTrace();
            }
        }

        return suggestionsCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }


}
