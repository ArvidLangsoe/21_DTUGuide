package com.arvid.dtuguide;

import android.*;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SearchView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.arvid.dtuguide.data.LocationDAO;
import com.arvid.dtuguide.data.LocationDTO;
import com.arvid.dtuguide.navigation.BuildingModel;
import com.arvid.dtuguide.navigation.NavigationController;
import com.arvid.dtuguide.navigation.coordinates.GeoPoint;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.IndoorBuilding;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener, ActivityCompat.OnRequestPermissionsResultCallback, GoogleMap.OnMapClickListener {


    private GoogleMap mMap;

    private Marker currentMarker;
    private static ArrayList<GroundOverlay> ballerupMap = new ArrayList<>();
    private int LOCATION_REQUEST_CODE=4565;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Locations");
    public static final String TAG = "";
    NavigationController controller;
    LocationDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        dao = new LocationDAO();
        controller = new NavigationController(dao);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        MapActivity fragment = new MapActivity();

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.topbar, menu);

        //EditText searchViewPlaceholder = (EditText)
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        final SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

        //Cursor cursorRecent = getContentResolver().query(RecentSearchSuggestionProvider.CONTENT_URI, null, null, new String[] { "" }, null);
        //Cursor cursorRooms = getContentResolver().query(SearchSuggestionProvider.CONTENT_URI, null, null, new String[] { "" }, null);
        //Cursor mergedCursor = new MergeCursor(new Cursor[] { cursorRecent, cursorRooms });
        Cursor c = getContentResolver().query(Provider.CONTENT_URI, null, null, new String[] {""}, null);
        final SearchCursorAdapter adapter = new SearchCursorAdapter(this, R.layout.searchview_suggestions_item, c, 0);

        searchView.setSuggestionsAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                adapter.runQueryOnBackgroundThread(query);
                return true;
            }
        });

        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                String roomName = adapter.getItemName(position);
                try {
                    LocationDTO location = controller.getLocation(roomName);

                    showLocation(location);
                } catch (LocationDAO.DAOException e) {
                    e.printStackTrace();
                }
                searchView.setQuery("", false);
                return true;
            }
        });



        int searchEditTextId = R.id.search_src_text;
        final AutoCompleteTextView searchEditText = (AutoCompleteTextView) searchView.findViewById(searchEditTextId);
        searchEditText.setDropDownAnchor(R.id.toolbar);

        final View dropDownAnchor = findViewById(searchEditText.getDropDownAnchor());

        if (dropDownAnchor != null) {
            dropDownAnchor.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                           int oldLeft, int oldTop, int oldRight, int oldBottom) {

                    int width = findViewById(R.id.toolbar).getWidth();
                    searchEditText.setDropDownWidth(width);

                }
            });
        }



        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle bottom_navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.navigate_to_dtu) {
            startActivity(new Intent(this, NavigateToDTUActivity.class));
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);

        mMap.setIndoorEnabled(true);

        mMap.getUiSettings().setIndoorLevelPickerEnabled(true);

        mMap.setMinZoomPreference(16);
        mMap.setMaxZoomPreference(20);

        LatLng ballerupSW = new LatLng(55.730327, 12.393678);
        LatLng ballerupNE = new LatLng(55.732781, 12.401019);

        LatLng ballerupCenter = new LatLng(55.731543, 12.396680);

        LatLngBounds ballerupBounds = new LatLngBounds(ballerupSW,ballerupNE);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ballerupCenter,16f));

        LatLngBounds BALLERUP = new LatLngBounds(new LatLng(55.730067,12.393402),new LatLng(55.733131,12.402851));
        mMap.setLatLngBoundsForCameraTarget(BALLERUP);


        Bitmap dtuMap = BitmapFactory.decodeResource(getResources(),R.drawable.dtu_map);

        addGroundOverlay(dtuMap,ballerupSW, ballerupNE);
        enableGPS();


    }

    private void addGroundOverlay(Bitmap dtuMap,LatLng swCorner, LatLng neCorner){
        int height = dtuMap.getHeight();
        int width = dtuMap.getWidth();
        int heightTiles = 2;
        int widthTiles = 4;

        double tileSizeLat=(neCorner.latitude-swCorner.latitude)/heightTiles;
        double tileSizeLong=(neCorner.longitude-swCorner.longitude)/widthTiles;

        for(int heightTile=0;heightTile<heightTiles;heightTile++){
            for(int widthTile=0;widthTile<widthTiles;widthTile++){
                Bitmap bm=Bitmap.createBitmap(dtuMap,widthTile*(width/widthTiles),heightTile*(height/heightTiles),width/widthTiles,height/heightTiles);

                GroundOverlayOptions options =new GroundOverlayOptions();

                LatLng sw = new LatLng(neCorner.latitude-(heightTile+1)*tileSizeLat,swCorner.longitude+widthTile*tileSizeLong);
                LatLng ne = new LatLng(neCorner.latitude-(heightTile)*tileSizeLat,swCorner.longitude+(widthTile+1)*tileSizeLong);

                System.out.println("Coor: " + sw + " " + ne);
                LatLngBounds tempBounds = new LatLngBounds(sw,ne);

                options.image(BitmapDescriptorFactory.fromBitmap(bm)).positionFromBounds(tempBounds);
                ballerupMap.add(mMap.addGroundOverlay(options));


            }
        }
    }

    private void enableGPS(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){

            mMap.setMyLocationEnabled(true);
            System.out.println("GPS: GPS enabled: "+mMap.isMyLocationEnabled());
        }
        else{
            System.out.println("GPS: No GPS accesss allowed, requesting permission");
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                System.out.println("GPS: Need explanation. See permission requests android.");
            }
            else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        System.out.println("GPS: Handling Request result");
        if (requestCode == LOCATION_REQUEST_CODE) {
            try {
                mMap.setMyLocationEnabled(true);
            }catch (SecurityException e){
                System.out.println("GPS: Security Exception, user denied GPS access.");
            }
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        //Todo: Add a check to see if the user is outside the map.
        return false;
    }


    public void showLocation(LocationDTO location){
        if(currentMarker!=null) {
            currentMarker.remove();
        }
        LatLng myPoint = new LatLng(location.getPosition().getLat(),location.getPosition().getLong());
        currentMarker=mMap.addMarker(new MarkerOptions().position(myPoint).title(location.getName()));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPoint,19f),3000,null);

    }


    @Override
    public void onMapClick(LatLng latLng) {
        System.out.println("UserClick: "+ latLng);
    }
}
