package com.arvid.dtuguide;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.alexvasilkov.gestures.views.interfaces.GestureView;
import com.arvid.dtuguide.navigation.Navigation;
import com.arvid.dtuguide.navigation.NavigationController;
import com.arvid.dtuguide.navigation.coordinates.GeoPoint;
import com.arvid.dtuguide.navigation.coordinates.MapPoint;

public class MainActivity extends AppCompatActivity {
    private GestureView map;
    private LocationManager locationManager;
    private ImageView mapImage;
    private NavigationController nc;

    private MapPoint cmp1 = new MapPoint(291,337);
    private MapPoint cmp2 = new MapPoint(62,1013);

    private GeoPoint cgp1 = new GeoPoint(12.395167,55.732010);
    private GeoPoint cgp2 = new GeoPoint(12.397372,55.730977);


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //display each item in a single line
            writeImageWithLocation(nc.getMyLocation());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapImage= (ImageView) findViewById(R.id.map);

        nc =new NavigationController();

        nc.calibrate(cgp1,cgp2,cmp1,cmp2);

        Bitmap dtuMap = BitmapFactory.decodeResource(getResources(), R.drawable.kort_v2);

        writeImageWithLocation(nc.getMyLocation());

    }



    public void writeImageWithLocation(MapPoint location){
        Bitmap dtuMap = BitmapFactory.decodeResource(getResources(), R.drawable.kort_v2);
        Bitmap resultBitmap = Bitmap.createBitmap(dtuMap.getWidth(), dtuMap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(resultBitmap);

        c.drawBitmap(dtuMap, 0, 0, null);
        Paint p = new Paint();
        p.setColor(Color.CYAN);
        c.drawCircle(((int)location.getX()*(dtuMap.getWidth()/560)),(int)location.getY()*(dtuMap.getHeight()/1920),dtuMap.getWidth()/40,p);
        c.save();

        mapImage.setImageDrawable(new BitmapDrawable(getResources(), resultBitmap));

    }


}
