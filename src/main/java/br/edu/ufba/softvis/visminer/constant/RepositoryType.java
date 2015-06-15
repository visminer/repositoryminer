package br.edu.ufba.softvis.visminer.constant;

public enum RepositoryType {

	GIT(1);
	
	private int id;
	
	private RepositoryType(int id){
		this.id = id;
	}
	
	public int getId(){
		return this.id;
	}
	
	public static RepositoryType parse(int id){
		
		for(RepositoryType repoType : RepositoryType.values()){
			if(repoType.getId() == id){
				return repoType;
			}
		}
		
		throw new IllegalArgumentException("No repository type found for id "+id);
		
	}
	
}
