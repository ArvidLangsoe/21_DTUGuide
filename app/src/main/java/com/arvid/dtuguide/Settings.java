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
    private float zoomSetting=17.5f;

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

    public float getZoom(){
        if(pref.contains("zoom")){
            zoomSetting=pref.getFloat("zoom",17.5f);
        }
        return zoomSetting;
    }

    public void setZoom(Float currentZoom){
        pref.edit().putFloat("zoom",currentZoom).commit();
        zoomSetting=currentZoom;
    }
}
