package com.arvid.dtuguide.navigation;

import android.os.IBinder;
import android.os.RemoteException;

import com.google.android.gms.maps.model.internal.zzj;

import java.util.List;

/**
 * Created by arvid on 08-01-2018.
 */

public class BuildingModel implements com.google.android.gms.maps.model.internal.zzj {
    int currentLevel=0;

    @Override
    public int getActiveLevelIndex() throws RemoteException {
        return currentLevel;
    }

    @Override
    public int getDefaultLevelIndex() throws RemoteException {
        return 0;
    }

    @Override
    public List<IBinder> getLevels() throws RemoteException {
        return null;
    }

    @Override
    public boolean isUnderground() throws RemoteException {
        return false;
    }

    @Override
    public boolean zzb(zzj zzj) throws RemoteException {
        return false;
    }

    @Override
    public int hashCodeRemote() throws RemoteException {
        return 0;
    }

    @Override
    public IBinder asBinder() {
        return null;
    }
}
