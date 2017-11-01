package com.arvid.dtuguide.data;


import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Hashtable;

import controller.FileManager;
import dto.Location;

public class LocationDAO {
	private Hashtable<String, Location> locations;
	
	public LocationDAO(){
		locations = new Hashtable<String, Location>();
	}

	private boolean updateData(){
		try{
			FileManager.writeData(locations);

			return true;

		}catch(Exception e){

			return false;
		}
	}

	public Hashtable<String, Location> getLocations() {
		locations = FileManager.retrieveData();
		return locations;
	}

	public boolean setLocations(Hashtable<String, Location> locations) {
		this.locations = locations;
		return updateData();
	}

	public Location getLocation(String name){
		getLocations();
		return locations.get(name);
	}

	public boolean saveLocation(Location newLocation){
		locations.put(newLocation.getName(), newLocation);
		return updateData();
	}

	public boolean deleteLocation(String name){
		locations.remove(name);
		return updateData();
	}

	@RequiresApi(api = Build.VERSION_CODES.N)
	public boolean updateLocation(Location newLocation){
		locations.replace(newLocation.getName(), newLocation);
		return updateData();
	}
}
