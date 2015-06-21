package br.edu.ufba.softvis.visminer.model.business;

import java.util.ArrayList;
import java.util.List;

import br.edu.ufba.softvis.visminer.persistence.PersistenceInterface;

/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * User friendly project bean class.
 * This class will be used for user interface.
 */

public class Project {

	private List<Committer> committers;
	private List<Commit> commits;
	private List<File> files;
	private List<SoftwareUnit> softwareUnits;
	private Tree currentTree;
	private Repository repository;
	
	// Auxiliary properties
	
	// Index of current commit in commits list.
	private int currentCommit;
	
	/* Number of commits made by each committer until the current commit, each index in array
	 * represents is the index of committer in committers list. 
	 */
	private int committerContribution[];
	
	public Project(){}
	
	public Project(Repository repository){
		this.repository = repository;
	}
	
	/**
	 * @return True if is possible access the next commit or false otherwise
	 * Changes the repository state to the state of the next commit. 
	 */
	public boolean nextCommit(){
		
		PersistenceInterface persistence = new PersistenceInterface();
		
		// Don't exists next commit.
		if(currentCommit == (commits.size() - 1)){
			return false;
		}
		
		currentCommit++;
		softwareUnits = persistence.findSoftwareUnitByRepository(repository.getId(), currentCommit);
		
		Commit commit = commits.get(currentCommit);
		
		//Updates committers
		committersUpdateHelper(commit.getCommitter(), false);
		
		// Compute only the new files.
		updateFiles(commit.getCommitedFiles());
		
		persistence.close();
		readSoftwareUnitTree(softwareUnits);
		
		return true;
		
	}
	
	/**
	 * @return True if is possible access the previous commit or false otherwise
	 * Changes the repository state to the state of the previous commit. 
	 */
	public boolean prevCommit(){
		
		PersistenceInterface persistence = new PersistenceInterface();
		
		// Don't exists previous commit.
		if(currentCommit == 0){
			return false;
		}
		
		currentCommit--;
		softwareUnits = persistence.findSoftwareUnitByRepository(repository.getId(), currentCommit);

		
		Commit commit = commits.get(currentCommit+1);
		committersUpdateHelper(commit.getCommitter(), true);
		
		files = new ArrayList<File>();
		
		// Empty the files list and recompute from the beginning which files will be accessible.
		for(int i = 0; i <= currentCommit; i++){
			updateFiles(commits.get(i).getCommitedFiles());
		}
		
		persistence.close();
		readSoftwareUnitTree(softwareUnits);
		
		return true;
		
	}
	
	/**
	 * @param commit
	 * @return True if is possible access the commit or false otherwise
	 * Changes the repository state to the state of certain commit.
	 */
	public boolean setCurrentCommit(Commit commit){
		
		PersistenceInterface persistence = new PersistenceInterface();
		
		int index = commits.indexOf(commit);
		if(index == -1){
			return false;
		}
		
		
		if((currentCommit - index) < 0){
			// Computes if commit is after current commit.
			softwareUnits = persistence.findSoftwareUnitByRepository(repository.getId(), index);	
			
			// From current commit forward adding new committers.
			for(int i = currentCommit+1; i <= index; i++){
				Commit commit2 = commits.get(i);
				committersUpdateHelper(commit2.getCommitter(), false);
				// Process only commitd files in the new commits.
				updateFiles(commit.getCommitedFiles());
			}
			currentCommit = index;
			
		}else if((currentCommit - index) > 0){
			
			// Computes if commit is before current commit.
			softwareUnits = persistence.findSoftwareUnitByRepository(repository.getId(), index);
			
			// From current commit backward removing committers.
			for(int i = currentCommit; i > index; i--){
				Commit commit2 = commits.get(i);
				committersUpdateHelper(commit2.getCommitter(), true);
			}
			
			currentCommit = index;
			
			// Empty files list and recomputes files from the beginning until new current commit.
			files = new ArrayList<File>();
			for(int i = 0; i <= currentCommit; i++){
				updateFiles(commits.get(i).getCommitedFiles());
			}
			
		}
		persistence.close();
		readSoftwareUnitTree(softwareUnits);
		return true;
		
	}


	/**
	 * @return The commit that define the current state of the repository.
	 */
	public Commit getCurrentCommit(){
		return commits.get(currentCommit);
	}
	
	/**
	 * 
	 * @param currentTree
	 * Reset project with the informations of the new tree.
	 * By default the repository state setted is of the last commit.
	 */
	public void setCurrentTree(Tree currentTree) {
		this.currentTree = currentTree;
		resetProject();
	}
	
