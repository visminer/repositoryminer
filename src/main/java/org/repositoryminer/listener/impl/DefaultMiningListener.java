package org.repositoryminer.listener.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.Document;
import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.codesmell.commit.ICommitCodeSmell;
import org.repositoryminer.codesmell.tag.DuplicatedCode;
import org.repositoryminer.codesmell.tag.ITagCodeSmell;
import org.repositoryminer.listener.ICommitCodeSmellDetectionListener;
import org.repositoryminer.listener.IMetricCalculationListener;
import org.repositoryminer.listener.IMiningListener;
import org.repositoryminer.listener.ITechnicalDebtDetectionListener;
import org.repositoryminer.metric.ICommitMetric;
import org.repositoryminer.parser.Parser;
import org.repositoryminer.persistence.handler.CommitAnalysisDocumentHandler;
import org.repositoryminer.persistence.handler.CommitDocumentHandler;
import org.repositoryminer.persistence.handler.ReferenceDocumentHandler;
import org.repositoryminer.persistence.handler.RepositoryDocumentHandler;
import org.repositoryminer.persistence.handler.TagAnalysisDocumentHandler;
import org.repositoryminer.persistence.handler.WorkingDirectoryDocumentHandler;
import org.repositoryminer.persistence.model.CommitDB;
import org.repositoryminer.persistence.model.ReferenceDB;
import org.repositoryminer.persistence.model.RepositoryDB;
import org.repositoryminer.persistence.model.WorkingDirectoryDB;
import org.repositoryminer.technicaldebt.ITechnicalDebt;
import org.repositoryminer.utility.HashHandler;

public class DefaultMiningListener implements IMiningListener {

	private CommitDocumentHandler commitHandler;
	private WorkingDirectoryDocumentHandler wdHandler;
	private RepositoryDocumentHandler repoHandler;
	private ReferenceDocumentHandler refHandler;
	private CommitAnalysisDocumentHandler commAnalysisHandler;
	private TagAnalysisDocumentHandler persistenceTag;

	private Document commitDoc, typeDoc, tagDoc;

	private List<Document> typeDocs;
	private List<Document> smellDocs;

	public DefaultMiningListener() {
		wdHandler = new WorkingDirectoryDocumentHandler();
		commitHandler = new CommitDocumentHandler();
		repoHandler = new RepositoryDocumentHandler();
		refHandler = new ReferenceDocumentHandler();
		commAnalysisHandler = new CommitAnalysisDocumentHandler();

		typeDocs = new ArrayList<Document>();
	}

	@Override
	public void updateRepository(RepositoryDB repository) {
		repoHandler.insert(repository.toDocument());
	}

	@Override
	public void updateReference(ReferenceDB reference) {
		refHandler.insert(reference.toDocument());
	}

	@Override
	public void updateCommit(CommitDB commit) {
		commitHandler.insert(commit.toDocument());
	}

	@Override
	public void updateWorkingDirectory(WorkingDirectoryDB workingDirectory) {
		wdHandler.insert(workingDirectory.toDocument());
	}

	@Override
	public void initCommitProcessing(String repositoryId, CommitDB commit, AST ast, String file, String hash) {
		commitDoc = new Document();
		commitDoc.append("commit", commit.getId());
		commitDoc.append("commit_date", commit.getCommitDate());
		commitDoc.append("package", ast.getDocument().getPackageDeclaration());
		commitDoc.append("file", file);
		commitDoc.append("repository", repositoryId);
		commitDoc.append("file_hash", hash);
	}

	@Override
	public void initTypeProcessing(AbstractTypeDeclaration type, String file) {
		typeDoc = new Document();
		String typeHash = file + "/" + type.getName();
		typeDoc.append("name", type.getName()).append("declaration", type.getArchetype().toString()).append("hash",
				HashHandler.SHA1(typeHash));
	}

