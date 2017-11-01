package com.arvid.dtuguide.navigation.coordinates;

/**
 * Created by arvid on 23-10-2017.
 */



public class CoordinateConverter {


    private final double radius = 6371;

    //Point 1 is the translation coordinates.

    private GeoPoint geo1;
    private GeoPoint geo2;

    private MapPoint mercator1;
    private MapPoint mercator2;

    private MapPoint map1;
    private MapPoint map2;

    private double mercatorDistance;
    private double mapDistance;

    private double mercXDistance;
    private double mercYDistance;
    private double mapXDistance;
    private double mapYDistance;


    private double mercatorToMapAngle;



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

        return Math.acos(dot/(1*calculateDistance(x1,x2,y1,y2)));
    }



    private MapPoint rotatePoint(MapPoint p,double angle){
        double xRot=p.getX()*Math.cos(angle)+p.getY()*Math.sin(angle);
        double yRot=-p.getX()*Math.sin(angle)+p.getY()*Math.cos(angle);

        return new MapPoint(xRot,yRot);
    }

    private MapPoint scalePoint(MapPoint p,double scaleX,double scaleY){
        double xScaled= p.getX()*scaleX;
        double yScaled= p.getY()*scaleY;
        return new MapPoint(xScaled,yScaled);
    }

    private MapPoint geoToMercator(GeoPoint point){
        MapPoint mercator = new MapPoint();

        double x=radius*(degreeToRadian(point.getLong()));

        double temp = (Math.PI/4)+(degreeToRadian(point.getLat())/2);
        double y= radius*Math.log(Math.tan(temp));

        mercator.setX(x);
        mercator.setY(y);

        return mercator;
    }

    private double degreeToRadian(double degree){
        return degree*Math.PI/180;
    }

    private void calculateConstants(){
        mercator1=geoToMercator(geo1);
        mercator2=geoToMercator(geo2);

        mercatorDistance=calculateDistance(mercator1.getX(),mercator2.getX(),mercator1.getY(),mercator2.getY());
        mapDistance=calculateDistance(map1.getX(),map2.getX(),map1.getY(),map2.getY());

        mercXDistance =mercator2.getX()-mercator1.getX();
        mercYDistance =mercator2.getY()-mercator1.getY();

        mapXDistance=map2.getX()-map1.getX();
        mapYDistance=map2.getY()-map1.getY();

        double mercatorAngle=calculateAngle(mercator1.getX(),mercator2.getX(),mercator1.getY(),mercator2.getY());
        double mapAngle = calculateAngle(map1.getX(),map2.getX(),map1.getY(),map2.getY());

        mercatorToMapAngle=mapAngle-mercatorAngle;
    }

    public MapPoint geoToMap(GeoPoint gP){
        MapPoint mercator=geoToMercator(gP);

        System.out.println("Mercator:" + mercator);
        MapPoint mercTrans = new MapPoint(mercator.getX()-mercator1.getX(),mercator.getY()-mercator1.getY());
        System.out.println("Mercator translated:" +mercTrans);


        MapPoint mercRot= rotatePoint(mercTrans,mercatorToMapAngle);
        System.out.println("Mercator rotated:" +mercRot);

        MapPoint mapPointTrans = scalePoint(mercRot,mapXDistance/mercXDistance,mapYDistance/mercYDistance);
        System.out.println("Mercator scaled:" +mapPointTrans);

        return new MapPoint(mapPointTrans.getX()+map1.getX(),mapPointTrans.getY()+map1.getY());
    }




}
