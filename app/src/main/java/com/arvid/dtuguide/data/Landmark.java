package com.arvid.dtuguide.data;

/**
 * Created by peter on 1/8/2018.
 */

public class Landmark extends LocationDTO{
    private MARKTYPE landmarktype;

    public MARKTYPE getLandmarktype() {
        return landmarktype;
    }

    public Landmark setLandmarktype(MARKTYPE landmarktype) {
        this.landmarktype = landmarktype;
        return this;
    }
}
