package com.arvid.dtuguide;

import com.arvid.dtuguide.data.MARKTYPE;

import java.util.HashMap;

/**
 * Created by arvid on 11-01-2018.
 */

public class Settings {

    private static Settings settings;

    private HashMap<MARKTYPE,Boolean> filterSettings;

    private Settings(){

        filterSettings= new HashMap<MARKTYPE,Boolean>();

        for(MARKTYPE landMark : MARKTYPE.values())
            filterSettings.put(landMark,true);
    }

    public static Settings getInstance(){
        if(settings==null){
            settings=new Settings();
        }
        return settings;
    }

    public boolean isVisible(MARKTYPE landmark){
        return filterSettings.get(landmark);
    }

    public void setFilter(MARKTYPE landmark,boolean value){
        filterSettings.put(landmark,value);
    }
}