	// helpers
	
	// Receives a list of committed files and updates their status in list files.
	private void updateFiles(List<File> files2){
		
		for(File file : files2){
			if(!files.contains(file)){
				files.add(file);
			}else{
				files.remove(file);
				
				// update file state
				if(!file.getFileState().isDeleted()){
					files.add(file);
				}
			}
		}
		
	}

	/* 
	 * Helps to decide which committers will appear based in current commit.
	 * Only committers that have commited until the current commit will be visible.
	 * Set delete true if want delete the committer otherwise false. 
	 */
	private void committersUpdateHelper(Committer committer, boolean delete){
		
		if(delete){
			
			int index = committers.indexOf(committer);
			committerContribution[index] -= 1;
			if(committerContribution[index] == 0){
				committers.remove(index);
			}
			
		}else{
			
			if(!committers.contains(committer)){
				committers.add(committer);
				committerContribution[committers.size() - 1] = 1;
			}else{
				int index = committers.indexOf(committer);
				committerContribution[index] += 1; 
			}
			
		}
		
	}

	/*
	 * When the tree is changed or a randomly commit is chosen, this method helps to set the repository state.
	 * This method computes repository state from initial commit until a certain commit.
	 */
	private void resetProject(){

		PersistenceInterface persistence = new PersistenceInterface();
		
		committerContribution = new int[repository.getCommitters().size()];
		commits = persistence.findCommitsByTree(currentTree.getId());
		currentCommit = commits.size() - 1;
		softwareUnits = persistence.findSoftwareUnitByRepository(repository.getId(), currentCommit);
		
		committers = new ArrayList<Committer>();
		files = new ArrayList<File>();
		
		// Set committers and files in their lists.
		for(Commit commit : commits){
			committersUpdateHelper(commit.getCommitter(), false);
			updateFiles(commit.getCommitedFiles());
		}
		readSoftwareUnitTree(softwareUnits);
		persistence.close();
		
	}
	
	/*
	 * Reads software units tree recursively and relates the software units with the files that are their parent.
	 */
	private void readSoftwareUnitTree(List<SoftwareUnit> list){
		
		//No software units saved or a leaf element was found.
		if(list == null){
			return;
		}
		
		for(int i = 0; i < list.size(); i++){
			SoftwareUnit elem = list.get(i);
			// Has parent file
			if(elem.getFile() != null){
				
				int index = files.indexOf(elem.getFile());
				
				if(index == -1){
					continue;
				}
				
				File f = files.get(index);
				if(f.getSoftwareUnits() == null){
					f.setSoftwareUnits(new ArrayList<SoftwareUnit>());
				}
				f.getSoftwareUnits().add(elem);
				elem.setFile(f);
				
			}else{
				//No has parent file
				readSoftwareUnitTree(list.get(i).getChildren());
			}
		}
		
	}

	// getters and setters

	/**
	 * 
	 * @return The current tree.
	 */
	public Tree getCurrentTree(){
		return currentTree;
	}
	
	/**
	 * @return Committers that have made a commit until current commit.
	 */
	public List<Committer> getCommitters() {
		return committers;
	}

	/**
	 * @param committers the committers to set
	 */
	public void setCommitters(List<Committer> committers) {
		this.committers = committers;
	}

	/**
	 * @return All commits of the tree.
	 */
	public List<Commit> getCommits() {
		return commits;
	}

	/**
	 * @param commits the commits to set
	 */
	public void setCommits(List<Commit> commits) {
		this.commits = commits;
	}

	/**
	 * @return All files presents in actual repository state, defined by current commit.
	 */
	public List<File> getFiles() {
		return files;
	}

	/**
	 * @param files the files to set
	 */
	public void setFiles(List<File> files) {
		this.files = files;
	}

	/**
	 * @return All software units presents in actual repository state, defined by current commit.
	 */
	public List<SoftwareUnit> getSoftwareUnits() {
		return softwareUnits;
	}

	/**
	 * @param softwareUnits the softwareUnits to set
	 */
	public void setSoftwareUnits(List<SoftwareUnit> softwareUnits) {
		this.softwareUnits = softwareUnits;
	}

	/**
	 * @return the repository
	 */
	public Repository getRepository() {
		return repository;
	}

	/**
	 * @param repository the repository to set
	 */
	public void setRepository(Repository repository) {
		this.repository = repository;
	}
	
}