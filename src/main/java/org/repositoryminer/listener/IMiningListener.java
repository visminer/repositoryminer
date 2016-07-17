package org.repositoryminer.listener;

import java.util.List;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.codesmell.commit.ICommitCodeSmell;
import org.repositoryminer.codesmell.tag.ITagCodeSmell;
import org.repositoryminer.metric.ICommitMetric;
import org.repositoryminer.parser.Parser;
import org.repositoryminer.persistence.model.CommitDB;
import org.repositoryminer.persistence.model.ReferenceDB;
import org.repositoryminer.persistence.model.RepositoryDB;
import org.repositoryminer.persistence.model.WorkingDirectoryDB;
import org.repositoryminer.technicaldebt.ITechnicalDebt;

public interface IMiningListener {
	
	public void updateReference(ReferenceDB reference);
	
	public void updateCommit(CommitDB commit);
	
	public void updateWorkingDirectory(WorkingDirectoryDB workingDirectory);
	
	public void updateRepository(RepositoryDB repository);
	
	public void initCommitProcessing(String repositoryId, CommitDB commit, AST ast, String file, String hash);
	
	public void initTypeProcessing(AbstractTypeDeclaration type, String file);
	
	public void updateAllMetrics(AbstractTypeDeclaration type, AST ast, List<ICommitMetric> metrics);
	
	public void updateAllCommitCodeSmells(AbstractTypeDeclaration type, AST ast, List<ICommitCodeSmell> codeSmells);

	public void updateAllTechnicalDebts(AST ast, AbstractTypeDeclaration type, List<ITechnicalDebt> technicalDebts);
	
	public void endOfTypeProcessing();
	
	public void endOfCommitProcessing();

	public void initTagProcessing(String repositoryId, CommitDB commit, ReferenceDB tag);
	
	public void updateAllTagCodeSmells(List<Parser> parsers, String repositoryPath, List<ITagCodeSmell> codeSmells);

	public void endOfTagProcessing();
}
