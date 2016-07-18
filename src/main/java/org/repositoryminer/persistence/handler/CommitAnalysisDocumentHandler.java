package org.repositoryminer.persistence.handler;

import com.mongodb.BasicDBObject;
import org.bson.Document;
import org.repositoryminer.persistence.Connection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class CommitAnalysisDocumentHandler extends DocumentHandler {

	private static final String COLLECTION_NAME = "commits_analysis";

	public CommitAnalysisDocumentHandler() {
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

	public Document getAllMeasures(String fileHash, String commitHash) {
		List<BasicDBObject> conditions = new ArrayList<BasicDBObject>();
		conditions.add(new BasicDBObject("file_hash", fileHash));
		conditions.add(new BasicDBObject("commit", commitHash));
		return findOne(new BasicDBObject("$and", conditions), null);
	}
	
	public Document getCodeSmellsMeasures(String fileHash, String commitHash) {
		BasicDBObject fields = new BasicDBObject();
		fields.put("abstract_types.metrics", 0);
		fields.put("abstract_types.technicaldebts", 0);
		
		List<BasicDBObject> conditions = new ArrayList<BasicDBObject>();
		conditions.add(new BasicDBObject("file_hash", fileHash));
		conditions.add(new BasicDBObject("commit", commitHash));
		return findOne(new BasicDBObject("$and", conditions), fields);
	}
	
	public Document getMetricsMeasures(String fileHash, String commitHash) {
		BasicDBObject fields = new BasicDBObject();
		fields.put("abstract_types.codesmells", 0);
		fields.put("abstract_types.technicaldebts", 0);
		
		List<BasicDBObject> conditions = new ArrayList<BasicDBObject>();
		conditions.add(new BasicDBObject("file_hash", fileHash));
		conditions.add(new BasicDBObject("commit", commitHash));
		return findOne(new BasicDBObject("$and", conditions), fields);
	}
	
	public Document getTechnicalDebtsMeasures(String fileHash, String commitHash) {
		BasicDBObject fields = new BasicDBObject();
		fields.put("abstract_types.metrics", 0);
		fields.put("abstract_types.codesmells", 0);
		
		List<BasicDBObject> conditions = new ArrayList<BasicDBObject>();
		conditions.add(new BasicDBObject("file_hash", fileHash));
		conditions.add(new BasicDBObject("commit", commitHash));
		return findOne(new BasicDBObject("$and", conditions), fields);
	}
	
	public List<Document> getAllByCommit(String idCommit) {
		BasicDBObject whereClause = new BasicDBObject();
		whereClause.put("commit", idCommit);
		return findMany(whereClause, null);
	}

	@SuppressWarnings("unchecked")
	public List<Document> getAllByReference(String idReference) {
		ReferenceDocumentHandler referenceHandler = new ReferenceDocumentHandler();
		List<Document> types = new ArrayList<>();
		Document tree = referenceHandler.findById(idReference, null);
		Object commits = tree.get("commits");

		if (commits instanceof ArrayList<?>) {
			ArrayList<String> commitIdsList = ((ArrayList<String>) commits);
			LinkedHashMap<String, String> typesHash = getTypesHashMap(commitIdsList);
			final Set<String> filesHash = typesHash.keySet();
			for (String fileHash : filesHash) {
				final Document type = getOneClassById(fileHash, typesHash.get(fileHash));
				if (type != null)
					types.add(type);
			}
			return types;
		}

		return Collections.emptyList();
	}

	private LinkedHashMap<String, String> getTypesHashMap(List<String> commitsId) {
		CommitDocumentHandler commitDocumentHandler = new CommitDocumentHandler();
		LinkedHashMap<String, String> typesHash = new LinkedHashMap<>();
		for (int i = commitsId.size() - 1; i >= 0; i--) {
			String commitId = commitsId.get(i);
			final List<Document> commitDiffs = commitDocumentHandler.getAllDiffs(commitId);
			for (Document diff : commitDiffs) {
				final String fileHash = diff.getString("hash");
				switch (diff.getString("type")) {
				case "DELETE":
					typesHash.remove(fileHash);
					break;
				case "RENAME":
					typesHash.put(fileHash, commitId);
					typesHash.remove(getFileHashByPath(diff.getString("old_path")));
					break;
				default:
					typesHash.put(fileHash, commitId);
				}
			}
		}
		return typesHash;
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
		updateQuery.append("$set", new BasicDBObject().append("abstract_types.0.technicaldebts.0.status", status)
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
		updateQuery.append("$set",
				new BasicDBObject().append("abstract_types.0.technicaldebts." + position + ".status", status));

		updateOne(andQuery, updateQuery);
	}

	public void confirmAllDebtsByCommit(String idCommit) {
		confirmAllDesignDebtsByCommit(idCommit);
		confirmAllCodeDebtsByCommit(idCommit);
	}

	private void confirmAllDesignDebtsByCommit(String idCommit) {
		BasicDBObject updateQuery = new BasicDBObject();
		updateQuery.append("$set", new BasicDBObject().append("abstract_types.0.technicaldebts.0.status", 1));

		updateMany(
				new BasicDBObject().append("commit", idCommit).append("abstract_types.0.technicaldebts.0.value", true),
				updateQuery);
	}

	private void confirmAllCodeDebtsByCommit(String idCommit) {
		BasicDBObject updateQuery = new BasicDBObject();
		updateQuery.append("$set", new BasicDBObject().append("abstract_types.0.technicaldebts.1.status", 1));

		updateMany(
				new BasicDBObject().append("commit", idCommit).append("abstract_types.0.technicaldebts.1.value", true),
				updateQuery);
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
		List<Document> trees = treeHandler.getTagsAndMasterByRepository(idRepository);

		for (Document tree : trees) {
			confirmAllDebtsByTag(tree.getString("_id"));
		}
	}

	@SuppressWarnings("unchecked")
	public HashMap<Document, Document> getTypeTimeline(String idRepository, String fileHash) {
		ReferenceDocumentHandler referenceHandler = new ReferenceDocumentHandler();
		List<Document> tags = referenceHandler.getTagsAndMasterByRepository(idRepository);
		LinkedHashMap<Document, Document> timeline = new LinkedHashMap<Document, Document>();

		for (Document tag : tags) {
			for (String commitId : (ArrayList<String>) tag.get("commits")) {
				Document type = getOneClassById(fileHash, commitId);
				if (type != null) {
					timeline.put(tag, type);
					break;
				}
			}
		}

		return timeline;
	}

	public String getFileHashByPath(String path) {
		BasicDBObject whereClause = new BasicDBObject();
		whereClause.put("file", path);
		final Document file = findOne(whereClause, null);
		if (file == null) {
			return null;
		}

		return file.getString("file_hash");
	}
}								