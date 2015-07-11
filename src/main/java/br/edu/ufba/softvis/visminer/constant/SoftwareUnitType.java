package br.edu.ufba.softvis.visminer.constant;

/**
 * @version 0.9
 * Software units types.
 */
public enum SoftwareUnitType {

	PROJECT(1),
	PACKAGE(2),
	FILE(3),
	CLASS(4),
	METHOD(5),
	ENUM(6),
	ENUM_CONST(7);
	
	private int id;
	
	private SoftwareUnitType(int id){
		this.id = id;
	}
	
	/**
	 * @return Software unit type id.
	 */
	public int getId(){
		return this.id;
	}
	
	/**
	 * @param id
	 * @return Software unit type with given id.
	 */
	public static SoftwareUnitType parse(int id){
		
		for(SoftwareUnitType softwareUnitType : SoftwareUnitType.values()){
			if(softwareUnitType.getId() == id){
				return softwareUnitType;
			}
		}

		throw new IllegalArgumentException("Does not exists SoftwareUnitType with id "+id);
		
	}
	
}
