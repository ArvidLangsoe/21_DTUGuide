package com.arvid.dtuguide.data;


import android.os.Build;
import android.support.annotation.RequiresApi;


import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class LocationDAO {

	private boolean importDataStatus;
	private List<Searchable> list;

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
	public List<Searchable> searchData(final String search) throws DAOException {
		list = new ArrayList<>();
		importDataStatus = false;
		if(search.length()<2){
			return list;
		}

		new Thread(new Runnable() {
			public void run() {
				//searchables.clear();
				list=convertStringJSON(retrieveFromStringMatch(search, 50, 1));
				for(Searchable s : list)
					data.put(s.getName(),s);
				importDataStatus = true;
			}
		}).start();

		int count =0;
		while(!importDataStatus&&count<500){
			try {
				count++;
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println("CORRECT DATA : " + list);
		return list;
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

	private static String convertBufferStreamToString(BufferedReader reader) throws IOException{
		BufferedReader bufferedReader = reader;
		String line = "";
		String result = "";
		while((line = bufferedReader.readLine()) != null)
			result += line;

		reader.close();
		return result;

	}

	private List<Searchable> convertStringJSON(String jsonString) {
		List<Searchable> tempList = Collections.synchronizedList(new ArrayList<Searchable>());
		try{

		JsonElement root = new JsonParser().parse(jsonString);

		JsonArray array = root.getAsJsonObject()
				.get("data").getAsJsonArray();

		LocationDTO locationDTO;


			for(int i=0; i<array.size(); i++) {

				locationDTO = new LocationDTO();

				String name = array
						.get(i).getAsJsonObject()
						.get("name").getAsString();

				String description = array
						.get(i).getAsJsonObject()
						.get("description").getAsString();

				String landmark = array
						.get(i).getAsJsonObject()
						.get("landmark").getAsString();

				double latitude = array
						.get(i).getAsJsonObject()
						.get("latitude").getAsDouble();

				double longitude = array
						.get(i).getAsJsonObject()
						.get("longitude").getAsDouble();

				int floor = array
						.get(i).getAsJsonObject()
						.get("floor").getAsInt();

				locationDTO.setName(name);
				locationDTO.setDescription(description);
				locationDTO.setFloor(floor);
				locationDTO.setLandmark(MARKTYPE.valueOf(landmark));
				locationDTO.setPosition(new LatLng(latitude,longitude));



				tempList.add(locationDTO);

				System.out.println("DATA NAME LOCATION : " + locationDTO);

			}
			return tempList;
		} catch (IndexOutOfBoundsException e) {
			System.out.println(e);
		}catch (Exception e){
			e.printStackTrace();
		}
		return tempList;
	}

	public String retrieveFromStringMatch(String match, int numberOfElements, int pageNumber){
		BufferedReader br = null;

		try {

			URL url = new URL("http://arvid-langsoe.dk/REST/searchable?type=location&searchString="+match+"&limit="+numberOfElements+"&sort=name&page="+pageNumber);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode()).printStackTrace();
				return "";
			}

			br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

		try {
			return convertBufferStreamToString(br);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "";
	}
}
