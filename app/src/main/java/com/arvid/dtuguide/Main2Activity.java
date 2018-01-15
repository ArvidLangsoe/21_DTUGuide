package com.arvid.dtuguide;

import android.Manifest;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.arvid.dtuguide.data.LocationDAO;
import com.arvid.dtuguide.data.LocationDTO;
import com.arvid.dtuguide.navigation.NavigationController;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;


public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener, ActivityCompat.OnRequestPermissionsResultCallback, GoogleMap.OnMapClickListener, View.OnClickListener, android.support.v4.app.FragmentManager.OnBackStackChangedListener, GoogleMap.OnMarkerClickListener {


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.map_layers_checkbox_0:
                checkBoxMapBasement.setChecked(true);
                checkBoxMapFirst.setChecked(false);
                checkBoxMapSecond.setChecked(false);
                showFloor(Floor.basement);
                currentMap = Floor.basement;
                break;
            case R.id.map_layers_checkbox_1:
                checkBoxMapFirst.setChecked(true);
                checkBoxMapBasement.setChecked(false);
                checkBoxMapSecond.setChecked(false);
                showFloor(Floor.ground_floor);
                currentMap = Floor.ground_floor;
                break;
            case R.id.map_layers_checkbox_2:
                checkBoxMapSecond.setChecked(true);
                checkBoxMapBasement.setChecked(false);
                checkBoxMapFirst.setChecked(false);
                showFloor(Floor.first_floor);
                currentMap = Floor.first_floor;
                break;
        }
    }

    @Override
    public void onBackStackChanged() {
        System.out.println("### BACKSTACK COUNT ###");
        System.out.println(getSupportFragmentManager().getBackStackEntryCount());

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            toggle.setDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            toggle.setDrawerIndicatorEnabled(true);
            getSupportFragmentManager().popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            searchView.clearFocus();
        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return false;
    }


    public enum Floor {basement, ground_floor, first_floor}

    private GoogleMap mMap;

    private Marker currentMarker;

    private static ArrayList<GroundOverlay> currentMaps;
    private static HashMap<Floor, ArrayList<GroundOverlay>> maps = new HashMap<Floor, ArrayList<GroundOverlay>>();


    private int LOCATION_REQUEST_CODE = 4565;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Locations");
    public static final String TAG = "";
    NavigationController controller;
    LocationDAO dao;

    private Floor currentMap;

    private CheckBox checkBoxMapBasement, checkBoxMapFirst, checkBoxMapSecond;

    private SearchView searchView;

    private ActionBarDrawerToggle toggle;
    private final String BACK_STACK_ROOT_TAG = "search_fragment";
    private MenuItem bottomNavigationItemSelected;

    public boolean isConnectedToInternet() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public void showInternetWarning() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("No Internet connection");
        builder.setMessage("DTU Guide needs a Internet connection at the start to work. " +
                "Check your device and click 'Retry' to use the Application.");
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
                if(!isConnectedToInternet())
                    showInternetWarning();
            }
        });
        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        if(!isConnectedToInternet())
            showInternetWarning();

        dao = new LocationDAO();
        controller = new NavigationController(dao, getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View view) {
                view.requestFocus();
                super.onDrawerClosed(view);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //bottomNavigationView.setSelectedItemId(R.id.map_layers_button);

        currentMap = Floor.ground_floor;

        getSupportFragmentManager().addOnBackStackChangedListener(this);
        //Handle when activity is recreated like on orientation Change
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popupLayerView, popupFilterView;

            if (bottomNavigationItemSelected == item) {
                bottomNavigationItemSelected = null;
                item.setChecked(false);
                return true;
            }

            bottomNavigationItemSelected = item;


            switch (item.getItemId()) {
                case R.id.map_navigate_button:
                    getMyLocation();
                    return true;
                case R.id.map_layers_button:
                    popupLayerView = layoutInflater.inflate(R.layout.map_layers_popup_layout, null);


                    checkBoxMapBasement = (CheckBox) popupLayerView.findViewById(R.id.map_layers_checkbox_0);
                    checkBoxMapFirst = (CheckBox) popupLayerView.findViewById(R.id.map_layers_checkbox_1);
                    checkBoxMapSecond = (CheckBox) popupLayerView.findViewById(R.id.map_layers_checkbox_2);

                    checkBoxMapFirst.setOnClickListener(Main2Activity.this);
                    checkBoxMapBasement.setOnClickListener(Main2Activity.this);
                    checkBoxMapSecond.setOnClickListener(Main2Activity.this);

                    switch (currentMap) {
                        case basement:
                            checkBoxMapBasement.setChecked(true);
                            break;
                        case ground_floor:
                            checkBoxMapFirst.setChecked(true);
                            break;
                        case first_floor:
                            checkBoxMapSecond.setChecked(true);
                            break;
                    }


                    PopupWindow popupWindowLayer = new PopupWindow(popupLayerView,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);


                    popupWindowLayer.setOutsideTouchable(true);

                    popupWindowLayer.showAsDropDown(findViewById(R.id.map_layers_button));


                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    int width = displayMetrics.widthPixels;
                    popupWindowLayer.update(findViewById(R.id.navigation), width, 400);

                    return true;

                case R.id.map_filter_button:
                    popupFilterView = layoutInflater.inflate(R.layout.map_filter_popup_layout, null);

                    PopupWindow popupWindowFilter = new PopupWindow(popupFilterView,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);

                    popupWindowFilter.setOutsideTouchable(true);

                    popupWindowFilter.showAsDropDown(findViewById(R.id.map_filter_button));
                    displayMetrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    width = displayMetrics.widthPixels;
                    popupWindowFilter.update(findViewById(R.id.navigation), width, 300);

                    return true;

            }

            return false;
        }
    };
    /*
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.map_layers_checkbox_0:
                checkBoxMapBasement.setChecked(true);
                checkBoxMapFirst.setChecked(false);
                checkBoxMapSecond.setChecked(false);
                showFloor(Floor.basement);

                break;
            case R.id.map_layers_checkbox_1:
                if (isChecked) {
                    checkBoxMapBasement.setChecked(false);
                    checkBoxMapSecond.setChecked(false);
                    showFloor(Floor.ground_floor);
                }
                break;
            case R.id.map_layers_checkbox_2:
                if (isChecked) {
                    checkBoxMapBasement.setChecked(false);
                    checkBoxMapFirst.setChecked(false);
                    showFloor(Floor.first_floor);
                }
                break;
        }

    }
    */


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //searchView.clearFocus();
            //hideSoftKeyboard();
            super.onBackPressed();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.topbar, menu);

        //EditText searchViewPlaceholder = (EditText)
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

        Cursor c = getContentResolver().query(Provider.CONTENT_URI, null, null, new String[]{""}, null);
        final SearchCursorAdapter adapter = new SearchCursorAdapter(this, R.layout.searchview_suggestions_item, c, 0);

        searchView.setSuggestionsAdapter(adapter);
        searchView.setFocusable(false);


        // Remove underline on search view
        View v = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
        View v2 = (LinearLayout) searchView.findViewById(android.support.v7.appcompat.R.id.search_voice_btn).getParent();
        v.setBackgroundColor(Color.WHITE);
        v2.setBackgroundColor(Color.WHITE);

        searchView.setOnQueryTextFocusChangeListener(new SearchView.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    getSupportFragmentManager().popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.map, new SearchFragment())
                            .addToBackStack(BACK_STACK_ROOT_TAG)
                            .commit();

                    System.out.println("***SEARCHVIEW FOCUS***");

                } else {
                    getSupportFragmentManager().popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    System.out.println("***SEARCHVIEW _NOT_ FOCUS***");
                }
            }
        });

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
                    LocationDTO location = (LocationDTO) controller.getSearchableItem(roomName);

                    showLocation(location);
                } catch (LocationDAO.DAOException e) {
                    e.printStackTrace();
                }
                searchView.setQuery(roomName, false);
                onBackPressed();
                return true;
            }
        });


        int searchEditTextId = R.id.search_src_text;
        final AutoCompleteTextView searchEditText = (AutoCompleteTextView) searchView.findViewById(searchEditTextId);

        searchEditText.setDropDownAnchor(R.id.toolbar);

        //searchEditText.setDropDownAnchor(R.id.anchor_dropdown);
        //searchEditText.setDropDownHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        searchEditText.setDropDownVerticalOffset(58);


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

    /*
    @Override
    public void onBackStackChanged() {
        shouldDisplayHomeUp();
    }

    public void shouldDisplayHomeUp(){
        //Enable Up button only  if there are entries in the back stack
        boolean canback = getSupportFragmentManager().getBackStackEntryCount()>0;
        getSupportActionBar().setDisplayHomeAsUpEnabled(canback);
    }

*/
    /*
    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        //getSupportFragmentManager().popBackStack();
        return false;
    }
    */


    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    */

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle bottom_navigation view item clicks here.

        switch (item.getItemId()) {
            case R.id.navigate_to_dtu:
                startActivity(new Intent(this, NavigateToDTUActivity.class));
                break;

            case R.id.nav_drawer_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;

            case R.id.nav_drawer_favorite:
                startActivity(new Intent(this, FavoriteActivity.class));
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerClickListener(this);

        mMap.setIndoorEnabled(false);

        mMap.setMinZoomPreference(16);
        mMap.setMaxZoomPreference(20);

        LatLng ballerupSW = new LatLng(55.730327, 12.393678);
        LatLng ballerupNE = new LatLng(55.732781, 12.401019);

        LatLng ballerupCenter = new LatLng(55.731543, 12.396680);

        LatLngBounds ballerupBounds = new LatLngBounds(ballerupSW, ballerupNE);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ballerupCenter, 16f));

        LatLngBounds BALLERUP = new LatLngBounds(new LatLng(55.730067, 12.393402), new LatLng(55.733131, 12.402851));
        mMap.setLatLngBoundsForCameraTarget(BALLERUP);

        Bitmap basement = BitmapFactory.decodeResource(getResources(), R.drawable.basement);
        generateGroundOverlay(basement, Floor.basement, ballerupSW, ballerupNE);
        Bitmap groundFloor = BitmapFactory.decodeResource(getResources(), R.drawable.ground_floor);
        generateGroundOverlay(groundFloor, Floor.ground_floor, ballerupSW, ballerupNE);
        Bitmap firstFloor = BitmapFactory.decodeResource(getResources(), R.drawable.first_floor);
        generateGroundOverlay(firstFloor, Floor.first_floor, ballerupSW, ballerupNE);

        showFloor(Floor.ground_floor);
        enableGPS();


    }

    public void showFloor(Floor floor) {
        if (null != currentMaps) {
            for (GroundOverlay o : currentMaps) {
                o.setVisible(false);
            }
        }

        for (GroundOverlay o : maps.get(floor)) {
            o.setVisible(true);
        }

        currentMaps = maps.get(floor);
    }

    private void generateGroundOverlay(Bitmap dtuMap, Floor floor, LatLng swCorner, LatLng neCorner) {
        int height = dtuMap.getHeight();
        int width = dtuMap.getWidth();
        int heightTiles = 2;
        int widthTiles = 4;

        double tileSizeLat = (neCorner.latitude - swCorner.latitude) / heightTiles;
        double tileSizeLong = (neCorner.longitude - swCorner.longitude) / widthTiles;
        maps.put(floor, new ArrayList<GroundOverlay>());

        for (int heightTile = 0; heightTile < heightTiles; heightTile++) {
            for (int widthTile = 0; widthTile < widthTiles; widthTile++) {
                Bitmap bm = Bitmap.createBitmap(dtuMap, widthTile * (width / widthTiles), heightTile * (height / heightTiles), width / widthTiles, height / heightTiles);

                GroundOverlayOptions options = new GroundOverlayOptions();

                LatLng sw = new LatLng(neCorner.latitude - (heightTile + 1) * tileSizeLat, swCorner.longitude + widthTile * tileSizeLong);
                LatLng ne = new LatLng(neCorner.latitude - (heightTile) * tileSizeLat, swCorner.longitude + (widthTile + 1) * tileSizeLong);

                System.out.println("Coor: " + sw + " " + ne);
                LatLngBounds tempBounds = new LatLngBounds(sw, ne);

                options.image(BitmapDescriptorFactory.fromBitmap(bm)).positionFromBounds(tempBounds);

                GroundOverlay overlay = mMap.addGroundOverlay(options);

                maps.get(floor).add(overlay);
                overlay.setVisible(false);

            }
        }
    }

    private void enableGPS() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            mMap.setMyLocationEnabled(true);

            System.out.println("GPS: GPS enabled: " + mMap.isMyLocationEnabled());
        } else {
            System.out.println("GPS: No GPS accesss allowed, requesting permission");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                System.out.println("GPS: Need explanation. See permission requests android.");
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        System.out.println("GPS: Handling Request result");
        if (requestCode == LOCATION_REQUEST_CODE) {
            try {
                mMap.setMyLocationEnabled(true);
            } catch (SecurityException e) {
                System.out.println("GPS: Security Exception, user denied GPS access.");
            }
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        //Todo: Add a check to see if the user is outside the map.
        return false;
    }


    public void showLocation(final LocationDTO location) {
        if (currentMarker != null) {
            currentMarker.remove();
        }

        //TODO: Remember to change to the floor.
        LatLng myPoint = location.getPosition();
        currentMarker = mMap.addMarker(new MarkerOptions().position(myPoint).title(location.getName()));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPoint, 19f), 3000, null);

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                if (controller.checkFavorite(location)) {
                    controller.removeFavorite(location);
                    Toast.makeText(getApplicationContext(), R.string.toast_rem_fav, Toast.LENGTH_SHORT).show();
                } else {
                    controller.addFavorite(location);
                    Toast.makeText(getApplicationContext(), R.string.toast_add_fav, Toast.LENGTH_SHORT).show();
                }

                marker.showInfoWindow();

            }
        });

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View infoWindowContent = getLayoutInflater().inflate(R.layout.infowindow_content, null);
                TextView nameTV = (TextView) infoWindowContent.findViewById(R.id.info_name);
                nameTV.setText(location.getName());

                TextView descriptionTV = (TextView) infoWindowContent.findViewById(R.id.info_description);
                descriptionTV.setText(location.getDescription());


                final ImageView favoriteIcon = (ImageView) infoWindowContent.findViewById(R.id.info_favorite);

                if (controller.checkFavorite(location)) {
                    favoriteIcon.setImageResource(R.drawable.ic_favorite_black_24dp);
                } else {
                    favoriteIcon.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                }

                RecyclerView recyclerView = (RecyclerView) infoWindowContent.findViewById(R.id.info_tags_recyclerview);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

                recyclerView.setLayoutManager(linearLayoutManager);

                TagsAdapter recAdapter = new TagsAdapter(location.getTags(), R.layout.recycler_item_tag);
                recyclerView.setAdapter(recAdapter);


                return infoWindowContent;
            }
        });

        currentMarker.showInfoWindow();

    }


    @Override
    public void onMapClick(LatLng latLng) {

        System.out.println("UserClick: " + latLng);
        bottomNavigationItemSelected = null;
        getSupportFragmentManager().popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
        //getSupportFragmentManager().popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public void getMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager)
                    getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            Location location = locationManager.getLastKnownLocation(locationManager
                    .getBestProvider(criteria, false));
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);

            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng), 3000, null);
        }

    }
}
