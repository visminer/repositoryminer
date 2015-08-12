package br.edu.ufba.softvis.visminer.analyzer.local;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
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

import br.edu.ufba.softvis.visminer.constant.TreeType;
import br.edu.ufba.softvis.visminer.model.database.CommitDB;
import br.edu.ufba.softvis.visminer.model.database.CommitterDB;
import br.edu.ufba.softvis.visminer.model.database.FileDB;
import br.edu.ufba.softvis.visminer.model.database.FileXCommitDB;
import br.edu.ufba.softvis.visminer.model.database.FileXCommitPK;
import br.edu.ufba.softvis.visminer.model.database.TreeDB;
import br.edu.ufba.softvis.visminer.utility.StringUtils;

/**
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
	public List<TreeDB> getTrees(){

		try{

			List<TreeDB> trees = new ArrayList<TreeDB>();

			Iterable<Ref> refs = git.branchList().call();
			for(Ref ref : refs){
				TreeDB tree = new TreeDB(0, ref.getName(), getLastCommitDate(ref.getName()), ref.getName().split("/")[2],
						TreeType.BRANCH);
				trees.add(tree);
			}

			refs = git.tagList().call();
			for(Ref ref : refs){
				TreeDB tree = new TreeDB(0, ref.getName(), getLastCommitDate(ref.getName()), ref.getName().split("/")[2],
						TreeType.TAG);
				trees.add(tree);
			}

			return trees;	

		}catch(GitAPIException e){
			e.printStackTrace();
			return null;
		}

	}

	@SuppressWarnings("resource")
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
	public List<CommitDB> getCommits() {

		try {

			Iterable<RevCommit> revCommits;
			revCommits = git.log().all().call();
			List<CommitDB> commits = new ArrayList<CommitDB>();

			for(RevCommit revCommit : revCommits){
				
				PersonIdent author = revCommit.getAuthorIdent();
				CommitDB commit = new CommitDB(0, author.getWhen(), revCommit.getFullMessage(),
						revCommit.getName());
				CommitterDB committerDb = new CommitterDB(0, author.getEmailAddress(), author.getName());
				commit.setCommitter(committerDb);
				commits.add(commit);
				
			}

			return commits;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public List<CommitDB> getCommitsByTree(String treeName) {

		Iterable<RevCommit> revCommits = null;
		try {
			
			revCommits = git.log()
					.add(repository.resolve(treeName))
					.call();
			
			List<CommitDB> commits = new ArrayList<CommitDB>();
			for(RevCommit revCommit : revCommits){
				CommitDB commit = new CommitDB(0, revCommit.getAuthorIdent().getWhen(), revCommit.getFullMessage(),
						revCommit.getName());
				commits.add(commit);
			}
			
			return commits;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		

	}

	@SuppressWarnings("resource")
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
	public List<FileDB> getCommitedFiles(CommitDB commitDb) {

		java.io.File gitDir = new java.io.File(repository.getDirectory().toString());

		ProcessBuilder builder = new ProcessBuilder();
		builder.directory(gitDir);

		List<String> commands = Arrays.asList("git", "log", "--numstat", "--oneline", "--max-count=1", commitDb.getName());
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

		List<FileDB> files = new ArrayList<FileDB>();

		try {

			buffer.readLine(); // skips the first line, it is useless

			while((str = buffer.readLine()) != null){

				String parts[] = str.split("\t"); //parts[0] - lines added, parts[1] - lines removed, parts[2] - file path
				
				int linesAdded = -1;
				int linesRemoved = -1;
				String path = this.repositoryPath+"/"+parts[2];
				
				if(!parts[0].equals("-")){
					linesAdded = Integer.parseInt(parts[0]);
				}
				if(!parts[1].equals("-")){
					linesRemoved = Integer.parseInt(parts[1]);
				}
				
				FileDB fileDb = new FileDB(0, path, StringUtils.sha1(path));
				FileXCommitDB fileXCommit = new FileXCommitDB(new FileXCommitPK(0, commitDb.getId()),
						linesAdded, linesRemoved, false);
				
				if(getData(commitDb.getName(), parts[2]) == null){
					fileXCommit.setRemoved(true);
				}
				
				List<FileXCommitDB> filesXCommits = new ArrayList<FileXCommitDB>(1);
				filesXCommits.add(fileXCommit);
				fileDb.setFileXCommits(filesXCommits);
				files.add(fileDb);
				
			}

			buffer.close();

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return files;		

	}

	@SuppressWarnings("resource")
	@Override
	public List<String> getSnapshotFilesNames(String commitUid){

		
		try {
			
			RevWalk revWalk = new RevWalk(repository);
			RevCommit lastCommit = revWalk.parseCommit(repository.resolve(commitUid));
			TreeWalk treeWalk = new TreeWalk(repository);
			treeWalk.addTree(lastCommit.getTree());
			treeWalk.setRecursive(true);
			
			List<String> files = new ArrayList<String>();
			while(treeWalk.next()){
				String path = this.repositoryPath+"/"+treeWalk.getPathString();
				files.add(path);
				
			}
			
			return files;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	@Override
	public void close() {
		this.repository.close();
		this.git.close();
	}

}