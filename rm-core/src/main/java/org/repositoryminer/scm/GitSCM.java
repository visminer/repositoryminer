package org.repositoryminer.scm;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.eclipse.jgit.util.io.DisabledOutputStream;
import org.repositoryminer.exception.ErrorMessage;
import org.repositoryminer.exception.RepositoryMinerException;
import org.repositoryminer.model.Change;
import org.repositoryminer.model.ChangeType;
import org.repositoryminer.model.Commit;
import org.repositoryminer.model.Reference;
import org.repositoryminer.model.ReferenceType;
import org.repositoryminer.model.SCMType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GitSCM implements ISCM {

	private static final Logger LOGGER = LoggerFactory.getLogger(GitSCM.class);

	private Repository repository;
	private Git git;
	private RevWalk revWalk;
	private TreeWalk treeWalk;
	private DiffFormatter diffFormatter;
	private String repoPath;

	@Override
	public SCMType getSCM() {
		return SCMType.GIT;
	}

	@Override
	public void open(String path) {
		FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
		File repoFolder = new File(path, ".git");

		if (!repoFolder.exists()) {
			throw new RepositoryMinerException(ErrorMessage.REPOSITORY_NOT_FOUND.toString());
		}

		try {
			repository = repositoryBuilder.setGitDir(repoFolder).readEnvironment().findGitDir().build();
		} catch (IOException e) {
			throw new RepositoryMinerException(ErrorMessage.GIT_REPOSITORY_IOERROR.toString(), e);
		}

		git = new Git(repository);
		revWalk = new RevWalk(repository);
		treeWalk = new TreeWalk(repository);
		repoPath = path;

		diffFormatter = new DiffFormatter(DisabledOutputStream.INSTANCE);
		diffFormatter.setRepository(repository);
		diffFormatter.setContext(0);
		diffFormatter.setDiffComparator(RawTextComparator.DEFAULT);
		diffFormatter.setDetectRenames(true);
		diffFormatter.setBinaryFileThreshold(2048);
	}

	@Override
	public List<Reference> getReferences() {
		List<Reference> refs = new ArrayList<Reference>();
		Iterable<Ref> branches = null;

		try {
			branches = git.branchList().call();
		} catch (GitAPIException e) {
			errorHandler(ErrorMessage.GIT_BRANCH_LIST_ERROR.toString(), e);
		}

		for (Ref b : branches) {
			if (b.getName().equals("HEAD")) {
				continue;
			}

			int i = b.getName().lastIndexOf("/") + 1;
			Reference r = new Reference(null, null, b.getName().substring(i), b.getName(), ReferenceType.BRANCH, null);
			refs.add(r);
		}

		Iterable<Ref> tags = null;
		try {
			tags = git.tagList().call();
		} catch (GitAPIException e) {
			errorHandler(ErrorMessage.GIT_TAG_LIST_ERROR.toString(), e);
		}

		for (Ref t : tags) {
			int i = t.getName().lastIndexOf("/") + 1;
			Reference r = new Reference(null, null, t.getName().substring(i), t.getName(), ReferenceType.TAG, null);
			refs.add(r);
		}

		return refs;
	}

	@Override
	public List<Commit> getCommits(int skip, int maxCount, String endPoint, Collection<String> commitsToSkip) {
		Iterable<RevCommit> revCommits = null;

		if (endPoint.startsWith("refs/tags/")) {
			revCommits = getCommitsFromTag(endPoint, skip, maxCount);
		} else {
			revCommits = getCommitsFromSomePoint(endPoint, skip, maxCount);
		}

		if (revCommits == null) {
			return new ArrayList<Commit>();
		}

		List<Commit> commits = new ArrayList<Commit>();

		if (commitsToSkip == null || commitsToSkip.size() == 0) {
			for (RevCommit revCommit : revCommits) {
				commits.add(processCommit(revCommit));
			}
		} else {
			for (RevCommit revCommit : revCommits) {
				if (!commitsToSkip.contains(revCommit.getName())) {
					commits.add(processCommit(revCommit));
				}
			}
		}

		return commits;
	}

	@Override
	public List<String> getCommitsNames(String endPoint) {
		Iterable<RevCommit> revCommits = null;

		if (endPoint.startsWith("refs/tags/")) {
			revCommits = getCommitsFromTag(endPoint, -1, -1);
		} else {
			revCommits = getCommitsFromSomePoint(endPoint, -1, -1);
		}

		if (revCommits == null) {
			return new ArrayList<String>();
		}

		List<String> names = new ArrayList<String>();
		for (RevCommit revCommit : revCommits) {
			names.add(revCommit.getName());
		}
		return names;
	}

	@Override
	public void checkout(String hash) {
		File lockFile = new File(repoPath, "git/index.lock");
		if (lockFile.exists()) {
			lockFile.delete();
		}

		try {
			git.checkout().setStartPoint(hash).setAllPaths(true).setForce(true).call();
		} catch (GitAPIException e) {
			errorHandler(ErrorMessage.GIT_CHECKOUT_ERROR.toString(), e);
		}
	}

	@Override
	public void close() {
		diffFormatter.close();
		treeWalk.close();
		revWalk.close();
		git.close();
		repository.close();
	}

	@Override
	public void reset() {
		try {
			if (!git.status().call().isClean()) {
				git.reset().setMode(ResetType.HARD).call();
			}
		} catch (NoWorkTreeException | GitAPIException e) {
			errorHandler(ErrorMessage.GIT_RESET_ERROR.toString(), e);
		}
	}

	private void errorHandler(String errorMessage, Throwable e) {
		close();
		LOGGER.error(errorMessage, e);
		throw new RepositoryMinerException(errorMessage, e);
	}

	private Commit processCommit(RevCommit revCommit) {
		PersonIdent author = revCommit.getAuthorIdent();
		PersonIdent committer = revCommit.getCommitterIdent();

		org.repositoryminer.model.PersonIdent myAuthor = new org.repositoryminer.model.PersonIdent(author.getName(), author.getEmailAddress());
		org.repositoryminer.model.PersonIdent myCommitter = new org.repositoryminer.model.PersonIdent(committer.getName(), committer.getEmailAddress());

		List<String> parents = new ArrayList<String>();
		for (RevCommit parent : revCommit.getParents()) {
			parents.add(parent.getName());
		}

		List<Change> changes = null;
		try {
			changes = getChangesForCommitedFiles(revCommit.getName());
		} catch (IOException e) {
			errorHandler(ErrorMessage.GIT_RETRIEVE_CHANGES_ERROR.toString(), e);
		}

		return new Commit(revCommit.getName(), revCommit.getFullMessage(), author.getWhen(), committer.getWhen(), null,
				parents, (parents.size() > 1), myAuthor, myCommitter, changes);
	}

	private List<Change> getChangesForCommitedFiles( String hash) throws IOException {
		 RevCommit commit = revWalk.parseCommit(ObjectId.fromString(hash));
		
		if (commit.getParentCount() > 1) {
			return new ArrayList<Change>();
		}
		
		 RevCommit parentCommit = commit.getParentCount() > 0
				? revWalk.parseCommit(ObjectId.fromString(commit.getParent(0).getName()))
				: null;

		 List<DiffEntry> diffEntries = diffFormatter.scan(parentCommit, commit);
		 List<Change> changes = new ArrayList<Change>();

		for (DiffEntry entry : diffEntries) {
			Change change = processChange(entry);
			analyzeCodeChurn(change, parentCommit, commit);
			changes.add(change);
		}

		return changes;
	}

	private Change processChange( DiffEntry entry) {
		switch (entry.getChangeType()) {
		case ADD:
			return new Change(entry.getNewPath(), null, 0, 0, ChangeType.ADD);

		case COPY:
			return new Change(entry.getNewPath(), entry.getOldPath(), 0, 0, ChangeType.COPY);

		case DELETE:
			return new Change(entry.getOldPath(), null, 0, 0, ChangeType.DELETE);

		case MODIFY:
			return new Change(entry.getNewPath(), null, 0, 0, ChangeType.MODIFY);

		case RENAME:
			return new Change(entry.getNewPath(), entry.getOldPath(), 0, 0, ChangeType.MOVE);

		default:
			return null;
		}
	}

	private void analyzeCodeChurn(Change change, RevCommit parentCommit,
			 RevCommit commit) throws IOException {
		 ByteArrayOutputStream output = new ByteArrayOutputStream();
		 DiffFormatter formatter = new DiffFormatter(output);

		formatter.setRepository(repository);
		formatter.setContext(0);
		formatter.setPathFilter(PathFilter.create(change.getPath()));
		formatter.format(parentCommit, commit);

		 Scanner scanner = new Scanner(output.toString());
		 int added = 0;
		 int removed = 0;

		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line.startsWith("+") && !line.startsWith("+++")) {
				added++;
			} else if (line.startsWith("-") && !line.startsWith("---")) {
				removed++;
			}
		}

		output.close();
		formatter.close();
		scanner.close();

		change.setLinesAdded(added);
		change.setLinesRemoved(removed);
	}

	private Iterable<RevCommit> getCommitsFromTag(String path, int skip, int maxCount) {
		try {
			 List<Ref> call = git.tagList().call();

			for (Ref ref : call) {
				if (ref.getName().equals(path)) {
					LogCommand log = git.log();
					Ref peeledRef = repository.peel(ref);
					if (peeledRef.getPeeledObjectId() != null) {
						log.add(peeledRef.getPeeledObjectId());
					} else {
						log.add(ref.getObjectId());
					}
					return log.setSkip(skip).setMaxCount(maxCount).call();
				}
			}

			return null;
		} catch (GitAPIException | IncorrectObjectTypeException | MissingObjectException e) {
			errorHandler(ErrorMessage.GIT_BRANCH_COMMITS_ERROR.toString(), e);
			return null;
		}
	}

	private Iterable<RevCommit> getCommitsFromSomePoint(String path, int skip, int maxCount) {
		try {
			return git.log().add(repository.resolve(path)).setSkip(skip).setMaxCount(maxCount).call();
		} catch (RevisionSyntaxException | GitAPIException | IOException e) {
			errorHandler(ErrorMessage.GIT_TAG_COMMITS_ERROR.toString(), e);
			return null;
		}
	}

}
