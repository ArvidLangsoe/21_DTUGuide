package com.arvid.dtuguide;

/**
 * Created by arvid on 23-10-2017.
 */



public class CoordinateConverter {
    private GeoPoint geo1;
    private GeoPoint geo2;

    private MapPoint map1;
    private MapPoint map2;

    private double geoDistance;
    private double mapDistance;

    private double geoToMapSizeRatio;
    private double mapToGeoSizeRatio;


    private double mapAngle;
    private double geoAngle;

    private double geoToMapAngle;
    private double mapToGeoAngle;


    public CoordinateConverter(GeoPoint geoBase1, GeoPoint geoBase2, MapPoint mapBase1, MapPoint mapBase2) {
        this.geo1 = geoBase1;
        this.geo2 = geoBase2;
        this.map1 = mapBase1;
        this.map2 = mapBase2;
        calculateConstants();
    }

    private double calculateDistance(double x1, double x2,double y1,double y2){
        return Math.abs(Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2)));

    }

    private double calculateAngle(double x1, double x2,double y1,double y2){
        double vx=x2-x1;
        double vy=y2-y1;

        double dot = 1*vx+0*vy;
        return Math.acos(dot/1*calculateDistance(x1,x2,y1,y2));
    }

    private void calculateConstants(){
        geoDistance=calculateDistance(geo1.getLong(), geo2.getLong(), geo1.getLat(), geo2.getLat());
        mapDistance=calculateDistance(map1.getX(), map2.getX(), map1.getY(), map2.getY());

        geoToMapSizeRatio=mapDistance/geoDistance;
        mapToGeoSizeRatio=geoDistance/mapDistance;

        mapAngle=calculateAngle(map1.getX(), map2.getX(), map1.getY(), map2.getY());
        geoAngle=calculateAngle(geo1.getLong(), geo2.getLong(), geo1.getLat(), geo2.getLat());

        geoToMapAngle=mapAngle-geoAngle;
        mapToGeoAngle=-geoToMapAngle;



    }


    public void geoToMap(GeoPoint  geo){
        double xtemp=geo.getLong();
        double ytemp=geo.getLat();

        //Scale the coordinates
        xtemp=xtemp*geoToMapSizeRatio;
        ytemp=ytemp*geoToMapSizeRatio;

        //Rotate coordinates: https://en.wikipedia.org/wiki/Rotation_of_axes#Method_2
        double xscaled=xtemp*Math.cos(geoToMapAngle)+ytemp*Math.sin(geoToMapAngle);
        double yscaled=-xtemp*Math.sin(geoToMapAngle)+ytemp*Math.cos(geoToMapAngle);


        //Translate coordinates.
        //TODO: TranslateCoordinates





    }



}
