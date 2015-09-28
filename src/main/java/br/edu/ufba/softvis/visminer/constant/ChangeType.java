package br.edu.ufba.softvis.visminer.constant;

public enum ChangeType {

	NONE(0),
	ADD(1),
	DELETE(2),
	MODIFY(3);
	
	private int id;
	
	private ChangeType(int id){
		this.id = id;
	}
	
	public int getId(){
		return this.id;
	}
	
	public static ChangeType parse(int id){
		
		for(ChangeType ct : ChangeType.values()){
			if(id == ct.getId()){
				return ct;
			}
		}
		
		return null;
		
	}
	
}