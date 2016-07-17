package org.repositoryminer.mining;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.repositoryminer.ast.AST;
import org.repositoryminer.ast.AbstractTypeDeclaration;
import org.repositoryminer.listener.IMiningListener;
import org.repositoryminer.listener.IProgressListener;
import org.repositoryminer.parser.Parser;
import org.repositoryminer.persistence.model.CommitDB;
import org.repositoryminer.persistence.model.DiffDB;
import org.repositoryminer.persistence.model.ReferenceDB;
import org.repositoryminer.scm.SCM;

public class SourceAnalyzer {

	private SCM scm;
	private RepositoryMiner repository;
	private List<Parser> parsers;
	private String repositoryId;
	private String repositoryPath;

	private IMiningListener miningListener;
	private IProgressListener progressListener;

	private boolean commitMetrics;
	private boolean commitTechnicalDebts;
	private boolean commitCodeSmells;
	private boolean tagCodeSmells;
	private List<CommitDB> commits;
	private List<ReferenceDB> tags;

	private Parser parser = null;
	
	public SourceAnalyzer(RepositoryMiner repository, 
			IMiningListener miningListener, IProgressListener progressListener,
			SCM scm, String repositoryId, String repositoryPath) {
		this.scm = scm;
		this.miningListener = miningListener;
		this.progressListener = progressListener;
		
		this.repository = repository;
		this.repositoryId = repositoryId;
		this.repositoryPath = repositoryPath;
		this.parsers = repository.getParsers();
		
		for (Parser parser : repository.getParsers()) {
			parser.setCharSet(repository.getCharset());
		}
	}

	public void analyze() throws UnsupportedEncodingException {
		if (commitCodeSmells || commitMetrics || commitTechnicalDebts) {
			analyzeCommits();
		}

		if (tagCodeSmells) {
			analyzeTags();
		}
	}

	private void analyzeTags() {
		for (ReferenceDB tag : tags) {
			String commitId = tag.getCommits().get(0);
			scm.checkout(commitId);
			
			for (Parser parser : repository.getParsers()) {
				parser.processSourceFolders(repositoryPath);
			}
			
			int index = commits.indexOf(new CommitDB(commitId));
			CommitDB commit = commits.get(index);
			processTag(commit, tag);
			
			scm.reset();
		}
	}

	private void analyzeCommits() throws UnsupportedEncodingException {
		int idx = 0;
		for (CommitDB commit : commits) {
			progressListener.commitProgressChange(++idx, commits.size());
			
			scm.checkout(commit.getId());

			for (Parser parser : repository.getParsers()) {
				parser.processSourceFolders(repositoryPath);
			}
			
			for (DiffDB diff : commit.getDiffs()) {
				processAST(diff.getPath(), diff.getHash(), commit);
			}

			scm.reset();
		}
	}

	private void processAST(String file, String fileHash, CommitDB commit) throws UnsupportedEncodingException {
		int index = file.lastIndexOf(".") + 1;
		String ext = file.substring(index);

		if (parser == null || !parser.getExtensions().contains(ext)) {
			for (Parser p : parsers) {
				if (p.getExtensions().contains(ext)) {
					parser = p;
				}
			}
		}
		
		if (parser == null) {
			return;
		}

		byte[] data = scm.getData(commit.getId(), file.replaceFirst(repositoryPath + "/", ""));
		if (data == null) {
			return;
		}

		String source = new String(data, repository.getCharset());
		AST ast = parser.generate(file, source);
		processCommit(commit, file, fileHash, ast);
	}

	private void processTag(CommitDB commit, ReferenceDB tag) {
		miningListener.initTagProcessing(repositoryId, commit, tag);
		processTagCodeSmells();
		miningListener.endOfTagProcessing();
	}
	
	private void processTagCodeSmells() {
		if (tagCodeSmells) {
			miningListener.updateAllTagCodeSmells(parsers, repositoryPath, repository.getTagCodeSmells());
		}
	}
	
	private void processCommit(CommitDB commit, String file, String hash, AST ast) {
		miningListener.initCommitProcessing(repositoryId, commit, ast, file, hash);

		List<AbstractTypeDeclaration> types = ast.getDocument().getTypes();
		for (AbstractTypeDeclaration type : types) {
			miningListener.initTypeProcessing(type, file);

			processCommitMetrics(ast, type);
			processCommitCodeSmells(ast, type);
			processTechnicalDebts(ast, type);

			miningListener.endOfTypeProcessing();
		}
		
		miningListener.endOfCommitProcessing();
	}

	private void processCommitMetrics(AST ast, AbstractTypeDeclaration type) {
		if (repository.getCommitMetrics() != null) {
			miningListener.updateAllMetrics(type, ast, repository.getCommitMetrics());
		}
	}

	private void processCommitCodeSmells(AST ast, AbstractTypeDeclaration type) {
		if (repository.getCommitCodeSmells() != null) {
			miningListener.updateAllCommitCodeSmells(type, ast, repository.getCommitCodeSmells());
		}
	}

	/** FIXME: Needs revision **/
	private void processTechnicalDebts(AST ast, AbstractTypeDeclaration type) {
		if (repository.getTechnicalDebts() != null) {
			miningListener.updateAllTechnicalDebts(ast, type, repository.getTechnicalDebts());
		}
	}

	public void setCommitMetrics(boolean commitMetrics) {
		this.commitMetrics = commitMetrics;
	}

	public void setCommitTechnicalDebts(boolean commitTechnicalDebts) {
		this.commitTechnicalDebts = commitTechnicalDebts;
	}

	public void setCommitCodeSmells(boolean commitCodeSmells) {
		this.commitCodeSmells = commitCodeSmells;
	}

	public void setTagCodeSmells(boolean tagCodeSmells) {
		this.tagCodeSmells = tagCodeSmells;
	}

	public void setCommits(List<CommitDB> commits) {
		this.commits = commits;
	}

	public void setTags(List<ReferenceDB> tags) {
		this.tags = tags;
	}

}