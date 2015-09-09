package br.edu.ufba.softvis.visminer.analyzer.local;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
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

import br.edu.ufba.softvis.visminer.constant.TreeType;
import br.edu.ufba.softvis.visminer.error.VisMinerAPIException;
import br.edu.ufba.softvis.visminer.model.database.CommitDB;
import br.edu.ufba.softvis.visminer.model.database.CommitterDB;
import br.edu.ufba.softvis.visminer.model.database.FileDB;
import br.edu.ufba.softvis.visminer.model.database.FileXCommitDB;
import br.edu.ufba.softvis.visminer.model.database.FileXCommitPK;
import br.edu.ufba.softvis.visminer.model.database.TreeDB;
import br.edu.ufba.softvis.visminer.utility.StringUtils;

/**
 * @see IVersioningSystem
 * Implementation for GIT repositories.
 */
public class GitSystem implements IVersioningSystem{

	private Repository repository;
	private Git git;
	private String repositoryPath;
	private RevWalk revWalk;
	private TreeWalk treeWalk;
	private ObjectReader reader;
	private ProcessBuilder processBuilder;
	private List<String> commands;
	
	private static final String HEAD = "HEAD";
	
	@Override
	public void open(String repositoryPath) {
		
		FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
		try {
			repository = repositoryBuilder.setGitDir(new File(repositoryPath))
							.readEnvironment() //read git environment variables
							.findGitDir()
							.build();
		} catch (IOException e) {
			throw new VisMinerAPIException(e.getMessage(), e);
		}

		git = new Git(repository);
		revWalk = new RevWalk(repository);
		treeWalk = new TreeWalk(repository);
		reader = repository.newObjectReader();

		processBuilder = new ProcessBuilder();
		processBuilder.directory(new File(repository.getDirectory().toString()));
		
		commands = Arrays.asList("git", "log", "--numstat", "--oneline", "--max-count=1", "");
		
		this.repositoryPath = repository.getWorkTree().getAbsolutePath();

	}
	
	@Override
	public String getAbsolutePath() {
		return repositoryPath;
	}

	@Override
	public List<TreeDB> getTrees(){

		try{
			
			List<TreeDB> trees = new ArrayList<TreeDB>();

			Iterable<Ref> refs = git.branchList().call();
			for(Ref ref : refs){

				if(ref.getName().equals(HEAD))
					continue;

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
			throw new VisMinerAPIException(e.getMessage(), e);
		}

	}

	@Override
	public Date getLastCommitDate(String treeName){

		try {

			RevCommit lastCommit = revWalk.parseCommit(repository.resolve(treeName));
			return lastCommit.getAuthorIdent().getWhen();

		} catch (IOException e) {
			throw new VisMinerAPIException(e.getMessage(), e);
		}


	}

	@Override
	public List<CommitDB> getCommits(){

		try{
			
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
			
		}catch(GitAPIException e){
			throw new VisMinerAPIException(e.getMessage(), e);
		}catch(IOException e){
			throw new VisMinerAPIException(e.getMessage(), e);
		}

	}

	public List<CommitDB> getCommitsByTree(String treeName, TreeType type) {

		Iterable<RevCommit> revCommits = null;
		
		if(type == TreeType.BRANCH){
			revCommits = getCommitsFromBranch(treeName);
		}else if(type == TreeType.TAG){
			revCommits = getCommitsFromTag(treeName);
		}

		List<CommitDB> commits = new ArrayList<CommitDB>();
		for(RevCommit revCommit : revCommits){
			CommitDB commit = new CommitDB(0, revCommit.getAuthorIdent().getWhen(), revCommit.getFullMessage(),
					revCommit.getName());
			commits.add(commit);
		}

		return commits;

	}

	private Iterable<RevCommit> getCommitsFromTag(String treeName){

		try{
			
			List<Ref> call = git.tagList().call();
			Iterable<RevCommit> logs = null;

			for(Ref ref : call){

				if(ref.getName().equals(treeName)){
					
					LogCommand log = git.log();
					Ref peeledRef = repository.peel(ref);
					
					if(peeledRef.getPeeledObjectId() != null) {
						log.add(peeledRef.getPeeledObjectId());
					} else {
						log.add(ref.getObjectId());
					}

					logs = log.call();
					return logs;
					
				}
			}
			
			return null;
			
		} catch(GitAPIException e){
			throw new VisMinerAPIException(e.getMessage(), e);
		} catch (MissingObjectException e) {
			throw new VisMinerAPIException(e.getMessage(), e);
		} catch (IncorrectObjectTypeException e) {
			throw new VisMinerAPIException(e.getMessage(), e);
		}

	}

	private Iterable<RevCommit> getCommitsFromBranch(String treeName){

		try{
			
			Iterable<RevCommit> revCommits = git.log()
					.add(repository.resolve(treeName))
					.call();
			return revCommits;
			
		}catch(GitAPIException e){
			throw new VisMinerAPIException(e.getMessage(), e);
		}catch(IOException e){
			throw new VisMinerAPIException(e.getMessage(), e);
		}

	}

	@Override
	public byte[] getData(String commitName, String filePath){

		try{
			
			RevCommit revCommit = revWalk.parseCommit(ObjectId.fromString(commitName));
			RevTree tree = revCommit.getTree();
			TreeWalk treeWalk = TreeWalk.forPath(reader, filePath, tree);

			if(treeWalk == null)
				return null;
			else
				return reader.open(treeWalk.getObjectId(0)).getBytes();

		}catch(IOException e){
			throw new VisMinerAPIException(e.getMessage(), e);
		}
	}

	@Override
	public List<FileDB> getCommitedFiles(CommitDB commitDb) {

		
		commands.set(5, commitDb.getName());
		processBuilder.command(commands);
		Process process = null;

		try {
			
			process = processBuilder.start();
			BufferedReader buffer = new BufferedReader(new InputStreamReader(process.getInputStream()));
			
			String str = null;
			List<FileDB> files = new ArrayList<FileDB>();

			buffer.readLine(); // skips the first line
			while((str = buffer.readLine()) != null){

				String parts[] = str.split("\t"); //parts[0] - lines added, parts[1] - lines removed, parts[2] - file path

				int linesAdded = -1;
				int linesRemoved = -1;
				String path = repositoryPath+"/"+parts[2];

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
			return files;
			
		} catch(IOException e){
			throw new VisMinerAPIException(e.getMessage(), e);
		}

	}

	@Override
	public List<String> getSnapshotFiles(String commitUid){

		try{
			
			RevCommit lastCommit = revWalk.parseCommit(repository.resolve(commitUid));
			
			treeWalk.reset();
			treeWalk.addTree(lastCommit.getTree());
			treeWalk.setRecursive(true);

			List<String> files = new ArrayList<String>();
			while(treeWalk.next()){
				String path = repositoryPath+"/"+treeWalk.getPathString();
				files.add(StringUtils.sha1(path));

			}

			return files;
			
		}catch(IOException e){
			throw new VisMinerAPIException(e.getMessage(), e);
		}

	}

	@Override
	public void checkoutToTree(String treeName){
		
		try{
			git.checkout().setName(treeName).call();
		}catch(GitAPIException e){
			throw new VisMinerAPIException(e.getMessage(), e);
		}
		
	}

	@Override
	public void close() {
		
		repository.close();
		git.close();
		treeWalk.close();
		revWalk.close();
		reader.close();
		
	}

}