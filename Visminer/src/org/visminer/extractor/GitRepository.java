package org.visminer.extractor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.gitective.core.CommitFinder;
import org.gitective.core.filter.commit.AndCommitFilter;
import org.gitective.core.filter.commit.AuthorFilter;
import org.gitective.core.filter.commit.AuthorSetFilter;
import org.gitective.core.filter.commit.CommitListFilter;
import org.visminer.constant.TreeType;
import org.visminer.model.database.Commit;
import org.visminer.model.database.Committer;
import org.visminer.model.database.File;
import org.visminer.model.database.Tree;
import org.visminer.utility.StringDigest;

public class GitRepository implements ILocalRepository {
	private org.eclipse.jgit.lib.Repository repository;

	@Override
	public void setLocalPath(String localPath) {

		try {
			this.repository = new FileRepository(localPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public List<Committer> getCommitters() {
		
		AuthorSetFilter authorFilter = new AuthorSetFilter();
		CommitFinder finder = new CommitFinder(repository);
		finder.setFilter(authorFilter);
		finder.find();
		
		List<Committer> committers = new ArrayList<Committer>();
		for(PersonIdent person : authorFilter.getPersons()){
			Committer committer = new Committer(person.getEmailAddress(), person.getName());
			if(!committers.contains(committer)){
				committers.add(committer);
			}
		}
		
		return committers;
		
	}

	@Override
	public List<Tree> getTrees() {
		
		org.eclipse.jgit.api.Git git = new org.eclipse.jgit.api.Git(this.repository);
		List<Tree> trees = new ArrayList<Tree>();
		
		try {
			String name = "";
			for(Ref ref : git.branchList().call()){
				name = ref.getName();
				Tree tree = new Tree(name, name.split("/")[2], TreeType.BRANCH);
				trees.add(tree);
			}
			for(Ref ref : git.tagList().call()){
				name = ref.getName();
				Tree tree = new Tree(name, name.split("/")[2], TreeType.TAG);
				trees.add(tree);
			}
			return trees;
		} catch (GitAPIException e) {
			return null;
		}
		
	}

	@Override
	public Commit getLastCommit(String treeName) {
		RevWalk revWalk = new RevWalk(repository);
		RevCommit lastCommit;
		try {
			lastCommit = revWalk.parseCommit(repository.resolve(treeName));
			Commit commit = new Commit();
			commit.setDate(lastCommit.getAuthorIdent().getWhen());
			commit.setMessage(lastCommit.getFullMessage());
			commit.setName(lastCommit.getName());
			return commit;
		} catch (RevisionSyntaxException | IOException e) {
			e.printStackTrace();
			return null;
		}			
	}

	@Override
	public List<File> getCommitedFiles(String commitName){
		
		java.io.File gitDir = new java.io.File(repository.getDirectory().toString());
		
		ProcessBuilder builder = new ProcessBuilder();
		builder.directory(gitDir);
		
		List<String> commands = Arrays.asList("git", "show", "--pretty=format:", "--name-only", "--no-commit-id", commitName);
		builder.command(commands);
		Process process;
		
		try {
			process = builder.start();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		BufferedReader buffer = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String str = null;
		
		List<File> files = new ArrayList<File>();
		try {
			while((str = buffer.readLine()) != null){
				File f = new File();
				f.setPath(str);
				f.setSha(StringDigest.sha1(str));
				files.add(f);
			}
			buffer.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return files;
		
	}

	@Override
	public List<String> getState(String name) {

		RevWalk revWalk = new RevWalk(repository);
		RevCommit lastCommit;
		try {
			
			lastCommit = revWalk.parseCommit(repository.resolve(name));
			TreeWalk treeWalk = new TreeWalk(repository);
			treeWalk.addTree(lastCommit.getTree());
			treeWalk.setRecursive(true);
			
			List<String> files = new ArrayList<String>();
			while(treeWalk.next()){
				files.add(treeWalk.getPathString());
			}
			
			return files;
			
		} catch (RevisionSyntaxException | IOException e) {
			e.printStackTrace();
			return null;
		}
		
		
	}

	@Override
	public byte[] getFileState(String commitName, String filePath) {
		
		RevWalk walk = new RevWalk(repository);
		RevCommit revCommit;
		try {
			
			revCommit = walk.parseCommit(ObjectId.fromString(commitName));
			RevTree tree = revCommit.getTree();
			ObjectReader reader = repository.newObjectReader();
			
			TreeWalk treeWalk;
			treeWalk = TreeWalk.forPath(reader, filePath, tree);
			
			if(treeWalk == null)
				return null;
			else
				return reader.open(treeWalk.getObjectId(0)).getBytes();
			
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		
	}

	@Override
	public List<Commit> getCommits(Committer committer, String tree) {
		
		AndCommitFilter filters = new AndCommitFilter();
		CommitListFilter revCommits = new CommitListFilter();
		AuthorFilter authorFilter = new AuthorFilter(new PersonIdent(committer.getName(), committer.getEmail()));
		filters.add(authorFilter);
		filters.add(revCommits);
		
		CommitFinder finder = new CommitFinder(repository);
		finder.setFilter(filters);
		finder.findFrom(tree);
		return getCommitList(revCommits);

	}

	@Override
	public List<Commit> getCommits(Committer committer) {
		
		AndCommitFilter filters = new AndCommitFilter();
		CommitListFilter revCommits = new CommitListFilter();
		AuthorFilter authorFilter = new AuthorFilter(new PersonIdent(committer.getName(), committer.getEmail()));
		filters.add(authorFilter);
		filters.add(revCommits);
		
		CommitFinder finder = new CommitFinder(repository);
		finder.setFilter(filters);
		finder.find();
		return getCommitList(revCommits);
		
	}

	@Override
	public List<Commit> getCommits(String treeName) {
		
		AndCommitFilter filters = new AndCommitFilter();
		CommitListFilter revCommits = new CommitListFilter();
		filters.add(revCommits);
		
		CommitFinder finder = new CommitFinder(repository);
		finder.setFilter(filters);
		finder.findFrom(treeName);
		return getCommitList(revCommits);
		
	}

	@Override
	public String getRepositoryAbsolutePath() {
		return this.repository.getDirectory().getAbsolutePath();
	}
	
	private List<Commit> getCommitList(CommitListFilter commitsList){
		
		List<Commit> commits = new ArrayList<Commit>();
		for(RevCommit revCommit : commitsList.getCommits()){
			Commit commit = new Commit();
			commit.setMessage(revCommit.getFullMessage());
			commit.setName(revCommit.getName());
			commit.setCommitter(new Committer(revCommit.getCommitterIdent().getEmailAddress(), revCommit.getCommitterIdent().getName()));
			commit.setDate(revCommit.getCommitterIdent().getWhen());
			commits.add(commit);
		}
	
		return commits;
		
	}

}