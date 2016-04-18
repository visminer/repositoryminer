package org.repositoryminer.model;

public enum ReferenceType {

	BRANCH("BRANCH"), TAG("TAG");

	private String key;

	private ReferenceType(String key) {
		this.key = key;
	}

	public String getKey(){
		return key;
	}
	
	public static ReferenceType parse(String key) {
		for(ReferenceType rt : ReferenceType.values()){
			if(rt.getKey().equals(key))
				return rt;
		}
		return null;
	}

}