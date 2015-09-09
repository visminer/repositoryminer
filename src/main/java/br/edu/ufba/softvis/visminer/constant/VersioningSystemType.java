package br.edu.ufba.softvis.visminer.constant;

/**
 * @version 0.9
 * Repository types
 */
public enum VersioningSystemType {

	NONE(0),
	GIT(1);
	
	private int id;
	
	private VersioningSystemType(int id){
		this.id = id;
	}
	
	/**
	 * 
	 * @return Repository type id.
	 */
	public int getId(){
		return this.id;
	}
	
	/**
	 * 
	 * @param id
	 * @return Repository type with given id.
	 */
	public static VersioningSystemType parse(int id){
		
		for(VersioningSystemType repoType : VersioningSystemType.values()){
			if(repoType.getId() == id){
				return repoType;
			}
		}

		return null;
		
	}
	
}
