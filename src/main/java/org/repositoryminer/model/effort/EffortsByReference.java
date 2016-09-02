package org.repositoryminer.model.effort;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

public class EffortsByReference {

	private String repository;
	private String reference;
	private String referenceName;
	private List<Effort> efforts = new ArrayList<Effort>();

	public static List<EffortsByReference> parseDocuments(List<Document> effortsByReferenceDocs) {
		List<EffortsByReference> effortsByRefColl = new ArrayList<EffortsByReference>();
		for (Document doc : effortsByReferenceDocs) {
			effortsByRefColl.add(parseDocument(doc));
		}
		return effortsByRefColl;
	}

	@SuppressWarnings("unchecked")
	public static EffortsByReference parseDocument(Document effortsByReferenceDoc) {
		EffortsByReference effortsByRef = new EffortsByReference();
		effortsByRef.setRepository(effortsByReferenceDoc.getString("repository"));
		effortsByRef.setReference(effortsByReferenceDoc.getString("reference"));
		effortsByRef.setReferenceName(effortsByReferenceDoc.getString("reference_name"));
		effortsByRef.setEfforts(Effort.parseDocuments((List<Document>) effortsByReferenceDoc.get("efforts")));
		
		return effortsByRef;
	}
	
	public Document toDocument() {
		Document doc = new Document();

		return doc.append("repository", repository).append("reference", reference)
				.append("reference_name", referenceName).append("efforts", Effort.toDocumentList(getEfforts()));
	}

	public EffortsByReference() {
	}

	public EffortsByReference(String repository, String reference, String referenceName, List<Effort> efforts) {
		this.repository = repository;
		this.reference = reference;
		this.referenceName = referenceName;
		this.efforts = efforts;
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

	public List<Effort> getEfforts() {
		return efforts;
	}

	public void setEfforts(List<Effort> efforts) {
		this.efforts = efforts;
	}

}
