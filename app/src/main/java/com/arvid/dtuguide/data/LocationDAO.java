package com.arvid.dtuguide.data;


import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.HashMap;

public class LocationDAO {
	private HashMap<String, LocationDTO> locations;
	
	public LocationDAO(){
		locations = new HashMap<String, LocationDTO>();
	}

	private boolean updateData(){
		try{
			FileManager.writeData(locations);

			return true;

		}catch(Exception e){

			return false;
		}
	}

	public HashMap<String, LocationDTO> getLocations() {
		locations = FileManager.retrieveData();
		return locations;
	}

	public boolean setLocations(HashMap<String, LocationDTO> locations) {
		this.locations = locations;
		return updateData();
	}

	public LocationDTO getLocation(String name){
		getLocations();
		return locations.get(name);
	}

	public boolean saveLocation(LocationDTO newLocation){
		locations.put(newLocation.getName(), newLocation);
		return updateData();
	}

	public boolean deleteLocation(String name){
		locations.remove(name);
		return updateData();
	}

	@RequiresApi(api = Build.VERSION_CODES.N)
	public boolean updateLocation(LocationDTO newLocation){
		locations.replace(newLocation.getName(), newLocation);
		return updateData();
	}
}
