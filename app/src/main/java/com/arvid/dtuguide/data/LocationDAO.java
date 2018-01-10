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

	private static HashMap<String, Searchable> data;

	public LocationDAO(){
		try {
			getAllData();
		} catch (Exception e) {
			setData(new HashMap<String, Searchable>());
		}
	}

	public HashMap<String, Searchable> getAllData() throws DAOException {
		if(data !=null)
			return data;
		else
			throw new DAOException("Locations can not be found (Null value).");
	}

	public boolean setData(HashMap<String, Searchable> data) {
		this.data = data;
		return true;
	}

	public Searchable getData(String name) throws DAOException {
		Searchable dto = data.get(name);

		if(dto!=null)
			return dto;
		else
			throw new DAOException("Searchable "+name+" can not be found (Null value)");
	}

	public boolean saveData(Searchable newSearchable){
		data.put(newSearchable.getName(), newSearchable);
		return true;
	}


	@RequiresApi(api = Build.VERSION_CODES.N)
	public boolean updateLocation(Searchable newSearchable){
		data.replace(newSearchable.getName(), newSearchable);
		return true;
	}
}
