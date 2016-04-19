package org.repositoryminer.model;

public enum ReferenceType {

	BRANCH("BRANCH"), TAG("TAG");

	private String id;

	private ReferenceType(String id) {
		this.id = id;
	}

	public String getId(){
		return id;
	}
	
	public static ReferenceType parse(String id) {
		for(ReferenceType rt : ReferenceType.values()){
			if(rt.getId().equals(id))
				return rt;
		}
		return null;
	}

}