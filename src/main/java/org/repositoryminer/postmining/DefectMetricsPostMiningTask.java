package org.repositoryminer.postmining;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.listener.postmining.IPostMiningListener;
import org.repositoryminer.mining.RepositoryMiner;
import org.repositoryminer.model.Commit;
import org.repositoryminer.model.Diff;
import org.repositoryminer.model.Reference;
import org.repositoryminer.model.Repository;
import org.repositoryminer.persistence.handler.CommitDocumentHandler;
import org.repositoryminer.persistence.handler.DefectMetricsDocumentHandler;
import org.repositoryminer.persistence.handler.IssueDocumentHandler;
import org.repositoryminer.persistence.handler.ReferenceDocumentHandler;

import edu.umd.cs.findbugs.anttask.FindBugsViewerTask;


/**
 * <h1>Defect-proneness measurement</h1>
 * <p>
 * To link classes with defects, for a bug that was fixed and closed in a version v, we computed which classes were modified during the fix change. 
 * We thus measure the defect-proneness of a class c in version v using the following three different ways, respectively: 
 * 1. The number of times class c is involved in fixing bugs that were injected in version v, that is, the version where the bugs were found and reported.
 * 2. The number of times class c is involved in fixing bugs that were resolved in version v.
 * 3. The number of times class c is involved in fixing bugs that were alive in version v; 
 *   that is, the bugs were reported before or in version v and were resolved after or in version v.
 */
public class DefectMetricsPostMiningTask implements IPostMiningTask{

	ReferenceDocumentHandler referenceDH = new ReferenceDocumentHandler();
	CommitDocumentHandler commitDH = new CommitDocumentHandler();
	IssueDocumentHandler issueDH = new IssueDocumentHandler();
	
	@Override
	public String getTaskName() {
		return "Defect-proneness measurement";
	}

	@Override
	public void execute(RepositoryMiner miner, Repository repository, IPostMiningListener listener) {
					
		//Define start and end dates for each version
		List<Document> versionList =  referenceDH.findAll();
		List<Reference> tagsList = Reference.parseDocuments(versionList);
		HashMap<String, Date> tags = defineVersionPeriod(versionList);
		List<Document>issuesList =  issueDH.findAll();
		
		//Link closed issues to each version
		HashMap<String, List<Commit>> issuesOpen = new HashMap<String, List<Commit>>();
		HashMap<String, List<Commit>> issuesClosed = new HashMap<String, List<Commit>>();
		HashMap<String, List<Commit>> issuesAlive = new HashMap<String, List<Commit>>();
		
		for (Reference reference : tagsList) {
			issuesOpen.put(reference.getId(), new ArrayList<Commit>());
			issuesClosed.put(reference.getId(), new ArrayList<Commit>());
			issuesAlive.put(reference.getId(), new ArrayList<Commit>());					
		}		
		if (issuesList != null && !issuesList.isEmpty()) {
			for (Document document : issuesList) {					
				Date openDate = document.getDate("created_at");
				Date closeDate = document.getDate("closed_at");
				if (isBug(document)) {
					List<Document> events = (List<Document>) document.get("events");
					for (Document event : events) {
						if (event.getString("commit") != null) {							
							Document commitDoc = commitDH.findById(event.getString("commit"));
							Commit commit = new Commit();
							try {
								if (commitDoc != null)
									commit = Commit.parseDocument(commitDoc);
							} catch (Exception e) {
								System.out.println(e.getStackTrace());
							}
							
							Boolean isOpenBefore = false;
							Boolean isClosedBefore = false;
							for (Reference reference : tagsList) {
								Boolean isClosedSameTag = false;
								if (openDate != null && tags.get(reference.getId()).after(openDate) && !isOpenBefore) {
									issuesOpen.get(reference.getId()).add(commit);
									issuesAlive.get(reference.getId()).add(commit);
									isOpenBefore = true;
									isClosedSameTag = true;
								}
								
								if (closeDate != null && tags.get(reference.getId()).after(closeDate) && !isClosedBefore) {
									issuesClosed.get(reference.getId()).add(commit);
									isClosedBefore = true;
									if (!isClosedSameTag)
										issuesAlive.get(reference.getId()).add(commit);
								}								
							}
						}
					}
				}				
			}			
		}
	
		//Calculate occurrences of classes in commits related with issues
		HashMap<String, HashMap<String, Integer>> classIssuesOpen = new HashMap<String, HashMap<String, Integer>>();
		HashMap<String, HashMap<String, Integer>> classIssuesClosed = new HashMap<String, HashMap<String, Integer>>();
		HashMap<String, HashMap<String, Integer>> classIssuesAlive = new HashMap<String, HashMap<String, Integer>>();		
		for (Reference reference : tagsList) {
			classIssuesOpen.put(reference.getId(), calculateClassOccurrences(issuesOpen.get(reference.getId())));
			classIssuesClosed.put(reference.getId(), calculateClassOccurrences(issuesClosed.get(reference.getId())));
			classIssuesAlive.put(reference.getId(),calculateClassOccurrences(issuesAlive.get(reference.getId())));			
		}
		
		//Save metrics
		DefectMetricsDocumentHandler dmHandler = new DefectMetricsDocumentHandler();
		for (Reference reference : tagsList) {
			Document newDocument = new Document("version",reference.getName());
			newDocument.append("issues_open", prepareDocument(classIssuesOpen.get(reference.getId())));			
			newDocument.append("issues_closed", prepareDocument(classIssuesClosed.get(reference.getId())));			
			newDocument.append("issues_alive", prepareDocument(classIssuesAlive.get(reference.getId())));
			dmHandler.insert(newDocument);			
		}		
	}
	
