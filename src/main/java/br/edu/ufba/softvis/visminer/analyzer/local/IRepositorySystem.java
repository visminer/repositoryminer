package br.edu.ufba.softvis.visminer.analyzer.local;

import java.util.Date;
import java.util.List;

import br.edu.ufba.softvis.visminer.model.CommitDB;
import br.edu.ufba.softvis.visminer.model.CommitterDB;
import br.edu.ufba.softvis.visminer.model.TreeDB;

public interface IRepositorySystem {

	public void open(String repositoryPath);

	public String getAbsolutePath();

	public List<CommitterDB> getCommitters();

	public List<TreeDB> getTrees();

	public List<String> getCommitsNames(String treeName);

	public Date getLastCommitDate(String treeName);

	public List<CommitDB> getCommits();

	public List<CommitDB> getCommits(CommitterDB committer);
	
	public List<CommitDB> getCommits(String treeName);

	//public AST getAST(String commitName, String filePath);

	//public Map<File, FileState> getCommitedFiles(String commitName);

	public void close();
	
}