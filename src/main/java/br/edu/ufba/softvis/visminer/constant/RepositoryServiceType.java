package br.edu.ufba.softvis.visminer.constant;

public enum RepositoryServiceType {

	GITHUB(1);
	
	private int id;
	
	private RepositoryServiceType(int id){
		this.id = id;
	}
	
	public int getId(){
		return this.id;
	}
	
	public static RepositoryServiceType parse(int id){
		
		for(RepositoryServiceType repoServType : RepositoryServiceType.values()){
			if(repoServType.getId() == id){
				return repoServType;
			}
		}
		
		throw new IllegalArgumentException("No repository service type found for id "+id);
		
	}
	
}
