package org.repositoryminer.persistence.handler.effort;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.model.effort.Effort;
import org.repositoryminer.model.effort.EffortCategoriesByReference;
import org.repositoryminer.model.effort.EffortCategory;
import org.repositoryminer.persistence.Connection;
import org.repositoryminer.persistence.handler.DocumentHandler;

import com.mongodb.BasicDBObject;

public class EffortCategoriesDocumentHandler extends DocumentHandler {

	private static final String COLLECTION_NAME = "effort_categories_by_reference";
	
	public EffortCategoriesDocumentHandler() {
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
			EffortCategoriesByReference effortCatsByRef = EffortCategoriesByReference.parseDocument(refDoc);
			List<EffortCategory> categories = effortCatsByRef.getCategories();
			if (categories != null) {
				PrintWriter printer = new PrintWriter(cvsFilePath);
				printer.println("category, commits, modifications, overall effort, has smells");
				for (EffortCategory category : categories) {
					String row = "\"" + category.getCategory() + "\", ";
					int commits = 0, modifications = 0;
					boolean hasCodeSmells = false;
					for (Effort effort : category.getEfforts()) {
						commits += effort.getCommits();
						modifications += effort.getModifications();
						hasCodeSmells = hasCodeSmells || (effort.getCodeSmells() != null && !effort.getCodeSmells().isEmpty());
					}
					double oeffort = Math.floor(modifications / commits);
					
					row += commits + ", " + modifications + ", " + oeffort + ", " + (hasCodeSmells ? "100" : "10");
					
					printer.println(row);
				}
				printer.flush();
				printer.close();
			}
		}
	}

}
