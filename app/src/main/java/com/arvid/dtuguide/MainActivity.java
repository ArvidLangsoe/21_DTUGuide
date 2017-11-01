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
import com.arvid.dtuguide.navigation.coordinates.MapPoint;

public class MainActivity extends AppCompatActivity implements MapChangeObserver {
    private GestureView map;
    private LocationManager locationManager;
    private ImageView mapImage;
    private NavigationController nc;

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

        Bitmap dtuMap = BitmapFactory.decodeResource(getResources(), R.drawable.kort_v2);

        writeImageWithLocation(new MapPoint(400,400));


        Thread background=new Thread(new Runnable() {

            @Override
            public void run() {
                while(true)
                {
                    try {
                        Thread.sleep(1000);
                        nc.setRandomMyLocation();
                        handler.sendMessage(handler.obtainMessage());
                    } catch (Exception e) {

                    }
                }
            }
        });

        background.start();
    }


    public void writeImageWithLocation(MapPoint location){
        Bitmap dtuMap = BitmapFactory.decodeResource(getResources(), R.drawable.kort_v2);
        Bitmap resultBitmap = Bitmap.createBitmap(dtuMap.getWidth(), dtuMap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(resultBitmap);

        c.drawBitmap(dtuMap, 0, 0, null);

        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(Color.CYAN);
        c.drawCircle((int)location.getX(),(int)location.getY(),dtuMap.getWidth()/40,p);
        c.save();

        mapImage.setImageDrawable(new BitmapDrawable(getResources(), resultBitmap));

    }

    @Override
    public void updateMap() {
        writeImageWithLocation(nc.getMyLocation());
    }



}
