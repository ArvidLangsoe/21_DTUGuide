package com.arvid.dtuguide.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class FileManager {

	final static String PATH = "";

	public static void writeData(HashMap<String, LocationDTO> dto){
		FileOutputStream f = null;
		ObjectOutputStream o = null;

		try{
			f = new FileOutputStream(new File(PATH+"LOCATIONS.data"));
			o = new ObjectOutputStream(f);

			o.writeObject(dto);

		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			System.out.println("Error initializing stream " +e);

		}finally{
			try {
				o.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				f.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	

	/**
	 * Retrieves Data from a specific file.
	 * @return Retrieved Data
	 */
	public static HashMap<String, LocationDTO> retrieveData(){

		FileInputStream fi = null;
		ObjectInputStream oi = null;

		try{
			fi = new FileInputStream(new File(PATH+"LOCATIONS.data"));
			oi = new ObjectInputStream(fi);

			@SuppressWarnings("unchecked")
			HashMap<String, LocationDTO> dto = (HashMap<String, LocationDTO>) oi.readObject();

			return dto;

		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			System.out.println(e+" ---------------------- ");
			System.out.println("Error initializing stream "+e);
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();

		}finally{
			try {
				oi.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fi.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;

	}
}
