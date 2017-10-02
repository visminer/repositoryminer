package org.repositoryminer.technicaldebt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.repositoryminer.domain.Commit;
import org.repositoryminer.domain.Reference;
import org.repositoryminer.domain.ReferenceType;
import org.repositoryminer.persistence.dao.CommitDAO;
import org.repositoryminer.persistence.dao.ReferenceDAO;
import org.repositoryminer.technicaldebt.persistence.TechnicalCodeDebtDAO;
import org.repositoryminer.util.HashingUtils;

import com.mongodb.client.model.Projections;

public class TechnicalDebtAnalyzer {

	private static final Map<TechnicalDebtId, List<TechnicalDebtIndicator>> debts = new HashMap<TechnicalDebtId, List<TechnicalDebtIndicator>>();
	
	// Initializes the types of debts with theirs respectives indicators.
	static {
		final List<TechnicalDebtIndicator> codeDebt = new ArrayList<TechnicalDebtIndicator>(9);
		final List<TechnicalDebtIndicator> desingDebt = new ArrayList<TechnicalDebtIndicator>(7);
		
		codeDebt.add(TechnicalDebtIndicator.AUTOMATIC_STATIC_ANALYSIS_ISSUES);
		codeDebt.add(TechnicalDebtIndicator.GOD_CLASS);
		codeDebt.add(TechnicalDebtIndicator.COMPLEX_METHOD);
		codeDebt.add(TechnicalDebtIndicator.DUPLICATED_CODE);
		codeDebt.add(TechnicalDebtIndicator.DATA_CLASS);
		codeDebt.add(TechnicalDebtIndicator.BRAIN_METHOD);
		
		desingDebt.addAll(codeDebt);
		
		codeDebt.add(TechnicalDebtIndicator.CODE_WITHOUT_STANDARDS);
		codeDebt.add(TechnicalDebtIndicator.SLOW_ALGORITHM);
		codeDebt.add(TechnicalDebtIndicator.MULTITHREAD_CORRECTNESS);
		
		debts.put(TechnicalDebtId.CODE_DEBT, codeDebt);
		debts.put(TechnicalDebtId.DESIGN_DEBT, desingDebt);
	}
	
	private final ReferenceDAO refPersist = new ReferenceDAO();
	private final CommitDAO commitPersist = new CommitDAO();
	private final TechnicalCodeDebtDAO codeTDHandler = new TechnicalCodeDebtDAO();

	private final CodeIndicatorsAnalyzer indicatorsAnalyzer = new CodeIndicatorsAnalyzer();
	private final WorkingTreeCreator workingTreeCreator = new WorkingTreeCreator();
	
	private String repositoryId;

	public TechnicalDebtAnalyzer(final String repositoryId) {
		this.repositoryId = repositoryId;
	}

	public void execute(final String hash) {
		persistAnalysis(hash, null);
	}

	public void execute(final String name, final ReferenceType type) {
		final Document refDoc = refPersist.findByNameAndType(name, type, repositoryId, null);
		final Reference reference = Reference.parseDocument(refDoc);
		final String commitId = reference.getCommits().get(0);
		persistAnalysis(commitId, reference);
	}

	private void persistAnalysis(final String commitId, final Reference ref) {
		final Commit commit = Commit.parseDocument(commitPersist.findById(commitId, Projections.include("commit_date")));
		
		Map<String, String> workingTree;
		if (ref != null) {
			Collections.reverse(ref.getCommits());
			workingTree = workingTreeCreator.createFromReference(ref.getCommits());
			ref.setCommits(null);
		} else {
			workingTree = workingTreeCreator.createFromCommit(commitId, repositoryId);
		}
		
		final List<Document> documents = new ArrayList<Document>();
		for (Entry<String, String> file : workingTree.entrySet()) {
			final Map<TechnicalDebtIndicator, Integer> indicators = indicatorsAnalyzer.detect(file.getKey(),
					file.getValue(), commit.getId());

			if (indicators.isEmpty()) {
				continue;
			}

			final Document doc = new Document();
			
			if (ref != null) {
				doc.append("reference", ref.getPath());
			}
			
			doc.append("commit", commit.getId());
			doc.append("commit_date", commit.getCommitDate());
			doc.append("repository", new ObjectId(repositoryId));
			doc.append("filename", file.getKey());
			doc.append("filestate", file.getValue());
			doc.append("filehash", HashingUtils.encodeToCRC32(file.getKey()));
			doc.append("technical_debt", false);
			
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

		codeTDHandler.insertMany(documents);
	}

}