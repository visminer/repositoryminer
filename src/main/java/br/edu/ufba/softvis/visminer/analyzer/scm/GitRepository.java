package br.edu.ufba.softvis.visminer.analyzer.scm;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
import br.edu.ufba.softvis.visminer.model.database.CommitDB;
import br.edu.ufba.softvis.visminer.model.database.CommitterDB;
import br.edu.ufba.softvis.visminer.model.database.FileDB;
import br.edu.ufba.softvis.visminer.model.database.FileXCommitDB;
import br.edu.ufba.softvis.visminer.model.database.FileXCommitPK;
import br.edu.ufba.softvis.visminer.model.database.TreeDB;
import br.edu.ufba.softvis.visminer.utility.StringUtils;

/**
 * @see SCM
 * Implementation for GIT repositories.
 */
public class GitRepository implements SCM{

	private Repository repository;
	private Git git;
	private RevWalk revWalk;
	private TreeWalk treeWalk;
	private DiffFormatter diffFormatter;

	private static final String CHARSET = "UTF-8";

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

	@Override
	public void open(String path) {

		FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
		String processedPath = processPath(path);

		try {

			repository = repositoryBuilder.setGitDir(new File(processedPath))
					.readEnvironment() //read git environment variables
					.findGitDir()
					.build();

		} catch (IOException e) {
			throw new VisMinerAPIException(e.getMessage(), e);
		}

		git = new Git(repository);
		revWalk = new RevWalk(repository);
		treeWalk = new TreeWalk(repository);

		diffFormatter = new DiffFormatter(DisabledOutputStream.INSTANCE);
		diffFormatter.setRepository(repository);
		//diffFormatter.setBinaryFileThreshold(2 * 1024); // 2 mb is the max
		diffFormatter.setContext(0);
		diffFormatter.setDiffComparator(RawTextComparator.DEFAULT);
		diffFormatter.setDetectRenames(false);

	}

	@Override
	public String getAbsolutePath() {
		return repository.getWorkTree().getAbsolutePath().replace("\\", "/");
	}

	@Override
	public List<TreeDB> getTrees(){

		try{

			List<TreeDB> trees = new ArrayList<TreeDB>();

			Iterable<Ref> refs = git.branchList().call();
			for(Ref ref : refs){

				if(ref.getName().equals("HEAD"))
					continue;

				int i = ref.getName().lastIndexOf("/");
				TreeDB tree = new TreeDB(0, ref.getName(), getLastCommitDate(ref.getName()), ref.getName().substring(++i),
						TreeType.BRANCH);
				trees.add(tree);

			}

			refs = git.tagList().call();
			for(Ref ref : refs){

				int i = ref.getName().lastIndexOf("/");
				TreeDB tree = new TreeDB(0, ref.getName(), getLastCommitDate(ref.getName()), ref.getName().substring(++i),
						TreeType.TAG);
				trees.add(tree);

			}

			return trees;

		}catch(GitAPIException e){
			clean(e);
		}
		
		return null;

	}

	private Date getLastCommitDate(String treeName){

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
			clean(e);
		}catch(IOException e){
			clean(e);
		}
		
