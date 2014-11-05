package org.visminer.model.business;


public class Tree {

	private int id;
	private String fullName;
	private String name;
	private int type;
	
	public Tree(int id, String name, String fullName, int type) {
		super();
		this.id = id;
		this.name = name;
		this.fullName = fullName;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
}
