package com.arvid.dtuguide.data;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class LocationDTO extends Searchable implements Serializable{

    private static final long serialVersionUID = 5555874847854456L;
    //Needed
    private LatLng position;
    private int floor;
    private String description;
    private MARKTYPE landmark;


    public String toString(){
        return "(Name:"+this.getName()+" - Pos:"+position+" - Stage:"+floor+" - Desc:'"+description+"\n";
    }

    public int getFloor() {
        return floor;
    }

    public LocationDTO setFloor(int floor) {
        this.floor = floor;
        return this;
    }

    public MARKTYPE getLandmark() {
        return landmark;
    }

    public LocationDTO setLandmark(MARKTYPE landmark) {
        this.landmark = landmark;
        return this;
    }

    public LatLng getPosition() {
        return position;
    }

    public LocationDTO setPosition(LatLng position) {
        this.position = position;
        return this;
    }

    public LocationDTO setDescription(String description) {this.description = description;
        return this;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
