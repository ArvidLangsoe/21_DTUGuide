package com.arvid.dtuguide;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.arvid.dtuguide.data.LocationDAO;
import com.arvid.dtuguide.navigation.NavigationController;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private NavigationController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        LocationDAO dao = new LocationDAO();
        controller = new NavigationController(dao, getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Button resetFavButton = (Button) findViewById(R.id.reset_favorite);
        Button resetRecButton = (Button) findViewById(R.id.reset_recent);
        Button testButton = (Button) findViewById(R.id.button2);

        resetFavButton.setOnClickListener(this);
        resetRecButton.setOnClickListener(this);
        testButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
        AlertDialog dialog;
        switch (v.getId()){
            case R.id.reset_favorite:
                builder.setMessage(R.string.dialog_fav_message).setTitle(R.string.dialog_fav_title);
                builder.setPositiveButton(R.string.clear, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        controller.clearFavorite();
                        Toast toastFav = Toast.makeText(getApplicationContext(), "Favorites has been removed", Toast.LENGTH_SHORT);
                        toastFav.show();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        return;
                    }
                });
                dialog = builder.create();
                dialog.show();
                break;
            case R.id.reset_recent:
                controller.clearHistory();
                Toast toast = Toast.makeText(this, "Recent history has been removed", Toast.LENGTH_SHORT);
                toast.show();
                break;
            case R.id.button2:
                builder.setTitle(R.string.dialog_fav_title);
                builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        return;
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        return;
                    }
                });
                View dialogView = getLayoutInflater().inflate(R.layout.settings_dialog_map_zoom, null);
                SeekBar seekBar = (SeekBar) dialogView.findViewById(R.id.zoom_level_seekbar);
                //seekBar.setProgress();

                builder.setView(dialogView);
                dialog = builder.create();
                dialog.show();
                break;
        }
    }
}
