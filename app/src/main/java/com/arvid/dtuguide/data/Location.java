package com.arvid.dtuguide.data;

import java.io.Serializable;

public class Location implements Serializable {

	/**
	 * 
	 */
	public class Position{
		private double x, y;

		public Position(double x, double y){
			this.x = x;
			this.y = y;
		}

		public double[] getCoordinates(){
			double[] result = new double[2];
			result[0] = this.x;
			result[1] = this.y;

			return result;
		}
	}

	public enum MARKTYPE {
		CANTEEN,
		WC,

	}

	public static class LocationBuilder {
		//Needed
		private Position position;
		private String name;
		private int stage;

		//Optionnal
		private MARKTYPE type;
		private String description;

		public LocationBuilder(String name, Position position, int stage){
			this.name = name;
			this.position = position;

			if(stage > 0 && stage < 3)
				this.stage = stage;
			else
				this.stage = 0;
		}

		public LocationBuilder type(MARKTYPE landmarktype){
			this.type = landmarktype;
			return this;
		}

		public LocationBuilder description(String description){
			this.description = description;
			return this;
		}
	}

	private static final long serialVersionUID = 5555874847854456L;
	//Needed
	private Position position;
	private String name;
	private int stage;
	private MARKTYPE type;
	private String description;

	public Location(LocationBuilder builder){
		this.name = builder.name;
		this.position = builder.position;
		this.stage = builder.stage;
		this.type = builder.type;
		this.description = builder.description;

	}

	public String toString(){
		return position+", "+name;
	}

	public int getStage() {
		return stage;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MARKTYPE getType() {return type;}

	public void setType(MARKTYPE type) {this.type = type;}

	public String getDescription() {return description;}

	public void setDescription(String description) {this.description = description;}

}
