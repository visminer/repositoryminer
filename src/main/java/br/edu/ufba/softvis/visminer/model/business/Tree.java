package br.edu.ufba.softvis.visminer.model.business;

import java.util.Date;

import br.edu.ufba.softvis.visminer.constant.TreeType;

public class Tree {

	private int id;
	private Date lastUpdate;
	private String name;
	private String fullName;
	private TreeType type;
	
	public Tree(){}
	
	public Tree(int id, Date lastUpdate, String name, String fullName,
			TreeType type) {
		super();
		this.id = id;
		this.lastUpdate = lastUpdate;
		this.name = name;
		this.fullName = fullName;
		this.type = type;
	}

	public Tree(Date lastUpdate, String name, String fullName,
			TreeType type) {
		super();
		this.lastUpdate = lastUpdate;
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

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
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

	public TreeType getType() {
		return type;
	}

	public void setType(TreeType type) {
		this.type = type;
	}	
	
}