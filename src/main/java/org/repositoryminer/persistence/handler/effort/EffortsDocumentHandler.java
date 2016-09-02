package org.repositoryminer.persistence.handler.effort;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.model.effort.Effort;
import org.repositoryminer.model.effort.EffortsByReference;
import org.repositoryminer.persistence.Connection;
import org.repositoryminer.persistence.handler.DocumentHandler;

import com.mongodb.BasicDBObject;

public class EffortsDocumentHandler extends DocumentHandler {

	private static final String COLLECTION_NAME = "effort_by_reference";

	public EffortsDocumentHandler() {
		super.collection = Connection.getInstance().getCollection(COLLECTION_NAME);
	}

	public Document getByReference(String referenceId) {
		BasicDBObject whereClause = new BasicDBObject();
		whereClause.put("reference", referenceId);

		return findOne(whereClause, null);
	}

	public Document getByReferenceName(String referenceName) {
		BasicDBObject whereClause = new BasicDBObject();
		whereClause.put("reference_name", referenceName);

		return findOne(whereClause, null);
	}

	public void effortsToCVS(String referenceName, String cvsFilePath) throws FileNotFoundException {
		Document refDoc = getByReferenceName(referenceName);
		if (refDoc != null && !refDoc.isEmpty()) {
			EffortsByReference effortsByRef = EffortsByReference.parseDocument(refDoc);
			List<Effort> efforts = effortsByRef.getEfforts();
			if (efforts != null) {
				PrintWriter printer = new PrintWriter(cvsFilePath);
				printer.println("file, commits, modifications, overall effort, has smells");
				for (Effort effort : efforts) {
					boolean hasCodeSmells = (effort.getCodeSmells() != null && !effort.getCodeSmells().isEmpty());
					printer.println(
							"\"" + effort.getFile() + "\", " + effort.getCommits() + ", " + effort.getModifications()
									+ ", " + Math.floor(effort.getModifications() / effort.getCommits()) + ", "
									+ (hasCodeSmells ? "100" : "10"));
				}
				printer.flush();
				printer.close();
			}
		}
	}

}
