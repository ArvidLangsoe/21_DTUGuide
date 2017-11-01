package dto;

import java.io.Serializable;

public class Location implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5555874847854456L;
	private double position;
	private String name;
	private short stage;

	public Location(double position, String name, short stage){
		this.position = position;
		this.name = name;

		if(stage > 0 && stage < 3)
			this.stage = stage;
		else
			this.stage = 0;
	}

	public String toString(){
		return position+", "+name;
	}

	public short getStage() {
		return stage;
	}

	public void setStage(short stage) {
		this.stage = stage;
	}

	public double getPosition() {
		return position;
	}

	public void setPosition(double position) {
		this.position = position;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
