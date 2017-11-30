package com.arvid.dtuguide.data;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.arvid.dtuguide.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FireBaseActivity extends AppCompatActivity {

    public static final String TAG = "";
    private TextView text;

    private LocationDAO dao;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Locations");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_base);

        dao = new LocationDAO();

        text = (TextView) findViewById(R.id.firebasetext);

        text.setText("Ini");

        updateData();
    }

    public void updateData(){

        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {};
                ArrayList<String> map = (ArrayList<String>) dataSnapshot.getValue();

                dao.setLocations(new HashMap<String, LocationDTO>());

                for(String location : map){
                    dao.saveLocation((dao.parseToDTO(location)));
                }

                try {
                    text.setText(dao.getLocations()+"");
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
}