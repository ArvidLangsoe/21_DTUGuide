package com.arvid.dtuguide;

import android.content.Context;
import android.content.SharedPreferences;

import com.arvid.dtuguide.data.MARKTYPE;

import java.util.HashMap;

/**
 * Created by arvid on 11-01-2018.
 */

public class Settings {

    private static Settings settings;
    private SharedPreferences pref;
    private float zoomSetting=18f;
    public final int animationTimer = 500;

    private HashMap<MARKTYPE,Boolean> filterSettings;

    private Settings(){

        filterSettings= new HashMap<MARKTYPE,Boolean>();

        for(MARKTYPE landMark : MARKTYPE.values())
            filterSettings.put(landMark,true);
    }

    public static Settings getInstance(Context context){
        if(settings==null){
            settings=new Settings();
            settings.pref=context.getSharedPreferences("Settings",Context.MODE_PRIVATE);

        }
        return settings;
    }

    public boolean isVisible(MARKTYPE landmark){

        if(pref.contains(landmark.toString())){
            filterSettings.put(landmark,pref.getBoolean(landmark.toString(),true));
        }

        return filterSettings.get(landmark);
    }

    public void setFilter(MARKTYPE landmark,boolean value){
        pref.edit().putBoolean(landmark.toString(),value).commit();
        filterSettings.put(landmark,value);
    }
    public int getMinZoom(){
        return 1;
    }
    public int getMaxZoom(){
        return 50;
    }
    public float getGoogleMaxZoom(){
        return 20.0f;
    }
    public float getGoogleMinZoom(){
        return 16.0f;
    }


    public int getZoom(){
        if(pref.contains("zoom")){
            zoomSetting=pref.getFloat("zoom",17.5f);
        }

        int numberOfLevels=getMaxZoom()-getMinZoom()+1;
        double currentLevel=((zoomSetting-getGoogleMinZoom())/(getGoogleMaxZoom()-1-getGoogleMinZoom()))*numberOfLevels+1;

        System.out.println("ZOOM: Number of levels: "+numberOfLevels+" currentLevel: "+ currentLevel);
        return (int)currentLevel;
    }

    public float getGoogleZoom(){
        if(pref.contains("zoom")){
            zoomSetting=pref.getFloat("zoom",17.5f);
        }
        return zoomSetting;
    }

    public void setZoom(int intZoom){
        int numberOfLevels=getMaxZoom()-getMinZoom()+1;
        int currentLevel=intZoom-1;
        float valuePerLevel= ((getGoogleMaxZoom()-1-getGoogleMinZoom())/numberOfLevels);

        float googleZoom = getGoogleMinZoom()+currentLevel*valuePerLevel;
        pref.edit().putFloat("zoom",googleZoom).commit();

        System.out.println("ZOOM: setzoom " + googleZoom);
        zoomSetting=googleZoom;
    }
}
