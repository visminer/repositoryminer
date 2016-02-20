package br.edu.ufba.softvis.visminer.analyzer.scm;

import java.io.IOException;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand.ResetType;

import br.edu.ufba.softvis.visminer.constant.TreeType;
import br.edu.ufba.softvis.visminer.error.VisMinerAPIException;
import br.edu.ufba.softvis.visminer.model.Commit;
import br.edu.ufba.softvis.visminer.model.File;
import br.edu.ufba.softvis.visminer.model.Tree;

public class GitRepository implements SCM {

	private JGitRepository jgit;
	private Git git;
	private String vmBranch;

	@Override
	public void open(String path) {
		this.jgit = new JGitRepository();
		jgit.open(path);
		git = jgit.getGit();
		vmBranch = "vm";
	}

	@Override
	public String getAbsolutePath() {
		return jgit.getAbsolutePath();
	}

	@Override
	public List<Tree> getTrees() {
		return jgit.getTrees();
	}

	@Override
	public List<Commit> getCommits() {
		return jgit.getCommits();
	}

	@Override
	public List<Commit> getCommitsByTree(String treeName, TreeType type) {
		return jgit.getCommitsByTree(treeName, type);
	}

	@Override
	public String getSource(String commitName, String filePath) {
		return jgit.getSource(commitName, filePath);
	}

	@Override
	public List<File> getCommitedFiles(Commit commitDB) {
		return jgit.getCommitedFiles(commitDB);
	}

	@Override
	public List<String> getRepositoryFiles(String hash) {
		return jgit.getRepositoryFiles(hash);
	}

	@Override
	public void checkout(String hash) {
		makeCheckout("master", false);
		makeCheckout(hash, true);
	}

	public void close() {
		jgit.close();
	}

	public void reset() {

		try {

			if (!git.status().call().isClean())
				git.reset().setMode(ResetType.HARD).call();

			makeCheckout("master", false);
			git.branchDelete().setBranchNames(vmBranch).setForce(true).call();

		} catch (Exception e) {
			clean(e);
		}

	}

	private void clean(Throwable e) {
		close();
		// TODO tratar no altonivel (segue ou para?)
		e.printStackTrace();
	}

	private void makeCheckout(String hash, boolean create) {
		try {
			if (create)
				Runtime.getRuntime().exec("git checkout -f -b vm " + hash,
						null, new java.io.File(getAbsolutePath()));
			else
				Runtime.getRuntime().exec("git checkout -f " + hash, null,
						new java.io.File(getAbsolutePath()));

		} catch (IOException e) {
			throw new VisMinerAPIException(e.getMessage(), e);
		}
	}

}
