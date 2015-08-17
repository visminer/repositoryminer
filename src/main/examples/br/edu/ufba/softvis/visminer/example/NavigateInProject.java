package br.edu.ufba.softvis.visminer.example;

import java.util.List;

import br.edu.ufba.softvis.visminer.main.VisMiner;
import br.edu.ufba.softvis.visminer.model.business.Commit;
import br.edu.ufba.softvis.visminer.model.business.Committer;
import br.edu.ufba.softvis.visminer.model.business.Metric;
import br.edu.ufba.softvis.visminer.model.business.Repository;
import br.edu.ufba.softvis.visminer.model.business.Tree;
import br.edu.ufba.softvis.visminer.retriever.MetricRetriever;
import br.edu.ufba.softvis.visminer.retriever.RepositoryRetriever;

// This example shows how to get navigate in the project inside the repository.
public class NavigateInProject {

	private Repository repository;
	
	public static void main(String[] args) {
		
		VisMiner visminer = new VisMiner();
		visminer.setDBConfig(DatabaseConfig.getDBConfig());
		NavigateInProject main = new NavigateInProject();
		
		PersistRepository persistRepo = new PersistRepository();
		if(!visminer.isRepositoryPersisted(persistRepo.getRepositoryPath())){
			persistRepo.persistRepository();
		}
		
		RepositoryRetriever retriever = new RepositoryRetriever();
		main.repository = retriever.retrieveByPath(persistRepo.getRepositoryPath());
		
		main.listCommitters();
		main.listMetrics();
		main.listCommitts("master");

		main.changeTree("0.8.0");
		Commit c = main.repository.getProject().getCommits().get(10);
		main.listCommitters(c);
		
		//navigation through the repository. Now we are navigating through commits in tag 0.8.0.
		main.repository.getProject().nextCommit();
		main.repository.getProject().prevCommit();
		main.repository.getProject().setCurrentCommit(null);
		// These methods return true if is possible to access the commit or false otherwise.
		
		main.repository.getProject().firstCommit();
		main.repository.getProject().lastCommit();
		
	}
	
	// lists commits in certain tree
	private List<Commit> listCommitts(String treeName) {
		
		List<Commit> commits = null;
		for(Tree t : repository.getTrees()){
			if(t.getName().equals(treeName)){
				repository.getProject().setCurrentTree(t);
				commits = repository.getProject().getCommits();
			}
		}
		
		return commits;
		
	}

	//get all metrics
	private List<Metric> listMetrics() {
		MetricRetriever retriever = new MetricRetriever();
		return retriever.findAll();
	}

	// get all committers
	private List<Committer> listCommitters() {
		return repository.getCommitters();
	}
	
	// Committers until this commit
	private List<Committer> listCommitters(Commit commit) {
		repository.getProject().setCurrentCommit(commit);
		return repository.getProject().getCommitters();
	}

	// Change the tree(tag or branch) using his name
	private void changeTree(String treeName){
		for(Tree t : repository.getTrees()){
			if(t.getName().equals(treeName)){
				repository.getProject().setCurrentTree(t);
			}
		}
	}	
	
}
