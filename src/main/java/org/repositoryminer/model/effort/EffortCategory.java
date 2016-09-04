package org.repositoryminer.model.effort;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

public class EffortCategory {

	private String category;
	private List<Effort> efforts;

	@SuppressWarnings("unchecked")
	public static List<EffortCategory> parseDocuments(List<Document> categoryDocs) {
		List<EffortCategory> categories = new ArrayList<EffortCategory>();
		for (Document doc : categoryDocs) {
			EffortCategory category = new EffortCategory();
			category.setCategory(doc.getString("category"));
			category.setEfforts(Effort.parseDocuments((List<Document>)doc.get("efforts")));

			categories.add(category);
		}

		return categories;
	}

	public Document toDocument() {
		Document doc = new Document();
		doc.append("category", category).append("efforts", Effort.toDocumentList(getEfforts()));
		
		return doc; 
	}

	public static List<Document> toDocumentList(List<EffortCategory> categories) {
		List<Document> list = new ArrayList<Document>();
		for (EffortCategory category : categories) {
			Document doc = new Document();
			doc.append("category", category.getCategory());
			doc.append("efforts", Effort.toDocumentList(category.getEfforts()));
			
			list.add(doc);
		}

		return list;
	}

	public EffortCategory() {
	}

	public EffortCategory(String category, List<Effort> efforts) {
		this.category = category;
		this.efforts = efforts;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<Effort> getEfforts() {
		return efforts;
	}

	public void setEfforts(List<Effort> efforts) {
		this.efforts = efforts;
	}

}
