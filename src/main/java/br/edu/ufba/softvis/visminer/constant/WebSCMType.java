package br.edu.ufba.softvis.visminer.constant;

/**
 * @version 0.9
 * Web repository types.
 */
public enum WebSCMType {

	NONE(0),
	GITHUB(1);
	
	private int id;
	
	private WebSCMType(int id){
		this.id = id;
	}
	
	/**
	 * @return Web repository type id.
	 */
	public int getId(){
		return this.id;
	}
	
	/**
	 * @param id
	 * @return Web repository type with given id.
	 */
	public static WebSCMType parse(int id){
		
		for(WebSCMType repoServType : WebSCMType.values()){
			if(repoServType.getId() == id){
				return repoServType;
			}
		}

		return null;
		
	}
	
}
