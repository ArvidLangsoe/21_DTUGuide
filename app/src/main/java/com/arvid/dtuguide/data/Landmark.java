package dto;

public class Landmark extends Location{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 48784541564156456L;

	public enum MARKTYPE {
		CANTEEN,
		WC,
		
	}

	private MARKTYPE type;
	
	public Landmark(double position, String name, short stage, MARKTYPE type) {
		super(position, name, stage);
		
		this.type = type;
	}

	public MARKTYPE getType() {
		return type;
	}

	public void setType(MARKTYPE type) {
		this.type = type;
	}

}
