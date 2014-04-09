package org.visminer.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathSuffixFilter;
import org.gitective.core.CommitFinder;
import org.gitective.core.filter.commit.AndCommitFilter;
import org.gitective.core.filter.commit.AuthorFilter;
import org.gitective.core.filter.commit.AuthorSetFilter;
import org.gitective.core.filter.commit.CommitListFilter;
import org.visminer.model.Commit;
import org.visminer.model.Committer;
import org.visminer.model.Version;

public class GitUtil {

	private final String TYPE_BRANCH = "branch";
	private final String TYPE_TAG = "tag";
	
	private Repository repository;
	
	public GitUtil(String path) throws IOException{
	
		repository = new FileRepository(path);
		
	}
	
	public org.visminer.model.Repository getRepository(){
		
		org.visminer.model.Repository userRepo = new org.visminer.model.Repository();
		userRepo.setPath(repository.getDirectory().getPath().replace("\\", "/"));
		String[] parts = userRepo.getPath().split("/");
		userRepo.setName(parts[parts.length - 2]);
		
		return userRepo;
		
	}
	
	public List<Version> getVersions() throws GitAPIException{
		
		Git git = new Git(repository);
		List<Version> versions = new ArrayList<Version>();
		versions.addAll(analyzeRefs(git.branchList().call(), TYPE_BRANCH));
		versions.addAll(analyzeRefs(git.tagList().call(), TYPE_TAG));
		
		return versions;
	}
	
	private List<Version> analyzeRefs(List<Ref> refs, String type){
		
		List<Version> versions = new ArrayList<Version>();
		
		for(Ref ref : refs){
			
			Version version = new Version();
			version.setPath(ref.getName());
			version.setType(type);
			
			String[] parts = ref.getName().split("/");
			version.setName(parts[parts.length - 1]);
			
			versions.add(version);
			
		}	
		
		return versions;
		
	}
	
	public List<Committer> getCommitters(){
		
		AuthorSetFilter authorFilter = new AuthorSetFilter();
		CommitFinder finder = new CommitFinder(repository);
		finder.setFilter(authorFilter);
		finder.findInBranches();
		
		List<Committer> committers = new ArrayList<Committer>();
		for(PersonIdent person : authorFilter.getPersons()){
			Committer committer = new Committer(person.getEmailAddress(), person.getName());
			committers.add(committer);
		}
		
		return committers;
		
	}
	
	public List<Commit> getCommits(String version, Committer committer){
		
		AndCommitFilter filters = new AndCommitFilter();
		CommitListFilter revCommits = new CommitListFilter();
		AuthorFilter authorFilter = new AuthorFilter(new PersonIdent(committer.getName(), committer.getEmail()));
		filters.add(authorFilter);
		filters.add(revCommits);
		
		CommitFinder finder = new CommitFinder(repository);
		finder.setFilter(filters);
		finder.findFrom(version);
		
		List<Commit> commits = new ArrayList<Commit>();

		for(RevCommit revCommit : revCommits.getCommits()){
			Commit commit = new Commit();
			commit.setMessage(revCommit.getFullMessage());
			commit.setSha(revCommit.getName());
			commit.setCommitter(committer);
			commit.setDate(revCommit.getCommitterIdent().getWhen());
			
			commits.add(commit);
			
		}
		
		return commits;
		
	}	
	
	public List<String> getFilesNameInCommit(String commitSha) throws MissingObjectException, IncorrectObjectTypeException, IOException{
		
		java.io.File gitDir = new java.io.File(repository.getDirectory().toString());
		
		ProcessBuilder builder = new ProcessBuilder();
		builder.directory(gitDir);
		
		List<String> commands = new ArrayList<String>();
		commands.add("git");
		commands.add("show");
		commands.add("--pretty=format:");
		commands.add("--name-only");
		commands.add("--no-commit-id");
		commands.add(commitSha);
		
		
		builder.command(commands);
		Process process = builder.start();
		
		BufferedReader buffer = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String str = null;
		
		List<String> paths = new ArrayList<String>();
		while((str = buffer.readLine()) != null){
			if(str.endsWith(".java")){
				paths.add(str);
			}
		}
		
		return paths;
		
	}
	
	public List<String> getFilesNameInVersion(String version) throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException{
		
		RevWalk revWalk = new RevWalk(repository);
		RevCommit lastCommit = revWalk.parseCommit(repository.resolve(version));
		
		TreeWalk treeWalk = new TreeWalk(repository);
		treeWalk.addTree(lastCommit.getTree());
		treeWalk.setFilter(PathSuffixFilter.create(".java"));
		treeWalk.setRecursive(true);
		
		List<String> files = new ArrayList<String>();
		while(treeWalk.next()){
			files.add(treeWalk.getPathString());
		}
		
		return files;
		
	}
	
	public Commit getLastCommit(String version) throws RevisionSyntaxException, MissingObjectException, IncorrectObjectTypeException, AmbiguousObjectException, IOException{
		
		RevWalk revWalk = new RevWalk(repository);
		RevCommit lastCommit = revWalk.parseCommit(repository.resolve(version));
		
		Commit commit = new Commit();
		commit.setDate(lastCommit.getAuthorIdent().getWhen());
		commit.setMessage(lastCommit.getFullMessage());
		commit.setSha(lastCommit.getName());
		
		return commit;
		
	}
	
	public String getFileStates(String commitSha, String filePath) throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException{
		
		RevWalk walk = new RevWalk(repository);
		RevCommit revCommit =  walk.parseCommit(ObjectId.fromString(commitSha));
		RevTree tree = revCommit.getTree();
		ObjectReader reader = repository.newObjectReader();
		
		TreeWalk treeWalk = TreeWalk.forPath(reader, filePath, tree);
		
		if(treeWalk == null)
			return null;
		
		byte[] data = reader.open(treeWalk.getObjectId(0)).getBytes();
		return new String(data);
		
	}
	
}