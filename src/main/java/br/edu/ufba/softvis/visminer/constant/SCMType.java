package br.edu.ufba.softvis.visminer.constant;

/**
 * @version 0.9
 * Repository types
 */
public enum SCMType {

	NONE(0),
	GIT(1);
	
	private int id;
	
	private SCMType(int id){
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
	public static SCMType parse(int id){
		
		for(SCMType repoType : SCMType.values()){
			if(repoType.getId() == id){
				return repoType;
			}
		}

		return null;
		
	}
	
}