	private List<Document> prepareDocument(HashMap<String, Integer> classList) {
		List<Document> iOpen = new ArrayList<Document>();
		
		for (String c : classList.keySet()) {
			Document doc = new Document("file", c).append("count", classList.get(c));
			iOpen.add(doc);
		}
		return iOpen;
	}

	private HashMap<String, Integer> calculateClassOccurrences(List<Commit> commits) {
		HashMap<String, Integer> classOccurrences = new HashMap<String, Integer>();
		if (commits.size() > 0) {
			for (Commit commit : commits) {
				List<Diff> diffs = commit.getDiffs();
				if (diffs != null && !diffs.isEmpty()) {
					for (Diff diff : diffs) {
						String className = diff.getPath().split("/")[diff.getPath().split("/").length-1];
						if (classOccurrences.get(className) == null) {
							classOccurrences.put(className,1);
							continue;
						}
						classOccurrences.put(className,classOccurrences.get(className) + 1 );
					}
				}
			}
		}
		return classOccurrences;
	}
	
	private HashMap<String, Date> defineVersionPeriod(List<Document> versionList) {
		HashMap<String, Date> tags = new HashMap<String, Date>();
		List<Reference> tagsList = new ArrayList<Reference>();
		if (versionList != null && !versionList.isEmpty()) {
			tagsList = Reference.parseDocuments(versionList);
			if (tagsList != null && !tagsList.isEmpty()) {
				for (Reference reference : tagsList) {					
					List<String> listaCommits = reference.getCommits();
					for (String commit : listaCommits) {
						Document commitDoc = commitDH.findById(commit);
						tags.put(reference.getId(), Commit.parseDocument(commitDoc).getCommitDate());						
						break;
					}
				}				
			}
		}
		return tags;
	}
	
	private boolean isBug(Document issue) {
		List<Document> labels = (List<Document>) issue.get("labels");		
		boolean isBug = false;
		if (labels != null && !labels.isEmpty()) {
			for (Document label : labels) {
				if (label.getString("name").equals("bug"))
					isBug = true;							
			}
		}
		return isBug;
	}
	
}
