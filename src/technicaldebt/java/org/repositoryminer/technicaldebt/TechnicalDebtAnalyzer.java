package org.repositoryminer.technicaldebt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.repositoryminer.model.Commit;
import org.repositoryminer.model.Contributor;
import org.repositoryminer.model.Reference;
import org.repositoryminer.model.Repository;
import org.repositoryminer.persistence.handler.CommitDocumentHandler;
import org.repositoryminer.persistence.handler.ReferenceDocumentHandler;
import org.repositoryminer.persistence.handler.RepositoryDocumentHandler;
import org.repositoryminer.persistence.handler.TechnicalCodeDebtDocumentHandler;
import org.repositoryminer.persistence.handler.WorkingTreeDocumentHandler;
import org.repositoryminer.scm.ISCM;
import org.repositoryminer.scm.ReferenceType;
import org.repositoryminer.scm.SCMFactory;
import org.repositoryminer.utility.StringUtils;

import com.mongodb.client.model.Projections;

public class TechnicalDebtAnalyzer {

	private static final Map<TechnicalDebtId, List<TechnicalDebtIndicator>> debts = new HashMap<TechnicalDebtId, List<TechnicalDebtIndicator>>();
	
	// Initializes the types of debts with theirs respectives indicators.
	static {
		final List<TechnicalDebtIndicator> codeDebt = new ArrayList<TechnicalDebtIndicator>(10);
		final List<TechnicalDebtIndicator> desingDebt = new ArrayList<TechnicalDebtIndicator>(8);
		
		codeDebt.add(TechnicalDebtIndicator.AUTOMATIC_STATIC_ANALYSIS_ISSUES);
		codeDebt.add(TechnicalDebtIndicator.GOD_CLASS);
		codeDebt.add(TechnicalDebtIndicator.COMPLEX_METHOD);
		codeDebt.add(TechnicalDebtIndicator.DUPLICATED_CODE);
		codeDebt.add(TechnicalDebtIndicator.DATA_CLASS);
		codeDebt.add(TechnicalDebtIndicator.BRAIN_METHOD);
		codeDebt.add(TechnicalDebtIndicator.REFUSED_PARENT_BEQUEST);
		
		desingDebt.addAll(codeDebt);
		
		codeDebt.add(TechnicalDebtIndicator.CODE_WITHOUT_STANDARDS);
		codeDebt.add(TechnicalDebtIndicator.SLOW_ALGORITHM);
		codeDebt.add(TechnicalDebtIndicator.MULTITHREAD_CORRECTNESS);
		
		desingDebt.add(TechnicalDebtIndicator.DEPTH_OF_INHERITANCE_TREE);
		
		debts.put(TechnicalDebtId.CODE_DEBT, codeDebt);
		debts.put(TechnicalDebtId.DESIGN_DEBT, desingDebt);
	}
	
	private final ReferenceDocumentHandler refPersist = new ReferenceDocumentHandler();
	private final CommitDocumentHandler commitPersist = new CommitDocumentHandler();
	private final WorkingTreeDocumentHandler wtHandler = new WorkingTreeDocumentHandler();
	private final TechnicalCodeDebtDocumentHandler codeTDHandler = new TechnicalCodeDebtDocumentHandler();

	private final CodeIndicatorsAnalyzer indicatorsAnalyzer = new CodeIndicatorsAnalyzer();
	
	private final ISCM scm;
	private final Repository repository;

	public TechnicalDebtAnalyzer(Repository repository) {
		this.repository = repository;
		scm = SCMFactory.getSCM(repository.getScm());
	}
	
	public TechnicalDebtAnalyzer(final String repositoryId) {
		final RepositoryDocumentHandler  handler = new RepositoryDocumentHandler();
		repository = Repository.parseDocument(handler.findById(repositoryId, Projections.include("scm", "path")));
		scm = SCMFactory.getSCM(repository.getScm());
	}

	public void execute(final String hash) {
		persistAnalysis(hash, null);
	}

	public void execute(final String name, final ReferenceType type) {
		final Document refDoc = refPersist.findByNameAndType(name, type, repository.getId(), Projections.slice("commits", 1));
		final Reference reference = Reference.parseDocument(refDoc);

		final String commitId = reference.getCommits().get(0);
		persistAnalysis(commitId, reference);
	}

	@SuppressWarnings("unchecked")
	private void persistAnalysis(final String commitId, final Reference ref) {
		final Commit commit = Commit.parseDocument(commitPersist.findById(commitId, Projections.include("commit_date")));
		final Document wd = wtHandler.findById(commit.getId());

		scm.open(repository.getPath());
		
		final List<Document> documents = new ArrayList<Document>();
		for (Document file : (List<Document>) wd.get("files")) {
			final Map<TechnicalDebtIndicator, Integer> indicators = indicatorsAnalyzer.detect(file.getString("file"),
					file.getString("checkout"), commit.getId());

			if (indicators.isEmpty()) {
				continue;
			}

			final Document doc = new Document();
			
			if (ref != null) {
				doc.append("reference_name", ref.getName());
				doc.append("reference_type", ref.getType().toString());
			}
			
			doc.append("commit", commit.getId());
			doc.append("commit_date", commit.getCommitDate());
			doc.append("repository", new ObjectId(repository.getId()));
			doc.append("filename", file.getString("file"));
			doc.append("filestate", file.getString("checkout"));
			doc.append("filehash", StringUtils.encodeToCRC32(file.getString("file")));
			doc.append("technical_debt", false);
			
			final List<Document> contribsDoc = new ArrayList<Document>();
			for (Contributor c : scm.getCommitters(file.getString("file"), commitId)) {
				contribsDoc.add(c.toDocument().append("colaborator", c.isCollaborator()));
			}
			doc.append("contributors", contribsDoc);
			
			final Document indicatorsDoc = new Document();
			for (Entry<TechnicalDebtIndicator, Integer> entry : indicators.entrySet()) {
				indicatorsDoc.append(entry.getKey().toString(), entry.getValue());
			}
 			
			final List<String> debtsNames = new ArrayList<String>();
			for (Entry<TechnicalDebtId, List<TechnicalDebtIndicator>> debtEntry : debts.entrySet()) {
				for (TechnicalDebtIndicator indicator : indicators.keySet()) {
					if (debtEntry.getValue().contains(indicator)) {
						debtsNames.add(debtEntry.getKey().toString());
						break;
					}
				}
			}
			
			doc.append("indicators", indicatorsDoc);
			doc.append("debts", debtsNames);

			documents.add(doc);
		}

		scm.close();
		codeTDHandler.insertMany(documents);
	}

}