package com.arvid.dtuguide;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.arvid.dtuguide.data.LocationDAO;
import com.arvid.dtuguide.navigation.NavigationController;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private NavigationController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Button button = (Button) findViewById(R.id.reset_favorite);
        button.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reset_favorite:
                controller.clearFavorite();
                Toast toast = Toast.makeText(this, "Favorites has been removed", Toast.LENGTH_SHORT);
                toast.show();
                break;
        }
    }
}
