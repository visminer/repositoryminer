package br.edu.ufba.softvis.visminer.analyzer.scm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.eclipse.jgit.util.io.DisabledOutputStream;

import br.edu.ufba.softvis.visminer.constant.ChangeType;
import br.edu.ufba.softvis.visminer.constant.TreeType;
import br.edu.ufba.softvis.visminer.error.VisMinerAPIException;
import br.edu.ufba.softvis.visminer.model.Commit;
import br.edu.ufba.softvis.visminer.model.Committer;
import br.edu.ufba.softvis.visminer.model.File;
import br.edu.ufba.softvis.visminer.model.Tree;
import br.edu.ufba.softvis.visminer.utility.StringUtils;

/**
 * @see SCM Implementation for GIT repositories.
 */
public class JGitRepository implements SCM {

	private Repository repository;
	private Git git;
	private RevWalk revWalk;
	private TreeWalk treeWalk;
	private DiffFormatter diffFormatter;
	private String vmBranch;

	private static final String CHARSET = "UTF-8";

	// Process the repository path
	private String processPath(String repositoryPath) {

		String path = repositoryPath.replace("\\", "/").trim();

		if (path.endsWith("/.git")) {
			return path;
		} else if (path.endsWith("/.git/")) {
			return path.substring(0, path.length() - 1);
		} else if (path.endsWith("/")) {
			return path.concat(".git");
		} else {
			return path.concat("/.git");
		}

	}

	@Override
	public void open(String path) {

		FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
		String processedPath = processPath(path);

		try {

			repository = repositoryBuilder.setGitDir(new java.io.File(processedPath)).readEnvironment() // read
																										// git
																										// environment
																										// variables
					.findGitDir().build();

		} catch (IOException e) {
			throw new VisMinerAPIException(e.getMessage(), e);
		}

		git = new Git(repository);
		revWalk = new RevWalk(repository);
		treeWalk = new TreeWalk(repository);

		diffFormatter = new DiffFormatter(DisabledOutputStream.INSTANCE);
		diffFormatter.setRepository(repository);
		diffFormatter.setContext(0);
		diffFormatter.setDiffComparator(RawTextComparator.DEFAULT);
		diffFormatter.setDetectRenames(false);

		vmBranch = "vm";

	}

	@Override
	public String getAbsolutePath() {
		return repository.getWorkTree().getAbsolutePath().replace("\\", "/");
	}

	@Override
	public List<Tree> getTrees() {
		try {
			int id = 1;

			List<Tree> trees = new ArrayList<Tree>();

			Iterable<Ref> refs = git.branchList().call();
			for (Ref ref : refs) {
				if (ref.getName().equals("HEAD"))
					continue;

				int i = ref.getName().lastIndexOf("/");
				Tree tree = new Tree(id, getLastCommitDate(ref.getName()), ref.getName().substring(++i), ref.getName(),
						TreeType.BRANCH);
				trees.add(tree);

				id++;
			}

			refs = git.tagList().call();
			for (Ref ref : refs) {

				int i = ref.getName().lastIndexOf("/");
				Tree tree = new Tree(id, getLastCommitDate(ref.getName()), ref.getName().substring(++i), ref.getName(),
						TreeType.TAG);
				trees.add(tree);

				id++;
			}

			return trees;

		} catch (GitAPIException e) {
			clean(e);
		}

		return null;

	}

	private Date getLastCommitDate(String treeName) {
		try {
			revWalk.reset();
			RevCommit lastCommit = revWalk.parseCommit(repository.resolve(treeName));
			return lastCommit.getAuthorIdent().getWhen();

		} catch (IOException e) {
			clean(e);
		}

		return null;
	}

