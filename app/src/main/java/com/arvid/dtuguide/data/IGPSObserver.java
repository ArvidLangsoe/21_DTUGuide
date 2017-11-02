package com.arvid.dtuguide.data;

import android.os.Bundle;

import com.arvid.dtuguide.navigation.coordinates.GeoPoint;

/**
 * Created by peter on 11/1/2017.
 */

public interface IGPSObserver {
    public void updateGPSLocation(GeoPoint position);

    public void notifyProviderStatus(boolean newStatus);

    public void notifyStatusChanged(String provider, int status, Bundle extras);
}
