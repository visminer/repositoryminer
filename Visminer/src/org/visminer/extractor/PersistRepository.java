package org.visminer.extractor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.visminer.constant.RepositoryType;
import org.visminer.model.database.Commit;
import org.visminer.model.database.Committer;
import org.visminer.model.database.File;
import org.visminer.model.database.FileXCommit;
import org.visminer.model.database.FileXCommitPK;
import org.visminer.model.database.Repository;
import org.visminer.model.database.Tree;
import org.visminer.persistence.PersistenceFacade;
import org.visminer.utility.StringDigest;

public class PersistRepository {

	private ILocalRepository repository = null;
	private Repository myRepository = null;
	private Set<Commit> commits = null;
	
	private PersistenceFacade persistenceFacade;
	
	
	public PersistRepository(org.visminer.model.business.Repository repo){
		
		persistenceFacade = new PersistenceFacade();
		
		switch(repo.getType()){
			case RepositoryType.GIT: this.repository = new GitRepository(); break;
		}
	
		this.repository.setLocalPath(repo.getPath());
		init(repo);
		
	}

	private void init(org.visminer.model.business.Repository repo){
		
		this.myRepository = new Repository();
		this.myRepository.setName(repo.getName());
		this.myRepository.setType(repo.getType());
		this.myRepository.setRemoteName(repo.getRemote_name());
		this.myRepository.setRemoteOwner(repo.getRemote_owner());
		this.myRepository.setRemoteService(repo.getRemoteType());
		this.myRepository.setPath(this.repository.getRepositoryAbsolutePath());
		this.myRepository.setSha(StringDigest.sha1(this.myRepository.getPath()));	
		
	}

	public void persist(){
		
		setCommittersInRepository();
		setCommitsInCommitters();
		this.myRepository = persistenceFacade.saveRepository(this.myRepository);
		persistTrees();
		persistFilesAndStates();
		
	}
	
	private void setCommittersInRepository(){
		
		List<Committer> committers = this.repository.getCommitters();
		
		for(Committer committer : committers){
			Committer c = persistenceFacade.getCommitterByEmailAndName(committer.getName(), committer.getEmail());
			if(c != null) committer.setId(c.getId());
			committer.setCommits(new ArrayList<Commit>());
		}

		this.myRepository.setCommitters(committers);
		
	}
	
	
	private void setCommitsInCommitters(){

		List<Tree> trees = this.repository.getTrees();
		for(Tree tree : trees){
			List<Commit> commitsAux = this.repository.getCommits(tree.getFullName());
			for(Commit commit : commitsAux){
				int i = this.myRepository.getCommitters().indexOf(commit.getCommitter());
				if(i > -1){
					Committer committerAux = this.myRepository.getCommitters().get(i);
					if(!committerAux.getCommits().contains(commit)) committerAux.addCommit(commit);
				}else{
					Committer c = commit.getCommitter();
					c.setCommits(new ArrayList<Commit>());
					c.addCommit(commit);
					this.myRepository.getCommitters().add(c);
				}
			}
		}
		
	}
	
	private void persistTrees(){
		
		this.commits = new HashSet<Commit>();
		List<Tree> trees = this.repository.getTrees();
		
		for(Tree tree : trees){
			List<Commit> commits = this.repository.getCommits(tree.getFullName());
			List<String> names = new ArrayList<String>();
			for(Commit c : commits){
				names.add(c.getName());
			}
			
			commits = persistenceFacade.getCommitsByNames(names);
			tree.setRepository(this.myRepository);
			tree.setCommits(commits);
			this.commits.addAll(commits);
			
		}
		persistenceFacade.saveAllTrees(trees);
		
	}
	
	private void persistFilesAndStates(){
		
		List<FileXCommit> filesStates = new ArrayList<FileXCommit>();
		
		for(Commit commit : this.commits){
			List<File> committedFiles = this.repository.getCommitedFiles(commit.getName());
			for(File file : committedFiles){
				
				File fileAux = persistenceFacade.getFileBySha(file.getSha());
				if(fileAux == null)
					fileAux = persistenceFacade.saveFile(file);
				
				FileXCommit fxc = new FileXCommit(new FileXCommitPK(fileAux.getId(), commit.getId()));
				fxc.setFile(fileAux);
				fxc.setCommit(commit);
				
				if(this.repository.getFileState(commit.getName(), file.getPath()) != null)
					fxc.setDeleted(false);
				else
					fxc.setDeleted(true);
				
				filesStates.add(fxc);
				
			}
		}
		
		persistenceFacade.saveFilesXCommits(filesStates);
		
	}
	
}