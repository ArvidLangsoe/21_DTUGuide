package com.arvid.dtuguide.data;


import android.os.Build;
import android.support.annotation.RequiresApi;

import com.arvid.dtuguide.navigation.coordinates.GeoPoint;

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

	public LocationDTO parseToDTO(String string){
		LocationDTO dto;

		String[] info = string.split(";");
		String name = info[0];
		double l1 = Double.parseDouble((info[1].split("-"))[0]);
		double l2 = Double.parseDouble((info[1].split("-"))[1]);
		GeoPoint position = new GeoPoint(l1, l2);
		int stage = Integer.parseInt(info[2]);
		String description = info[3];

		LocationDTO.MARKTYPE type;

		if(!info[4].equals("null"))
			type = LocationDTO.MARKTYPE.valueOf(info[4]);
		else
			type = null;

		dto = new LocationDTO(new LocationDTO.LocationBuilder(
				name,
				position,
				stage)
				.description(description)
				.type(type));


		return dto;
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