	@Override
	public void updateAllMetrics(AbstractTypeDeclaration type, AST ast, List<ICommitMetric> metrics) {
		final List<Document> metricDocs = new ArrayList<Document>();
		for (ICommitMetric metric : metrics) {
			metric.calculate(type, ast, new IMetricCalculationListener() {
				@Override
				public void updateMetricValue(String metricName, int value) {
					Document mDoc = new Document();
					mDoc.append("name", metricName).append("accumulated", new Integer(value));

					metricDocs.add(mDoc);
				}

				@Override
				public void updateMetricValue(String metricName, float value) {
					Document mDoc = new Document();
					mDoc.append("name", metricName).append("accumulated", new Float(value));

					metricDocs.add(mDoc);
				}

				@Override
				public void updateMethodBasedMetricValue(String metricName, int accumulatedValue,
						Map<String, Integer> valuesPerMethod) {
					Document mDoc = new Document();
					mDoc.append("name", metricName).append("accumulated", new Integer(accumulatedValue));
					for (Entry<String, Integer> entry : valuesPerMethod.entrySet()) {
						mDoc.append("method", entry.getKey()).append("value", entry.getValue());
					}

					metricDocs.add(mDoc);
				}
			});
		}

		typeDoc.append("metrics", metricDocs);
	}

	@Override
	public void updateAllCommitCodeSmells(AbstractTypeDeclaration type, AST ast, List<ICommitCodeSmell> codeSmells) {
		smellDocs = new ArrayList<Document>();
		for (ICommitCodeSmell codeSmell : codeSmells) {
			codeSmell.detect(type, ast, new ICommitCodeSmellDetectionListener() {

				@Override
				public void updateSmellDetection(String smellName, boolean detected) {
					Document sDoc = new Document();
					sDoc.append("name", smellName).append("value", new Boolean(detected));

					smellDocs.add(sDoc);
				}

				@Override
				public void updateMethodBasedSmellDetection(String smellName,
						Map<String, Boolean> detectionsPerMethod) {
					List<Document> detections = new ArrayList<Document>();
					for (Map.Entry<String, Boolean> detection : detectionsPerMethod.entrySet()) {
						detections
								.add(new Document("method", detection.getKey()).append("value", detection.getValue()));
					}
					Document sDoc = new Document();
					sDoc.append("name", smellName).append("methods", detections);

					smellDocs.add(sDoc);
				}
			});
		}

		typeDoc.append("codesmells", smellDocs);
	}

	@Override
	public void updateAllTechnicalDebts(AST ast, AbstractTypeDeclaration type, List<ITechnicalDebt> technicalDebts) {
		final List<Document> technicalDebtDocs = new ArrayList<Document>();
		for (ITechnicalDebt techDebt : technicalDebts) {
			techDebt.detect(type, ast, smellDocs, new ITechnicalDebtDetectionListener() {

				@Override
				public void updateDebtDetection(String debtName, boolean detected) {
					Document dDoc = new Document();
					dDoc.append("name", debtName).append("value", new Boolean(detected)).append("status", 0);

					technicalDebtDocs.add(dDoc);
				}
			});
		}

		typeDoc.append("technical_debts", technicalDebtDocs);
	}

	@Override
	public void endOfTypeProcessing() {
		typeDocs.add(typeDoc);
	}

	@Override
	public void endOfCommitProcessing() {
		commitDoc.append("abstract_types", typeDocs);
		commAnalysisHandler.insert(commitDoc);
	}

	@Override
	public void initTagProcessing(String repositoryId, CommitDB commit, ReferenceDB tag) {
		tagDoc = new Document();
		tagDoc.append("tag", tag.getName());
		tagDoc.append("tag_type", tag.getType().toString());
		tagDoc.append("commit", commit.getId());
		tagDoc.append("commit_date", commit.getCommitDate());
		tagDoc.append("repository", repositoryId);
	}

	@Override
	public void updateAllTagCodeSmells(List<Parser> parsers, String repositoryPath, List<ITagCodeSmell> codeSmells) {
		smellDocs = new ArrayList<Document>();
		for (ITagCodeSmell codeSmell : codeSmells) {
			if (codeSmell instanceof DuplicatedCode) {
				codeSmell.detect(parsers, repositoryPath, new DefaultDuplicatedCodeDetectionListener(smellDocs));
			}
		}
	}

	@Override
	public void endOfTagProcessing() {
		tagDoc.append("codesmells", smellDocs);
		persistenceTag.insert(tagDoc);
	}

}
