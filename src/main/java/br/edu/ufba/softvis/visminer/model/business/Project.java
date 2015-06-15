package br.edu.ufba.softvis.visminer.model.business;

import java.util.ArrayList;
import java.util.List;

import br.edu.ufba.softvis.visminer.persistence.PersistenceInterface;

public class Project {

	private List<Committer> committers;
	private List<Commit> commits;
	private List<File> files;
	private List<SoftwareUnit> softwareUnits;
	private Tree currentTree;
	private Repository repository;
	
	// Auxiliary properties
	private int currentCommit;
	private int committerContribution[];
	
	public Project(){}
	
	public Project(Repository repository){
		this.repository = repository;
	}
	
	public boolean nextCommit(){
		
		PersistenceInterface persistence = new PersistenceInterface();
		
		if(currentCommit == (commits.size() - 1)){
			return false;
		}
		
		currentCommit++;
		softwareUnits = persistence.findSoftwareUnitByRepository(repository.getId(), currentCommit);
		
		Commit commit = commits.get(currentCommit);
		committersUpdateHelper(commit, true);
		
		updateFiles(commit.getCommitedFiles());
		persistence.close();
		readSoftwareUnitTree(softwareUnits);
		
		return true;
		
	}
	
	public boolean prevCommit(){
		
		PersistenceInterface persistence = new PersistenceInterface();
		
		if(currentCommit == 0){
			return false;
		}
		
		currentCommit--;
		softwareUnits = persistence.findSoftwareUnitByRepository(repository.getId(), currentCommit);

		Commit commit = commits.get(currentCommit+1);
		committersUpdateHelper(commit, false);
		
		files = new ArrayList<File>();
		for(int i = 0; i <= currentCommit; i++){
			updateFiles(commits.get(i).getCommitedFiles());
		}
		
		persistence.close();
		readSoftwareUnitTree(softwareUnits);
		
		return true;
		
	}
	
	public boolean setCurrentCommit(Commit commit){
		
		PersistenceInterface persistence = new PersistenceInterface();
		
		int index = commits.indexOf(commit);
		if(index == -1){
			return false;
		}
		
		if((currentCommit - index) < 0){

			softwareUnits = persistence.findSoftwareUnitByRepository(repository.getId(), index);	
			for(int i = currentCommit+1; i <= index; i++){
				Commit commit2 = commits.get(i);
				committersUpdateHelper(commit2, true);
				updateFiles(commit.getCommitedFiles());
			}
			currentCommit = index;
			
		}else if((currentCommit - index) > 0){
			
			softwareUnits = persistence.findSoftwareUnitByRepository(repository.getId(), index);
			for(int i = currentCommit; i > index; i--){
				Commit commit2 = commits.get(i);
				committersUpdateHelper(commit2, false);
			}
			
			currentCommit = index;
			files = new ArrayList<File>();
			for(int i = 0; i <= currentCommit; i++){
				updateFiles(commits.get(i).getCommitedFiles());
			}
			
		}
		persistence.close();
		readSoftwareUnitTree(softwareUnits);
		return true;
		
	}

	public Commit getCurrentCommit(){
		return commits.get(currentCommit);
	}
	
	public void setCurrentTree(Tree currentTree) {
		this.currentTree = currentTree;
		resetProject();
	}
	
	// helpers
	
	private void updateFiles(List<File> files2){
		
		for(File file : files2){
			if(!files.contains(file)){
				files.add(file);
			}else{
				files.remove(file);
				if(!file.getFileState().isRemoved()){
					files.add(file);
				}
			}
		}
		
	}

	private void committersUpdateHelper(Commit commit, boolean forward){
		
		if(forward){
			
			if(!committers.contains(commit.getCommitter())){
				committers.add(commit.getCommitter());
				committerContribution[committers.size() - 1] = 1;
			}else{
				int index = committers.indexOf(commit.getCommitter());
				committerContribution[index] += 1; 
			}
			
		}else{
			
			int index = committers.indexOf(commit.getCommitter());
			committerContribution[index] -= 1;
			if(committerContribution[index] == 0){
				committers.remove(index);
			}
			
		}
		
	}

	private void resetProject(){

		PersistenceInterface persistence = new PersistenceInterface();
		
		committerContribution = new int[repository.getCommitters().size()];
		commits = persistence.findCommitsByTree(currentTree.getId());
		currentCommit = commits.size() - 1;
		softwareUnits = persistence.findSoftwareUnitByRepository(repository.getId(), currentCommit);
		
		committers = new ArrayList<Committer>();
		files = new ArrayList<File>();
		
		for(Commit commit : commits){
			committersUpdateHelper(commit, true);
			updateFiles(commit.getCommitedFiles());
		}
		readSoftwareUnitTree(softwareUnits);
		persistence.close();
		
	}
	
	private void readSoftwareUnitTree(List<SoftwareUnit> list){
		
		if(list == null){
			return;
		}
		
		for(int i = 0; i < list.size(); i++){
			SoftwareUnit elem = list.get(i);
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
				readSoftwareUnitTree(list.get(i).getChildren());
			}
		}
		
	}
	
	// getters and setters
	
	public List<Committer> getCommitters() {
		return committers;
	}
	public void setCommitters(List<Committer> committers) {
		this.committers = committers;
	}
	public List<Commit> getCommits() {
		return commits;
	}
	public void setCommits(List<Commit> commits) {
		this.commits = commits;
	}
	public List<File> getFiles() {
		return files;
	}
	public void setFiles(List<File> files) {
		this.files = files;
	}
	public List<SoftwareUnit> getSoftwareUnits() {
		return softwareUnits;
	}
	public void setSoftwareUnits(List<SoftwareUnit> softwareUnits) {
		this.softwareUnits = softwareUnits;
	}
	public Tree getCurrentTree() {
		return currentTree;
	}

	public Repository getRepository() {
		return repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}
}