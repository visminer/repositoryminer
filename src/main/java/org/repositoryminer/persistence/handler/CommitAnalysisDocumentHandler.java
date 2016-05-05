package org.repositoryminer.persistence.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.persistence.Connection;

import com.mongodb.BasicDBObject;

public class CommitAnalysisDocumentHandler extends DocumentHandler{

	private static final String COLLECTION_NAME = "commit_analysis";
	
	public CommitAnalysisDocumentHandler(){
		super.collection = Connection.getInstance().getCollection(COLLECTION_NAME);
	}
	
	public Document getOneClassById(String fileHash, String idCommit) {
		BasicDBObject andQuery = new BasicDBObject();
		List<BasicDBObject> conditions = new ArrayList<BasicDBObject>();
		
		conditions.add(new BasicDBObject("commit", idCommit));
		conditions.add(new BasicDBObject("file_hash", fileHash));
		conditions.add(new BasicDBObject("abstract_types.declaration", "CLASS_OR_INTERFACE"));
		andQuery.put("$and", conditions);
		
	    return findOne(andQuery, null);
	}
	
	@SuppressWarnings("unchecked")
	public List<Document> getCodeSmells(String fileHash, String idCommit) {
		Document commitAnalysis = getOneClassById(fileHash, idCommit);
		Document type = getType(commitAnalysis);
		Object codeSmells = type.get("codesmells");
		if (codeSmells instanceof ArrayList<?>) {
			return (ArrayList<Document>) codeSmells;
		}
		return Collections.emptyList();
	}
	
	@SuppressWarnings("unchecked")
	private Document getType(Document commitAnalysis) {
		ArrayList<Document> abstractTypes = (ArrayList<Document>) commitAnalysis.get("abstract_types");
		return abstractTypes.get(0);
	}
	
	public List<Document> getAllByCommit(String idCommit) {
		BasicDBObject whereClause = new BasicDBObject();
		whereClause.put("commit", idCommit);
		
		return findMany(whereClause, null);
	}
	
	@SuppressWarnings("unchecked")
	public List<Document> getAllByTree(String idTree) {
		ReferenceDocumentHandler referenceHandler = new ReferenceDocumentHandler();
		Document tree = referenceHandler.findById(idTree, null);
		Object commits = tree.get("commits");
		if (commits instanceof ArrayList<?>) {
			ArrayList<String> commitsList = ((ArrayList<String>) commits);
			HashMap<String, Document> types = new HashMap<String, Document>();
			for (String commit : commitsList) {
				List<Document> typesList = getAllByCommit(commit);
				for (Document type : typesList) {
					if (!types.containsKey(type.getString("file_hash"))) {
						types.put(type.getString("file_hash"), type);
					}
				}
			}
			return new ArrayList<Document>(types.values());
		}
		return Collections.emptyList();
	}
		
	public void updateDesignDebtStatus(String fileHash, String idCommit, int status) {
		updateDebtStatus(fileHash, idCommit, status, 0);
	}
	
	public void updateCodeDebtStatus(String fileHash, String idCommit, int status) {
		updateDebtStatus(fileHash, idCommit, status, 1);
	}
	
	public void updateAllDebtsStatus(String fileHash, String idCommit, int status) {
		BasicDBObject andQuery = new BasicDBObject();
		List<BasicDBObject> conditions = new ArrayList<BasicDBObject>();
		
		conditions.add(new BasicDBObject("commit", idCommit));
		conditions.add(new BasicDBObject("file_hash", fileHash));
		andQuery.put("$and", conditions);
		
		BasicDBObject updateQuery = new BasicDBObject();
		updateQuery.append("$set", new BasicDBObject()
				.append("abstract_types.0.technicaldebts.0.status", status)
				.append("abstract_types.0.technicaldebts.1.status", status));
				
		updateOne(andQuery, updateQuery);
	}
	
	private void updateDebtStatus(String fileHash, String idCommit, int status, int position) {
		BasicDBObject andQuery = new BasicDBObject();
		List<BasicDBObject> conditions = new ArrayList<BasicDBObject>();
		
		conditions.add(new BasicDBObject("commit", idCommit));
		conditions.add(new BasicDBObject("file_hash", fileHash));
		andQuery.put("$and", conditions);
		
		BasicDBObject updateQuery = new BasicDBObject();
		updateQuery.append("$set", new BasicDBObject().append("abstract_types.0.technicaldebts."+position+".status", status));
				
		updateOne(andQuery, updateQuery);
	}
		
	public void confirmAllDebtsByCommit(String idCommit) {	
		confirmAllDesignDebtsByCommit(idCommit);
		confirmAllCodeDebtsByCommit(idCommit);
	}
	
	private void confirmAllDesignDebtsByCommit(String idCommit) {
		BasicDBObject updateQuery = new BasicDBObject();
		updateQuery.append("$set", new BasicDBObject().append("abstract_types.0.technicaldebts.0.status", 1));
		
		updateMany(new BasicDBObject().append("commit", idCommit).append("abstract_types.0.technicaldebts.0.value", true), updateQuery);
	}
	
	private void confirmAllCodeDebtsByCommit(String idCommit) {
		BasicDBObject updateQuery = new BasicDBObject();
		updateQuery.append("$set", new BasicDBObject().append("abstract_types.0.technicaldebts.1.status", 1));
		
		updateMany(new BasicDBObject().append("commit", idCommit).append("abstract_types.0.technicaldebts.1.value", true), updateQuery);
	}
	
	@SuppressWarnings("unchecked")
	public void confirmAllDebtsByTag(String idTree) {
		ReferenceDocumentHandler treeHandler = new ReferenceDocumentHandler();
		Document tree = treeHandler.findById(idTree, null);
		
		List<String> commits = (List<String>) tree.get("commits"); 
		for (String commit : commits) {			
			confirmAllDebtsByCommit(commit);
		}
	}
	
	public void confirmAllDebtsByRepository(String idRepository) {
		ReferenceDocumentHandler treeHandler = new ReferenceDocumentHandler();
		List<Document> trees = treeHandler.getTagsByRepository(idRepository);
		
		for (Document tree : trees) {
			confirmAllDebtsByTag(tree.getString("_id"));
		}
	}		
}