	@Override
	public List<Commit> getCommits() {
		try {
			Iterable<RevCommit> revCommits;
			revCommits = git.log().all().call();
			List<Commit> commits = new ArrayList<Commit>();

			for (RevCommit revCommit : revCommits) {
				PersonIdent author = revCommit.getAuthorIdent();
				Commit commit = new Commit(revCommit.getId().getName(), author.getWhen(), revCommit.getFullMessage(),
						revCommit.getName());
				Committer committer = new Committer(author.getEmailAddress(), author.getName());
				commit.setCommitter(committer);
				commits.add(commit);
			}

			return commits;

		} catch (GitAPIException e) {
			clean(e);
		} catch (IOException e) {
			clean(e);
		}

		return null;
	}

	@Override
	public List<Commit> getCommitsByTree(String treeName, TreeType type) {
		Iterable<RevCommit> revCommits = null;

		try {
			if (type == TreeType.BRANCH) {
				revCommits = getCommitsFromBranch(treeName);
			} else if (type == TreeType.TAG) {
				revCommits = getCommitsFromTag(treeName);
			}

			List<Commit> commits = new ArrayList<Commit>();
			for (RevCommit revCommit : revCommits) {
				Commit commit = new Commit(revCommit.getId().getName(), revCommit.getAuthorIdent().getWhen(),
						revCommit.getFullMessage(), revCommit.getName());
				commits.add(commit);
			}

			return commits;
		} catch (GitAPIException e) {
			clean(e);
		} catch (RevisionSyntaxException e) {
			clean(e);
		} catch (MissingObjectException e) {
			clean(e);
		} catch (IncorrectObjectTypeException e) {
			clean(e);
		} catch (AmbiguousObjectException e) {
			clean(e);
		} catch (IOException e) {
			clean(e);
		}

		return null;
	}

	private Iterable<RevCommit> getCommitsFromTag(String treeName)
			throws GitAPIException, MissingObjectException, IncorrectObjectTypeException {
		List<Ref> call = git.tagList().call();
		Iterable<RevCommit> logs = null;

		for (Ref ref : call) {

			if (ref.getName().equals(treeName)) {

				LogCommand log = git.log();
				Ref peeledRef = repository.peel(ref);

				if (peeledRef.getPeeledObjectId() != null) {
					log.add(peeledRef.getPeeledObjectId());
				} else {
					log.add(ref.getObjectId());
				}

				logs = log.call();
				return logs;
			}
		}

		return null;
	}

	private Iterable<RevCommit> getCommitsFromBranch(String treeName)
			throws RevisionSyntaxException, NoHeadException, MissingObjectException, IncorrectObjectTypeException,
			AmbiguousObjectException, GitAPIException, IOException {
		Iterable<RevCommit> revCommits = git.log().add(repository.resolve(treeName)).call();
		return revCommits;
	}

	@Override
	public String getSource(String commitName, String filePath) {
		try {
			String source = "";

			revWalk.reset();
			ObjectReader reader = repository.newObjectReader();

			RevCommit revCommit = revWalk.parseCommit(ObjectId.fromString(commitName));
			RevTree tree = revCommit.getTree();
			TreeWalk treeWalk = TreeWalk.forPath(reader, filePath, tree);
			if (treeWalk != null) {
				ObjectId objId = treeWalk.getObjectId(0);
				if (objId != null) {
					byte[] bytes = reader.open(objId).getBytes();
					source = new String(bytes, CHARSET);
				}
			}
			reader.close();

			return source;

		} catch (UnsupportedEncodingException e) {
			clean(e);
		} catch (MissingObjectException e) {
			clean(e);
		} catch (IncorrectObjectTypeException e) {
			clean(e);
		} catch (IOException e) {
			clean(e);
		}

		return "";
	}

