package org.repositoryminer.technicaldebt;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.repositoryminer.model.Commit;
import org.repositoryminer.model.Reference;
import org.repositoryminer.persistence.handler.CommitDocumentHandler;
import org.repositoryminer.persistence.handler.ReferenceDocumentHandler;
import org.repositoryminer.persistence.handler.TechnicalCodeDebtDocumentHandler;
import org.repositoryminer.persistence.handler.WorkingDirectoryDocumentHandler;
import org.repositoryminer.scm.ReferenceType;
import org.repositoryminer.technicaldebt.code.ITechnicalCodeDebt;
import org.repositoryminer.utility.StringUtils;

import com.mongodb.client.model.Projections;

public class TDAnalyzer {

	private List<ITechnicalCodeDebt> technicalCodeDebts;
	
	private String repositoryId;
	
	private ReferenceDocumentHandler refPersist;
	private CommitDocumentHandler commitPersist;
	private WorkingDirectoryDocumentHandler wdHandler;
	private TechnicalCodeDebtDocumentHandler codeTDHandler;
	
	public TDAnalyzer(String repositoryId) {
		this.repositoryId = repositoryId;
		
		refPersist = new ReferenceDocumentHandler();
		commitPersist = new CommitDocumentHandler();
		wdHandler = new WorkingDirectoryDocumentHandler();
		codeTDHandler = new TechnicalCodeDebtDocumentHandler();
	}

	public void analyzeTD(String hash){
		analyze(hash);
	}

	public void analyzeTD(String name, ReferenceType type) {
		Document refDoc = refPersist.findByNameAndType(name, type, repositoryId, Projections.slice("commits", 1));
		Reference reference = Reference.parseDocument(refDoc);

		String commitId = reference.getCommits().get(0);
		analyze(commitId);
	}

	@SuppressWarnings("unchecked")
	private void analyze(String commitId) {
		Commit commit = Commit.parseDocument(commitPersist.findById(commitId, Projections.include("commit_date")));
		Document wd = wdHandler.findById(commit.getId());
		
		List<Document> documents = new ArrayList<Document>();
		
		for (Document file : (List<Document>) wd.get("files")) {
			List<Document> debts = new ArrayList<Document>();
			for (ITechnicalCodeDebt debtDetectors : getTechnicalCodeDebts()) {
				Document td = debtDetectors.detect(file.getString("file"), file.getString("checkout"), commit.getId());
				if (td != null) {
					debts.add(td);
				}
			}
			
			if (debts.size() == 0) {
				continue;
			}
			
			Document doc = new Document();
			doc.append("commit", commit.getId());
			doc.append("commit_date", commit.getCommitDate());
			doc.append("repository", new ObjectId(repositoryId));
			doc.append("filename", file.getString("file"));
			doc.append("filestate", file.getString("checkout"));
			doc.append("filehash", StringUtils.encodeToCRC32(file.getString("file")));
			doc.append("technical_debts", debts);
			
			documents.add(doc);
		}
		
		codeTDHandler.insertMany(documents);
	}
	
	public boolean addTechnicalCodeDebt(ITechnicalCodeDebt debt) {
		for (ITechnicalCodeDebt debt2 : getTechnicalCodeDebts()) {
			if (debt.getId().equals(debt2.getId())) {
				return false;
			}
		}
		
		technicalCodeDebts.add(debt);
		return true;
	}
	
	public List<ITechnicalCodeDebt> getTechnicalCodeDebts() {
		if (technicalCodeDebts == null) {
			technicalCodeDebts = new ArrayList<ITechnicalCodeDebt>();
		}
		
		return technicalCodeDebts;
	}

	public void setTechnicalCodeDebts(List<ITechnicalCodeDebt> technicalCodeDebts) {
		this.technicalCodeDebts = technicalCodeDebts;
	}
	
}