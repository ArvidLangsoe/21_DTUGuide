package com.arvid.dtuguide.data;


import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.HashMap;

public class LocationDAO {

	public class DAOException extends Exception{
		public DAOException(String message){super(message);}
		public String toString(){return getMessage();}
	}

	private HashMap<String, LocationDTO> locations;

	public LocationDAO(){
		try {
			getLocations();
		} catch (Exception e) {
			setLocations(new HashMap<String, LocationDTO>());
		}
	}

	private boolean updateData(){
		try{
			FileManager.writeData(locations);

			return true;

		}catch(Exception e){

			return false;
		}
	}

	public HashMap<String, LocationDTO> getLocations() throws DAOException {
		locations = FileManager.retrieveData();
		if(locations !=null)
			return locations;
		else
			throw new DAOException("Locations can not be found (Null value).");
	}

	public boolean setLocations(HashMap<String, LocationDTO> locations) {
		this.locations = locations;
		return updateData();
	}

	public LocationDTO getLocation(String name) throws DAOException {
		LocationDTO location = locations.get(name);

		if(location!=null)
			return location;
		else
			throw new DAOException("Location "+name+" can not be found (Null value)");
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