	@Override
	public List<File> getCommitedFiles(Commit commit) {
		List<File> files = new ArrayList<File>();

		try {

			String repoPath = getAbsolutePath();
			RevCommit revCommit = revWalk.parseCommit(ObjectId.fromString(commit.getName()));
			AnyObjectId currentCommit = repository.resolve(commit.getName());
			AnyObjectId oldCommit = revCommit.getParentCount() > 0
					? repository.resolve(revCommit.getParent(0).getName()) : null;

			List<DiffEntry> diffs = null;
			diffs = diffFormatter.scan(oldCommit, currentCommit);

			for (DiffEntry entry : diffs) {

				RevCommit parentCommit = oldCommit == null ? null
						: revWalk.parseCommit(ObjectId.fromString(oldCommit.getName()));

				File file = new File();

				String path = !entry.getNewPath().equals("/dev/null") ? entry.getNewPath() : entry.getOldPath();

				file.setPath(path);
				file.setUid(StringUtils.sha1(repoPath + "/" + file.getPath()));
				getFileChanges(path, parentCommit, revCommit, file);

				if (entry.getChangeType() == org.eclipse.jgit.diff.DiffEntry.ChangeType.DELETE)
					file.setChangeType(ChangeType.DELETE);
				else if ((entry.getChangeType() == org.eclipse.jgit.diff.DiffEntry.ChangeType.ADD)
						|| entry.getChangeType() == org.eclipse.jgit.diff.DiffEntry.ChangeType.COPY)
					file.setChangeType(ChangeType.ADD);
				else
					file.setChangeType(ChangeType.MODIFY);
				files.add(file);
			}

		} catch (IOException e) {
			clean(e);
		}

		return files;
	}

	private void getFileChanges(String path, RevCommit oldCommit, RevCommit currentCommit, File file) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		DiffFormatter formatter = new DiffFormatter(output);
		formatter.setRepository(repository);
		formatter.setContext(0);
		formatter.setPathFilter(PathFilter.create(path));

		try {
			formatter.format(oldCommit, currentCommit);
		} catch (IOException e) {
			clean(e);
		}

		Scanner scanner = new Scanner(output.toString());
		int added = 0, removed = 0;

		while (scanner.hasNextLine()) {

			String line = scanner.nextLine();
			if (line.startsWith("+") && !line.startsWith("+++")) {
				added++;
			} else if (line.startsWith("-") && !line.startsWith("---")) {
				removed++;
			}

		}

		try {
			output.close();
		} catch (IOException e) {
		}

		formatter.close();
		scanner.close();

		file.setLinesAdded(added);
		file.setLinesRemoved(removed);
	}

	@Override
	public List<String> getRepositoryFiles(String hash) {
		try {
			revWalk.reset();
			RevCommit lastCommit = revWalk.parseCommit(repository.resolve(hash));

			treeWalk.reset();
			treeWalk.addTree(lastCommit.getTree());
			treeWalk.setRecursive(true);

			List<String> files = new ArrayList<String>();
			while (treeWalk.next()) {
				String path = treeWalk.getPathString();
				files.add(StringUtils.sha1(path));
			}

			return files;

		} catch (IOException e) {
			clean(e);
		}

		return null;
	}

	@Override
	public void checkout(String hash) {
		try {

			if (!git.status().call().isClean())
				git.reset().setMode(ResetType.HARD).call();

			git.checkout().setName("master").setForce(true).call();
			deleteVMBranch();
			git.checkout().setCreateBranch(true).setStartPoint(hash).setName(vmBranch).setForce(true).call();

		} catch (GitAPIException e) {
			clean(e);
		}
	}

	public void close() {
		diffFormatter.close();
		treeWalk.close();
		revWalk.close();
		git.close();
		repository.close();
	}

	private void deleteVMBranch() {
		try {
			List<Ref> refs = git.branchList().call();
			for (Ref ref : refs) {

				if (ref.getName().endsWith(vmBranch)) {
					git.branchDelete().setBranchNames(vmBranch).setForce(true).call();
					break;
				}

			}

		} catch (GitAPIException e) {
			clean(e);
		}
	}

	public void reset() {
		try {
			if (!git.status().call().isClean())
				git.reset().setMode(ResetType.HARD).call();

			git.checkout().setName("master").setForce(true).call();
			git.branchDelete().setBranchNames(vmBranch).setForce(true).call();

		} catch (Exception e) {
			clean(e);
		}
	}

	private void clean(Throwable e) {
		close();
		throw new VisMinerAPIException(e.getMessage(), e);
	}

	protected Git getGit() {
		return this.git;
	}

}