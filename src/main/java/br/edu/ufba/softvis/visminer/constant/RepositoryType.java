package br.edu.ufba.softvis.visminer.constant;

/**
 * @version 0.9
 * Repository types
 */
public enum RepositoryType {

	GIT(1);
	
	private int id;
	
	private RepositoryType(int id){
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
	public static RepositoryType parse(int id){
		
		for(RepositoryType repoType : RepositoryType.values()){
			if(repoType.getId() == id){
				return repoType;
			}
		}
		
		throw new IllegalArgumentException("Does not exists RepositoryType with id "+id);
		
	}
	
}
