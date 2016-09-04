package org.repositoryminer.model.effort;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

/**
 * <h1>A category wrapper for efforts</h1>
 * <p>
 * This class can be used to attach a category name to a list of efforts. For
 * instance, one may want to rank efforts according to a taxonomy composed of
 * classifications: MAJOR, MINOR, SUB-MAJOR, etc.
 * <p>
 * An example of categorization of efforts can be found in
 * {@link org.repositoryminer.postprocessing.effort.EffortCategoriesMiningTask}
 */
public class EffortCategory {

	private String category;
	private List<Effort> efforts;

	@SuppressWarnings("unchecked")
	public static List<EffortCategory> parseDocuments(List<Document> categoryDocs) {
		List<EffortCategory> categories = new ArrayList<EffortCategory>();
		for (Document doc : categoryDocs) {
			EffortCategory category = new EffortCategory();
			category.setCategory(doc.getString("category"));
			category.setEfforts(Effort.parseDocuments((List<Document>) doc.get("efforts")));

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
