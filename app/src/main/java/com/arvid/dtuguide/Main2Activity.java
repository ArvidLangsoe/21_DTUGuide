package com.arvid.dtuguide;

import android.Manifest;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.VectorDrawable;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.arvid.dtuguide.data.LocationDAO;
import com.arvid.dtuguide.data.LocationDTO;
import com.arvid.dtuguide.data.MARKTYPE;
import com.arvid.dtuguide.navigation.Floor;
import com.arvid.dtuguide.navigation.NavigationController;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener, ActivityCompat.OnRequestPermissionsResultCallback, GoogleMap.OnMapClickListener
        ,GoogleMap.OnCameraMoveListener,CompoundButton.OnCheckedChangeListener
        , View.OnClickListener, android.support.v4.app.FragmentManager.OnBackStackChangedListener {


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.map_layers_checkbox_0:
                checkBoxMapBasement.setChecked(true);
                checkBoxMapFirst.setChecked(false);
                checkBoxMapSecond.setChecked(false);
                showFloor(FloorHeight.basement);
                currentMap = FloorHeight.basement;
                break;
            case R.id.map_layers_checkbox_1:
                checkBoxMapFirst.setChecked(true);
                checkBoxMapBasement.setChecked(false);
                checkBoxMapSecond.setChecked(false);
                showFloor(FloorHeight.ground_floor);
                currentMap = FloorHeight.ground_floor;
                break;
            case R.id.map_layers_checkbox_2:
                checkBoxMapSecond.setChecked(true);
                checkBoxMapBasement.setChecked(false);
                checkBoxMapFirst.setChecked(false);
                showFloor(FloorHeight.first_floor);
                currentMap = FloorHeight.first_floor;
        }
    }

    @Override
    public void onBackStackChanged() {
        System.out.println("### BACKSTACK COUNT ###");
        System.out.println(getSupportFragmentManager().getBackStackEntryCount());

        if(getSupportFragmentManager().getBackStackEntryCount()>0) {
            toggle.setDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
        else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            toggle.setDrawerIndicatorEnabled(true);
            getSupportFragmentManager().popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

    }


    private double cameraZoom=0;
    private GoogleMap mMap;

    private Marker currentMarker;

    HashMap<Switch,String> switches= new HashMap<Switch,String>();
    private static HashMap<FloorHeight,Floor> maps=new HashMap<FloorHeight,Floor>();


    private int LOCATION_REQUEST_CODE=4565;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Locations");
    public static final String TAG = "";
    NavigationController controller;
    LocationDAO dao;

    private FloorHeight currentMap;

    private CheckBox checkBoxMapBasement, checkBoxMapFirst, checkBoxMapSecond;

    private SearchView searchView;

    ActionBarDrawerToggle toggle;
    private final String BACK_STACK_ROOT_TAG = "search_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        dao = new LocationDAO();
        controller = new NavigationController(dao, getApplicationContext(),this);
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

        currentMap = FloorHeight.ground_floor;

        getSupportFragmentManager().addOnBackStackChangedListener(this);
        //Handle when activity is recreated like on orientation Change
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popupLayerView, popupFilterView;

            switch (item.getItemId()) {
                case R.id.map_navigate_button:
                    //TODO: What to happen when the bottom menu is clicked
                    //mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.map_layers_button:
                    popupLayerView = layoutInflater.inflate(R.layout.map_layers_popup_layout, null);


                    checkBoxMapBasement = (CheckBox)popupLayerView.findViewById(R.id.map_layers_checkbox_0);
                    checkBoxMapFirst = (CheckBox)popupLayerView.findViewById(R.id.map_layers_checkbox_1);
                    checkBoxMapSecond = (CheckBox)popupLayerView.findViewById(R.id.map_layers_checkbox_2);

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
                    popupWindowLayer.update(findViewById(R.id.navigation),width,400);

                    return true;

                case R.id.map_filter_button:
                    popupFilterView = layoutInflater.inflate(R.layout.map_filter_popup_layout, null);

                    readyFilterSwitch(popupFilterView,R.id.map_filter_cantine,"cantine",MARKTYPE.CANTEEN);
                    readyFilterSwitch(popupFilterView,R.id.map_filter_movement,"movement",MARKTYPE.STAIRS_UP);
                    readyFilterSwitch(popupFilterView,R.id.map_filter_public,"public",MARKTYPE.LIBRARY);
                    readyFilterSwitch(popupFilterView,R.id.map_filter_toilet,"toilet",MARKTYPE.WC);
                    readyFilterSwitch(popupFilterView,R.id.map_filter_water,"water",MARKTYPE.WATER_FOUNTAIN);

                    int i=0;
                    for(Switch s: switches.keySet()) {
                        s.setOnCheckedChangeListener(Main2Activity.this);
                    }

                    PopupWindow popupWindowFilter = new PopupWindow(popupFilterView,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);

                    popupWindowFilter.setOutsideTouchable(true);

                    popupWindowFilter.showAsDropDown(findViewById(R.id.map_filter_button));
                    displayMetrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    width = displayMetrics.widthPixels;
                    popupWindowFilter.update(findViewById(R.id.navigation),width,600);

                    return true;

            }
            return false;
        }
    };

    private void readyFilterSwitch(View filterView,int id, String nameCheck, MARKTYPE filterSettingCheck){
        com.arvid.dtuguide.Settings currentSettings = com.arvid.dtuguide.Settings.getInstance(getApplicationContext());
        Switch mySwitch =(Switch)filterView.findViewById(id);
        switches.put(mySwitch,nameCheck);
        mySwitch.setChecked( currentSettings.isVisible(filterSettingCheck));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //searchView.clearFocus();
            hideSoftKeyboard();
            //super.onBackPressed();

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

        Cursor c = getContentResolver().query(Provider.CONTENT_URI, null, null, new String[] {""}, null);
        final SearchCursorAdapter adapter = new SearchCursorAdapter(this, R.layout.searchview_suggestions_item, c, 0);

        searchView.setSuggestionsAdapter(adapter);
        searchView.setFocusable(false);


        // Remove underline on search view
        View v = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
        View v2 = (LinearLayout)searchView.findViewById(android.support.v7.appcompat.R.id.search_voice_btn).getParent();
        v.setBackgroundColor(Color.WHITE);
        v2.setBackgroundColor(Color.WHITE);

        searchView.setOnQueryTextFocusChangeListener(new SearchView.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus) {
                    getSupportFragmentManager().popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.map, new SearchFragment())
                            .addToBackStack(BACK_STACK_ROOT_TAG)
                            .commit();

                    System.out.println("***SEARCHVIEW FOCUS***");

                }
                else {
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
    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        //getSupportFragmentManager().popBackStack();
        return false;
    }


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
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;
        mMap.setOnMapClickListener(this);

        mMap.setIndoorEnabled(false);

        mMap.setMinZoomPreference(16);
        mMap.setMaxZoomPreference(20);

        LatLng ballerupSW = new LatLng(55.730327, 12.393678);
        LatLng ballerupNE = new LatLng(55.732781, 12.401019);

        LatLng ballerupCenter = new LatLng(55.731543, 12.396680);

        LatLngBounds ballerupBounds = new LatLngBounds(ballerupSW,ballerupNE);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ballerupCenter,16f));

        LatLngBounds BALLERUP = new LatLngBounds(new LatLng(55.730067,12.393402),new LatLng(55.733131,12.402851));
        mMap.setLatLngBoundsForCameraTarget(BALLERUP);

        Bitmap basement = BitmapFactory.decodeResource(getResources(),R.drawable.basement);
        generateGroundOverlay(basement,FloorHeight.basement,ballerupSW, ballerupNE);

        Bitmap groundFloor = BitmapFactory.decodeResource(getResources(),R.drawable.ground_floor);
        generateGroundOverlay(groundFloor,FloorHeight.ground_floor,ballerupSW, ballerupNE);

        Bitmap firstFloor = BitmapFactory.decodeResource(getResources(),R.drawable.first_floor);
        generateGroundOverlay(firstFloor,FloorHeight.first_floor,ballerupSW, ballerupNE);



        showFloor(FloorHeight.ground_floor);
        enableGPS();
        generateLandmarks();

        mMap.setOnCameraMoveListener(this);

    }

    public void showFloor(FloorHeight floor){
        for(Floor f : maps.values()){
            f.hideFloor();
        }

        maps.get(floor).showFloor();
    }

    private void generateGroundOverlay(Bitmap dtuMap,FloorHeight floor,LatLng swCorner, LatLng neCorner){
        int height = dtuMap.getHeight();
        int width = dtuMap.getWidth();
        int heightTiles = 2;
        int widthTiles = 4;

        double tileSizeLat=(neCorner.latitude-swCorner.latitude)/heightTiles;
        double tileSizeLong=(neCorner.longitude-swCorner.longitude)/widthTiles;

        Floor floorObj = new Floor(getApplicationContext());
        maps.put(floor,floorObj);

        for(int heightTile=0;heightTile<heightTiles;heightTile++){
            for(int widthTile=0;widthTile<widthTiles;widthTile++){
                Bitmap bm=Bitmap.createBitmap(dtuMap,widthTile*(width/widthTiles),heightTile*(height/heightTiles),width/widthTiles,height/heightTiles);

                GroundOverlayOptions options =new GroundOverlayOptions();

                LatLng sw = new LatLng(neCorner.latitude-(heightTile+1)*tileSizeLat,swCorner.longitude+widthTile*tileSizeLong);
                LatLng ne = new LatLng(neCorner.latitude-(heightTile)*tileSizeLat,swCorner.longitude+(widthTile+1)*tileSizeLong);

                System.out.println("Coor: " + sw + " " + ne);
                LatLngBounds tempBounds = new LatLngBounds(sw,ne);

                options.image(BitmapDescriptorFactory.fromBitmap(bm)).positionFromBounds(tempBounds);

                GroundOverlay overlay = mMap.addGroundOverlay(options);

                floorObj.addOverlay(overlay);
                overlay.setVisible(false);


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

    public void generateLandmarks(){
       if(maps==null){
           return;
       }
       if(maps.size()<3){
           return;
       }
        List<LocationDTO> landmarks;
        try {
            landmarks = controller.getLandmarks();

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        System.out.println("LANDMARK: " + landmarks);
        for(LocationDTO landMark : landmarks){
            switch (landMark.getFloor()){
                case 0:
                    maps.get(FloorHeight.basement).addLandmark(mMap,landMark);
                    break;
                case 1:
                    maps.get(FloorHeight.ground_floor).addLandmark(mMap,landMark);
                    break;
                case 2:
                    maps.get(FloorHeight.first_floor).addLandmark(mMap,landMark);
                    break;
            }
        }
        showFloor(currentMap);

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
        for(Floor f:maps.values()){
            f.removeMarkers();
            f.hideFloor();
        }

        LatLng myPoint = location.getPosition();
        currentMarker=mMap.addMarker(new MarkerOptions().position(myPoint).title(location.getName()));
        currentMarker.setVisible(false);

        switch(location.getFloor()){
            case 0:
                maps.get(FloorHeight.basement).addMarker(currentMarker).showFloor();
                currentMap=FloorHeight.basement;
                break;
            case 1:
                maps.get(FloorHeight.ground_floor).addMarker(currentMarker).showFloor();
                currentMap=FloorHeight.ground_floor;
                break;
            case 2:
                maps.get(FloorHeight.first_floor).addMarker(currentMarker).showFloor();
                currentMap=FloorHeight.first_floor;
                break;
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPoint,17.5f),1500,null);

    }


    @Override
    public void onMapClick(LatLng latLng) {

        System.out.println("UserClick: "+ latLng);
        hideSoftKeyboard();
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
        searchView.clearFocus();
        //getSupportFragmentManager().popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onCameraMove() {
        Float zoomSetting = com.arvid.dtuguide.Settings.getInstance(getApplicationContext()).getZoom();
        double tolerance= zoomSetting;
        double newZoom=mMap.getCameraPosition().zoom;
        if(cameraZoom>tolerance){
            if(newZoom<=tolerance){
                showFloor(currentMap);
            }
        }else{
            if(newZoom>tolerance){
                showFloor(currentMap);
            }
        }

        cameraZoom= newZoom;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        System.out.println("TEST"+ isChecked);
        String filterChanged = switches.get(buttonView);
        com.arvid.dtuguide.Settings mySettings= com.arvid.dtuguide.Settings.getInstance(getApplicationContext());

        switch(filterChanged){
            case "cantine":
                mySettings.setFilter(MARKTYPE.CANTEEN,isChecked);
                mySettings.setFilter(MARKTYPE.KITCHEN,isChecked);
                break;
            case "movement":
                mySettings.setFilter(MARKTYPE.STAIRS_DOWN,isChecked);
                mySettings.setFilter(MARKTYPE.STAIRS_UP,isChecked);
                mySettings.setFilter(MARKTYPE.STAIRS_UP_DOWN,isChecked);
                mySettings.setFilter(MARKTYPE.ELEVATOR_DOWN,isChecked);
                mySettings.setFilter(MARKTYPE.ELEVATOR_UP,isChecked);
                mySettings.setFilter(MARKTYPE.ELEVATOR_UP_DOWN,isChecked);
                mySettings.setFilter(MARKTYPE.ENTRANCE,isChecked);
                break;
            case "public":
                mySettings.setFilter(MARKTYPE.LIBRARY,isChecked);
                mySettings.setFilter(MARKTYPE.SHOP,isChecked);
                break;
            case "toilet":
                mySettings.setFilter(MARKTYPE.WC,isChecked);
                mySettings.setFilter(MARKTYPE.WC_HANDICAP,isChecked);
                break;
            case "water":
                mySettings.setFilter(MARKTYPE.WATER_FOUNTAIN,isChecked);
                break;
        }
        showFloor(currentMap);

    }
}
