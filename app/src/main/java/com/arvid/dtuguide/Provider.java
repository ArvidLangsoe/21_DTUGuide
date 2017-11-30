package com.arvid.dtuguide;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.arvid.dtuguide.data.LocationDAO;
import com.arvid.dtuguide.data.LocationDTO;
import com.arvid.dtuguide.navigation.NavigationController;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jeppe on 30-11-2017.
 */

public class Provider extends ContentProvider {
    public static final String AUTHORITY = "com.arvid.dtuguide.Provider";
    public static final String CONTENT_URL = "content://" + AUTHORITY + "/rooms/";
    public static final Uri CONTENT_URI = Uri.parse(CONTENT_URL);


    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference myRef = database.getReference("Locations");
    public static final String TAG = "";
    private static LocationDAO dao;
    private static NavigationController controller;
    private List<String> historyList;


    @Override
    public boolean onCreate() {
        dao = new LocationDAO();
        controller = new NavigationController(dao);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        try {
            controller.getLocation("X1.81");
        } catch (LocationDAO.DAOException e) {
            e.printStackTrace();
        }
        historyList = controller.getHistoryList();

/*
        historyList = new ArrayList<String>();
        historyList.add("room1");
        historyList.add("room2");
*/
        System.out.println("LISTX: "+ historyList);
        String search=selectionArgs[0];

        int id = 0;
        MatrixCursor roomsCursor = new MatrixCursor(new String[]{"_id", "name"});

        if(search.isEmpty()) {
            for (String name : historyList) {
                Object[] obj = {id, name};
                id++;
                roomsCursor.addRow(obj);
            }
        }
        else{
            try {
                List<String> myList =controller.searchMatch(search);

                for (String name :  myList) {
                    Object[] obj = {id, name};
                    id++;
                    roomsCursor.addRow(obj);
                }
            } catch (LocationDAO.DAOException e) {
                e.printStackTrace();
            }
        }

        return roomsCursor;
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
