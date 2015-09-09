package br.edu.ufba.softvis.visminer.constant;

/**
 * @version 0.9
 * Web repository types.
 */
public enum WebServiceType {

	NONE(0),
	GITHUB(1);
	
	private int id;
	
	private WebServiceType(int id){
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
	public static WebServiceType parse(int id){
		
		for(WebServiceType repoServType : WebServiceType.values()){
			if(repoServType.getId() == id){
				return repoServType;
			}
		}

		return null;
		
	}
	
}
