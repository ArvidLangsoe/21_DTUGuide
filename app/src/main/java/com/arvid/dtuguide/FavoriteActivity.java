package com.arvid.dtuguide;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.arvid.dtuguide.data.LocationDAO;
import com.arvid.dtuguide.navigation.NavigationController;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {
    private NavigationController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        LocationDAO dao = new LocationDAO();
        controller = new NavigationController(dao, getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_favorite);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recView = (RecyclerView) findViewById(R.id.favorite_recycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recView.setLayoutManager(linearLayoutManager);
        /*
        List<String> list = new ArrayList<String>();
        list.add("test1");
        list.add("test2");
        */

        FavoritesAdapter recAdapter = new FavoritesAdapter(controller.getFavorite(), R.layout.recycler_item_favorite);

        System.out.println("**FAVORITES**");
        System.out.println(controller.getFavorite());


        recView.setAdapter(recAdapter);
    }
}
