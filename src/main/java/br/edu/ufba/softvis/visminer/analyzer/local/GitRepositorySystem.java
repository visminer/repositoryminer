package br.edu.ufba.softvis.visminer.analyzer.local;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
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
import org.gitective.core.CommitFinder;
import org.gitective.core.filter.commit.AndCommitFilter;
import org.gitective.core.filter.commit.AuthorFilter;
import org.gitective.core.filter.commit.AuthorSetFilter;
import org.gitective.core.filter.commit.CommitListFilter;

import br.edu.ufba.softvis.visminer.constant.TreeType;

public class GitRepositorySystem implements IRepositorySystem{

	private Repository repository;
	private Git git;
	
	@Override
	public void open(String repositoryPath) {
		try {
			this.repository = new FileRepository(repositoryPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.git = new Git(this.repository);
	}
	
	@Override
	public String getAbsolutePath() {
		return this.repository.getWorkTree().getAbsolutePath();	
	}
	
	@Override
	public List<Committer> getCommitters() {
		AuthorSetFilter authorFilter = new AuthorSetFilter();
		CommitFinder finder = new CommitFinder(repository);
		finder.setFilter(authorFilter);
		finder.find();
		
		Set<Committer> committers = new HashSet<Committer>();

		for(PersonIdent person : authorFilter.getPersons()){
			Committer committer = new Committer(person.getEmailAddress(), person.getName(), true);
			committers.add(committer);
		}
		
		try {
			
			Iterable<RevCommit> revCommits = git.log().all().call();
			for(RevCommit revCommit : revCommits){
				Committer committer = new Committer(revCommit.getAuthorIdent().getEmailAddress(), revCommit.getAuthorIdent().getName(), false);
				committers.add(committer);
			}
			
		} catch (GitAPIException | IOException e) {
			e.printStackTrace();
		}
		
		List<Committer> committersList = new ArrayList<Committer>(committers.size());
		committersList.addAll(committers);
		
		return committersList;
	}
	
	@Override
	public List<Tree> getTrees() {
		try{
			
		List<Tree> trees = new ArrayList<Tree>();
		
		Iterable<Ref> refs = git.branchList().call();
		for(Ref ref : refs){
			Tree tree = new Tree(getLastCommitDate(ref.getName()), ref.getName().split("/")[2], ref.getName(), TreeType.BRANCH);
			trees.add(tree);
		}

		refs = git.tagList().call();
		for(Ref ref : refs){
			Tree tree = new Tree(getLastCommitDate(ref.getName()), ref.getName().split("/")[2], ref.getName(), TreeType.TAG);
			trees.add(tree);
		}

		return trees;	
		
		}catch(GitAPIException e){
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public List<String> getCommitsNames(String treeName) {
		AndCommitFilter filters = new AndCommitFilter();
		CommitListFilter revCommits = new CommitListFilter();
		filters.add(revCommits);
		
		CommitFinder finder = new CommitFinder(repository);
		finder.setFilter(filters);
		finder.findFrom(treeName);
		
		List<String> names = new ArrayList<String>();
		
		for(RevCommit revCommit : revCommits.getCommits()){
			names.add(revCommit.getName());
		}
	
		return names;
	}
	
	@Override
	public Date getLastCommitDate(String treeName) {
		try {
			
			RevWalk revWalk = new RevWalk(repository);
			RevCommit lastCommit;
			
			lastCommit = revWalk.parseCommit(repository.resolve(treeName));
			return lastCommit.getAuthorIdent().getWhen();
			
		} catch (RevisionSyntaxException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public List<Commit> getCommits() {
		try {
			
			Iterable<RevCommit> revCommits;
			revCommits = git.log().all().call();
			List<Commit> commits = new ArrayList<Commit>();

			for(RevCommit revCommit : revCommits){
				Commit commit = new Commit(revCommit.getAuthorIdent().getWhen(), revCommit.getFullMessage(), revCommit.getName());
				commits.add(commit);
			}

			return commits;
			
		} catch (GitAPIException | IOException e) {
			e.printStackTrace();
			return null;
		}

	}
	
	@Override
	public List<Commit> getCommits(Committer committer){
		AndCommitFilter filters = new AndCommitFilter();
		CommitListFilter revCommits = new CommitListFilter();
		AuthorFilter authorFilter = new AuthorFilter(new PersonIdent(committer.getName(), committer.getEmail()));
		filters.add(authorFilter);
		filters.add(revCommits);
		
		CommitFinder finder = new CommitFinder(repository);
		finder.setFilter(filters);
		finder.find();
		
		return createListCommit(revCommits);		
	}

	public List<Commit> getCommits(String treeName) {
		AndCommitFilter filters = new AndCommitFilter();
		CommitListFilter revCommits = new CommitListFilter();
		filters.add(revCommits);
		
		CommitFinder finder = new CommitFinder(repository);
		finder.setFilter(filters);
		finder.findFrom(treeName);
		
		return createListCommit(revCommits);
	}
	
	@Override
	public AST getAST(String commitName, String filePath) {
		try {
			
			RevWalk walk = new RevWalk(repository);
			RevCommit revCommit;
			TreeWalk treeWalk;
			
			revCommit = walk.parseCommit(ObjectId.fromString(commitName));
			RevTree tree = revCommit.getTree();
			ObjectReader reader = repository.newObjectReader();
			treeWalk = TreeWalk.forPath(reader, filePath, tree);
			
			if(treeWalk == null)
				return null;
			else
				return null;//reader.open(treeWalk.getObjectId(0)).getBytes();
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public Map<File, FileState> getCommitedFiles(String commitName) {
		return null;
	}
	
	@Override
	public void close() {
		this.repository.close();
		this.git.close();		
	}
	
	
	
	
	// Helpers	
	private List<Commit> createListCommit(CommitListFilter revCommits){
		List<Commit> commits = new ArrayList<Commit>();
		for(RevCommit revCommit : revCommits.getCommits()){
			Commit commit = new Commit(revCommit.getAuthorIdent().getWhen(), revCommit.getFullMessage(), revCommit.getName());
			commit.setCommitter(new Committer(revCommit.getAuthorIdent().getEmailAddress(),revCommit.getAuthorIdent().getName()));
			commits.add(commit);
		}
		return commits;
	}
	
}