		return null;

	}

	@Override
	public List<CommitDB> getCommitsByTree(String treeName, TreeType type) {

		Iterable<RevCommit> revCommits = null;

		try{

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

		}catch(GitAPIException e){
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

	private Iterable<RevCommit> getCommitsFromTag(String treeName) throws GitAPIException, MissingObjectException, IncorrectObjectTypeException{


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

	}

	private Iterable<RevCommit> getCommitsFromBranch(String treeName) throws RevisionSyntaxException, NoHeadException, MissingObjectException, IncorrectObjectTypeException, AmbiguousObjectException, GitAPIException, IOException{

		Iterable<RevCommit> revCommits = git.log()
				.add(repository.resolve(treeName))
				.call();
		return revCommits;

	}

	@Override
	public String getSource(String commitName, String filePath){

		try {

			revWalk.reset();
			ObjectReader reader = repository.newObjectReader();

			RevCommit revCommit = revWalk.parseCommit(ObjectId.fromString(commitName));
			RevTree tree = revCommit.getTree();
			TreeWalk treeWalk = TreeWalk.forPath(reader, filePath, tree);
			byte[] bytes = reader.open(treeWalk.getObjectId(0)).getBytes();
			reader.close();
			
			return new String(bytes, CHARSET);

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
	public List<FileDB> getCommitedFiles(CommitDB commitDB){

		try{
			
			String repoPath = getAbsolutePath();
			RevCommit revCommit = revWalk.parseCommit(ObjectId.fromString(commitDB.getName()));
			AnyObjectId currentCommit = repository.resolve(commitDB.getName());
			AnyObjectId oldCommit = revCommit.getParentCount() > 0 ? 
					repository.resolve(revCommit.getParent(0).getName()) : null;

			List<DiffEntry> diffs = null;
			diffs = diffFormatter.scan(oldCommit, currentCommit);

			List<FileDB> filesDB = new ArrayList<FileDB>();
			for(DiffEntry entry : diffs) {

				RevCommit parentCommit = oldCommit == null ? null : 
					revWalk.parseCommit(ObjectId.fromString(oldCommit.getName()));
				
				FileDB fileDB = new FileDB();
				FileXCommitDB fxcDB = new FileXCommitDB();
				
				fxcDB.setId(new FileXCommitPK(0, commitDB.getId()));
				
				String path = !entry.getNewPath().equals("/dev/null") ? entry.getNewPath() 
						: entry.getOldPath();
				
				fileDB.setPath(repoPath + "/" + path);
				fileDB.setUid(StringUtils.sha1(fileDB.getPath()));
				getFileChanges(path, parentCommit, revCommit, fxcDB);
				
				if(entry.getChangeType() == org.eclipse.jgit.diff.DiffEntry.ChangeType.DELETE)
					fxcDB.setChangeType(ChangeType.DELETE);
				else if((entry.getChangeType() == org.eclipse.jgit.diff.DiffEntry.ChangeType.ADD)
							|| entry.getChangeType() == org.eclipse.jgit.diff.DiffEntry.ChangeType.COPY)
						fxcDB.setChangeType(ChangeType.ADD);
				else
						fxcDB.setChangeType(ChangeType.MODIFY);
					
				
				
				List<FileXCommitDB> fxcList = new ArrayList<FileXCommitDB>(1);
				fxcList.add(fxcDB);
				fileDB.setFileXCommits(fxcList);
				filesDB.add(fileDB);
				
			}

			return filesDB;

		}catch(IOException e){
			clean(e);
		}
		
		return null;

	}

	private void getFileChanges(String path, RevCommit oldCommit,
			RevCommit currentCommit, FileXCommitDB fxcDB){

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

		while(scanner.hasNextLine()){

			String line = scanner.nextLine();
			if(line.startsWith("+") && !line.startsWith("+++")){
				added++;
			}else if(line.startsWith("-") && !line.startsWith("---")){
				removed++;
			}

		}

		try {
			output.close();
		} catch (IOException e) {}

		formatter.close();
		scanner.close();

		fxcDB.setLinesAdded(added);
		fxcDB.setLinesRemoved(removed);

	}

	@Override
	public List<String> getRepositoryFiles(String hash){

		try{

			revWalk.reset();
			RevCommit lastCommit = revWalk.parseCommit(repository.resolve(hash));

			treeWalk.reset();
			treeWalk.addTree(lastCommit.getTree());
			treeWalk.setRecursive(true);

			List<String> files = new ArrayList<String>();
			while(treeWalk.next()){
				String path = getAbsolutePath() + "/" + treeWalk.getPathString();
				files.add(StringUtils.sha1(path));
			}

			return files;

		}catch(IOException e){
			clean(e);
		}
		
		return null;

	}

	@Override
	public void checkout(String hash){

		try{

			if(!git.status().call().isClean())
				git.reset().setMode(ResetType.HARD).setRef(hash).call();
			
			git.checkout().setName(hash).call();

		}catch(GitAPIException e){
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
	
	private void clean(Throwable e){
		
		close();
		throw new VisMinerAPIException(e.getMessage(), e);
		
	}

}