package br.edu.ufba.softvis.visminer.analyzer.local;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import org.gitective.core.filter.commit.AuthorSetFilter;
import org.gitective.core.filter.commit.CommitListFilter;

import br.edu.ufba.softvis.visminer.constant.TreeType;
import br.edu.ufba.softvis.visminer.model.bean.Commit;
import br.edu.ufba.softvis.visminer.model.bean.Committer;
import br.edu.ufba.softvis.visminer.model.bean.File;
import br.edu.ufba.softvis.visminer.model.bean.FileState;
import br.edu.ufba.softvis.visminer.model.bean.Tree;
import br.edu.ufba.softvis.visminer.utility.AuthorEmailFilter;
import br.edu.ufba.softvis.visminer.utility.StringUtils;

/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * @see IRepositorySystem
 * Implementation for GIT repositories.
 */
public class GitRepository implements IRepositorySystem{

	private Repository repository;
	private Git git;
	String repositoryPath;
	
	// Process the repository path
	private String processPath(String repositoryPath){
		
		String path = repositoryPath.replace("\\", "/").trim();
		
		if(path.endsWith("/.git")){
			return path;
		}else if(path.endsWith("/.git/")){
			return path.substring(0, path.length()-1);
		}else if(path.endsWith("/")){
			return path.concat(".git");
		}else{
			return path.concat("/.git");
		}
		
	}
	
	public GitRepository(String repositoryPath) {

		String path = processPath(repositoryPath);
		
		try {
			this.repository = new FileRepository(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.git = new Git(this.repository);
		this.repositoryPath = this.repository.getWorkTree().getAbsolutePath().replace("\\", "/");
		
	}

	@Override
	public String getAbsolutePath() {
		return 	this.repositoryPath;
	}

	@Override
	public List<Committer> getCommitters(){

		AuthorSetFilter authorFilter = new AuthorSetFilter();
		CommitFinder finder = new CommitFinder(repository);
		finder.setFilter(authorFilter);
		finder.find();

		Set<Committer> committers = new HashSet<Committer>();

		for(PersonIdent person : authorFilter.getPersons()){
			Committer committer = new Committer(0, person.getEmailAddress(), person.getName(), true);
			committers.add(committer);
		}

		try {

			Iterable<RevCommit> revCommits = git.log().all().call();
			for(RevCommit revCommit : revCommits){
				Committer committer = new Committer(0, revCommit.getAuthorIdent().getEmailAddress(), revCommit.getAuthorIdent().getName(), false);
				committers.add(committer);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		List<Committer> committersList = new ArrayList<Committer>(committers.size());
		committersList.addAll(committers);

		return committersList;

	}

	@Override
	public List<Tree> getTrees(){

		try{

			List<Tree> trees = new ArrayList<Tree>();

			Iterable<Ref> refs = git.branchList().call();
			for(Ref ref : refs){
				Tree tree = new Tree(0, getLastCommitDate(ref.getName()), ref.getName().split("/")[2], ref.getName(), TreeType.BRANCH);
				trees.add(tree);
			}

			refs = git.tagList().call();
			for(Ref ref : refs){
				Tree tree = new Tree(0, getLastCommitDate(ref.getName()), ref.getName().split("/")[2], ref.getName(), TreeType.TAG);
				trees.add(tree);
			}

			return trees;	

		}catch(GitAPIException e){
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public Date getLastCommitDate(String treeName) {

		try {

			RevWalk revWalk = new RevWalk(repository);
			RevCommit lastCommit;

			lastCommit = revWalk.parseCommit(repository.resolve(treeName));
			return lastCommit.getAuthorIdent().getWhen();

		} catch (Exception e) {
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
				Commit commit = new Commit(0, revCommit.getAuthorIdent().getWhen(), revCommit.getFullMessage(), revCommit.getName());
				commits.add(commit);
			}

			return commits;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public List<Commit> getCommitsByCommitter(String committerEmail){

		AndCommitFilter filters = new AndCommitFilter();
		CommitListFilter revCommits = new CommitListFilter();
		AuthorEmailFilter authorFilter = new AuthorEmailFilter(committerEmail);
		filters.add(authorFilter);
		filters.add(revCommits);

		CommitFinder finder = new CommitFinder(repository);
		finder.setFilter(filters);
		finder.findInBranches();

		return createListCommit(revCommits);

	}

	public List<Commit> getCommitsByTree(String treeName) {

		AndCommitFilter filters = new AndCommitFilter();
		CommitListFilter revCommits = new CommitListFilter();
		filters.add(revCommits);

		CommitFinder finder = new CommitFinder(repository);
		finder.setFilter(filters);
		finder.findFrom(treeName);

		return createListCommit(revCommits);

	}

	@Override
	public byte[] getData(String commitName, String filePath) {

		try {

			RevWalk walk = new RevWalk(repository);
			RevCommit revCommit = walk.parseCommit(ObjectId.fromString(commitName));
			RevTree tree = revCommit.getTree();
			ObjectReader reader = repository.newObjectReader();
			TreeWalk treeWalk = TreeWalk.forPath(reader, filePath, tree);

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
	public List<File> getCommitedFiles(String commitName) {

		java.io.File gitDir = new java.io.File(repository.getDirectory().toString());

		ProcessBuilder builder = new ProcessBuilder();
		builder.directory(gitDir);

		List<String> commands = Arrays.asList("git", "log", "--numstat", "--oneline", "--max-count=1", commitName);
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

			buffer.readLine(); // skips the first line, it is useless

			while((str = buffer.readLine()) != null){

				String parts[] = str.split("\t"); //parts[0] - lines added, parts[1] - lines removed, parts[2] - file path
				
				int linesAdded = -1;
				int linesRemoved = -1;
				String path = this.repositoryPath+"/"+parts[2];
				
				if(!parts[0].equals("-"))
					linesAdded = Integer.parseInt(parts[0]);
				if(!parts[1].equals("-"))
					linesRemoved = Integer.parseInt(parts[1]);
				
				FileState fs = new FileState(linesAdded, linesRemoved, false);
				if(getData(commitName, parts[2]) == null){
					fs.setDeleted(true);
				}

				File f = new File(0, path, StringUtils.sha1(path));
				f.setFileState(fs);
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
	public void close() {

		this.repository.close();
		this.git.close();

	}

	/*
	 * Avoids repeat code in the process to create a commit list.
	 */
	private List<Commit> createListCommit(CommitListFilter revCommits){
		List<Commit> commits = new ArrayList<Commit>();
		for(RevCommit revCommit : revCommits.getCommits()){
			Commit commit = new Commit(0, revCommit.getAuthorIdent().getWhen(), revCommit.getFullMessage(), revCommit.getName());
			commits.add(commit);
		}
		return commits;
	}

}