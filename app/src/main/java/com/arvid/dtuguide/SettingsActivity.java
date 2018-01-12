package com.arvid.dtuguide;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_simple);
        setSupportActionBar(toolbar);
        // Display the fragment as the main content.

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


}
