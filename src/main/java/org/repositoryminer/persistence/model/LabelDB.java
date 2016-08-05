package org.repositoryminer.persistence.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

public class LabelDB {

	private String name;
	private String color;
	
	public LabelDB() {}

	public LabelDB(String name, String color) {
		super();
		this.name = name;
		this.color = color;
	}

	public static List<Document> toDocumentList(List<LabelDB> labels) {
		if (labels == null) {
			return null;
		}
		
		List<Document> docs = new ArrayList<Document>();
		for (LabelDB l : labels) {
			docs.add(new Document("name", l.name).append("color", l.color));
		}
		return docs;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
}