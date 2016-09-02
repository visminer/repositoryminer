package org.repositoryminer.model.effort;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

public class EffortCategoriesByReference {

	private String repository;
	private String reference;
	private String referenceName;
	private List<EffortCategory> categories = new ArrayList<EffortCategory>();

	public static List<EffortCategoriesByReference> parseDocuments(List<Document> effortsByCategoryDocs) {
		List<EffortCategoriesByReference> effortCategoriesByRef = new ArrayList<EffortCategoriesByReference>();
		for (Document doc : effortsByCategoryDocs) {
			effortCategoriesByRef.add(parseDocument(doc));
		}
		return effortCategoriesByRef;
	}

	@SuppressWarnings("unchecked")
	public static EffortCategoriesByReference parseDocument(Document effortsByCategoryDoc) {
		EffortCategoriesByReference effortCategoriesByRef = new EffortCategoriesByReference();
		effortCategoriesByRef.setRepository(effortsByCategoryDoc.getString("repository"));
		effortCategoriesByRef.setReference(effortsByCategoryDoc.getString("reference"));
		effortCategoriesByRef.setReferenceName(effortsByCategoryDoc.getString("reference_name"));
		effortCategoriesByRef.setCategories(
				EffortCategory.parseDocuments((List<Document>) effortsByCategoryDoc.get("categories")));

		return effortCategoriesByRef;
	}

	public Document toDocument() {
		Document doc = new Document();

		return doc.append("repository", repository).append("reference", reference)
				.append("reference_name", referenceName)
				.append("categories", EffortCategory.toDocumentList(getCategories()));
	}

	public EffortCategoriesByReference() {
	}

	public EffortCategoriesByReference(String repository, String reference, String referenceName,
			List<EffortCategory> categories) {
		this.repository = repository;
		this.reference = reference;
		this.referenceName = referenceName;
		this.categories = categories;
	}

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getReferenceName() {
		return referenceName;
	}

	public void setReferenceName(String referenceName) {
		this.referenceName = referenceName;
	}

	public List<EffortCategory> getCategories() {
		return categories;
	}

	public void setCategories(List<EffortCategory> categories) {
		this.categories = categories;
	}

}
