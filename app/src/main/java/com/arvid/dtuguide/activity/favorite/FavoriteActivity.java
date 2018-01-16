package com.arvid.dtuguide.activity.favorite;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.arvid.dtuguide.R;
import com.arvid.dtuguide.data.LocationDAO;
import com.arvid.dtuguide.data.Searchable;
import com.arvid.dtuguide.navigation.NavigationController;

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
        TextView emptyView = (TextView) findViewById(R.id.empty_view);

        List<Searchable> favorites = controller.getFavorite();
        if(favorites.isEmpty()) {
            recView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            recView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recView.setLayoutManager(linearLayoutManager);

        recView.setAdapter(new FavoritesAdapter(getApplicationContext(), favorites, R.layout.recycler_item_favorite,this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
