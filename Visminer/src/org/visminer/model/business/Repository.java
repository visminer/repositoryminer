package org.visminer.model.business;

import java.util.ArrayList;
import java.util.List;

import org.visminer.constant.TreeType;
import org.visminer.persistence.PersistenceFacade;

public class Repository {

	private int id;
	private String path;
	private String name;
	private String remote_owner;
	private String remote_name;
	private int type;
	private int remoteType;
	
	private List<Committer> committers;
	private List<Commit> commits;
	private List<Tree> trees;
	private List<File> files;
	
	private int currentCommitIndex;
	private Commit currentCommit;
	private Tree currentTree;
	
	private PersistenceFacade persistence;
	
	public void start(){
		
		persistence = new PersistenceFacade();
		
		List<org.visminer.model.database.Tree> treesDb = persistence.getTreesByRepository(id);
		List<org.visminer.model.database.Committer> committersDb = persistence.getCommittersByRepository(id);
		
		this.trees = new ArrayList<Tree>(treesDb.size());
		this.committers = new ArrayList<Committer>(committersDb.size());
		
		for(org.visminer.model.database.Tree t : treesDb){
			Tree tree = new Tree(t.getId(), t.getName(), t.getFullName(), t.getType());
			trees.add(tree);
		}
		
		for(org.visminer.model.database.Committer c : committersDb){
			Committer committer = new Committer(c.getId(), c.getName(), c.getEmail());
			committers.add(committer);
		}
		
		setTree("master", TreeType.BRANCH);
		
	}
	
	public boolean setTree(String name, int type){
		
		for(Tree t : this.trees){
			if(t.getName().equals(name) && t.getType() ==  type){
				this.currentTree = t;
				updateTree();
				return true;
			}
		}
		
		return false;
		
	}
	
	public boolean previousCommit(){
		
		if(this.currentCommitIndex == 1) return false;
		this.currentCommitIndex--;
		this.currentCommit = this.commits.get(currentCommitIndex - 1);
		updateProject();
		return true;
		
	}
	
	public boolean nextCommit(){
		
		if(this.currentCommitIndex == this.commits.size()) return false;
		this.currentCommitIndex++;
		this.currentCommit = this.commits.get(currentCommitIndex - 1);
		updateProject();
		return true;
			
	}
	
	public void firstCommit(){
		this.currentCommitIndex = 1;
		this.currentCommit = this.commits.get(currentCommitIndex - 1);
		updateProject();
	}
	
	public void lastCommit(){
		this.currentCommitIndex = this.commits.size();
		this.currentCommit = this.commits.get(currentCommitIndex - 1);
		updateProject();
	}
	
	private void updateTree(){
		
		List<org.visminer.model.database.Commit> commitsDb =  persistence.getCommitsByTree(currentTree.getId());
		this.commits = new ArrayList<Commit>(commitsDb.size());
		
		for(org.visminer.model.database.Commit c : commitsDb){
			Commit commit = new Commit(c.getId(), c.getName(), c.getMessage(), c.getDate());
			commits.add(commit);
		}
		
		currentCommitIndex = commits.size();
		currentCommit = commits.get(currentCommitIndex - 1);
		updateProject();
		
	}
	
	private void updateProject(){
		
		files = new ArrayList<File>();
		for(int i = 0; i < currentCommitIndex; i++){
			for(File f : this.commits.get(i).getFiles()){
				if(this.files.contains(f) && f.isDeleted())
					files.remove(f);
				else if(!this.files.contains(f))
					this.files.add(f);
			}
		}
		
	}
	
	public Repository(){}	

	public Repository(int id, String path, String name, String remote_owner,
			String remote_name, int type, int remoteType) {
		super();
		this.id = id;
		this.path = path;
		this.name = name;
		this.remote_owner = remote_owner;
		this.remote_name = remote_name;
		this.type = type;
		this.remoteType = remoteType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemote_owner() {
		return remote_owner;
	}

	public void setRemote_owner(String remote_owner) {
		this.remote_owner = remote_owner;
	}

	public String getRemote_name() {
		return remote_name;
	}

	public void setRemote_name(String remote_name) {
		this.remote_name = remote_name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getRemoteType() {
		return remoteType;
	}

	public void setRemoteType(int remoteType) {
		this.remoteType = remoteType;
	}

	public List<Committer> getCommitters() {
		return committers;
	}

	public List<Commit> getCommits() {
		return commits;
	}

	public List<Tree> getTrees() {
		return trees;
	}

	public List<File> getFiles() {
		return files;
	}

	public void setFiles(List<File> files) {
		this.files = files;
	}

	public Commit getCurrentCommit() {
		return currentCommit;
	}

	public void setCurrentCommit(Commit currentCommit) {
		this.currentCommit = currentCommit;
		updateProject();
	}

	public Tree getCurrentTree() {
		return currentTree;
	}

	public void setCurrentTree(Tree currentTree) {
		this.currentTree = currentTree;
		updateTree();
	}

}
