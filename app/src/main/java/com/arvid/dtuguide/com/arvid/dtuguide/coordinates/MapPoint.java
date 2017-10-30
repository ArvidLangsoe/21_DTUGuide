package com.arvid.dtuguide.com.arvid.dtuguide.coordinates;

import java.util.Map;

/**
 * Created by arvid on 23-10-2017.
 */

public class MapPoint {

    private double x;
    private double y;

    public MapPoint(){

    }

    public MapPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "MapPoint{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
