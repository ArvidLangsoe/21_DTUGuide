package com.arvid.dtuguide;

import android.content.ContentProvider;
import android.content.ContentValues;
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
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/rooms");

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
        updateData();
        try {

            controller.getLocation("X1.81");
            System.out.println("HISTORY:"+ controller.getHistoryList());
        } catch (LocationDAO.DAOException e) {
            e.printStackTrace();
        }
        historyList = controller.getHistoryList();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        historyList = controller.getHistoryList();

        String search=uri.getPath();
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

    public void updateData(){

        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

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
}
