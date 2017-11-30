package com.arvid.dtuguide.data;

import android.support.annotation.NonNull;

import com.arvid.dtuguide.navigation.coordinates.GeoPoint;

import java.io.Serializable;

public class LocationDTO implements Serializable, Comparable<LocationDTO> {

    public enum MARKTYPE {
        CANTEEN,
        WC,

    }

    public static class LocationBuilder {
        //Needed
        private GeoPoint position;
        private String name;
        private int stage;

        //Optionnal
        private MARKTYPE type;
        private String description;

        public LocationBuilder(String name, GeoPoint position, int stage){
            this.name = name;
            this.position = position;

            if(stage > 0 && stage < 3)
                this.stage = stage;
            else
                this.stage = 0;
        }

        public LocationBuilder type(MARKTYPE landmarktype){
            this.type = landmarktype;
            return this;
        }

        public LocationBuilder description(String description){
            this.description = description;
            return this;
        }
    }

    private static final long serialVersionUID = 5555874847854456L;
    //Needed
    private GeoPoint position;
    private String name;
    private int stage;
    private MARKTYPE type;
    private String description;

    public LocationDTO(LocationBuilder builder){
        this.name = builder.name;
        this.position = builder.position;
        this.stage = builder.stage;
        this.type = builder.type;
        this.description = builder.description;

    }

    @Override
    public int compareTo(@NonNull LocationDTO o) {
        return this.name.compareToIgnoreCase(o.name);
    }

    public String toString(){
        return "(Name:"+name+" - Pos:"+position+" - Stage:"+stage+" - Desc:'"+description+"' - Type:"+type+"\n";
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public GeoPoint getPosition() {
        return position;
    }

    public void setPosition(GeoPoint position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MARKTYPE getType() {return type;}

    public void setType(MARKTYPE type) {this.type = type;}

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

}
