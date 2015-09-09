package br.edu.ufba.softvis.visminer.constant;

public enum StatusType {

	OPEN("open", 1), 
	CLOSED("closed", 2);
	
	private String code;
	private int id;
	
	private StatusType(String code, int id){
		this.code = code;
		this.id = id;
	}
	
	public int getId(){
		return this.id;
	}
	
	public String getCode(){
		return this.code;
	}
	
	public static StatusType parse(String code){
		
		for(StatusType issueState : StatusType.values()){
			if(issueState.getCode().equals(code)){
				return issueState;
			}
		}
		
		return null;
	}
	
	public static StatusType parse(int id){
		
		for(StatusType issueState : StatusType.values()){
			if(issueState.getId() == id){
				return issueState;
			}
		}
		
		return null;
	}
	
}
