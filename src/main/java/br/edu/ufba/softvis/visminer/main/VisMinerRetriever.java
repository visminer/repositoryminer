package br.edu.ufba.softvis.visminer.main;

import java.io.File;
import java.util.List;

import br.edu.ufba.softvis.visminer.model.business.Committer;
import br.edu.ufba.softvis.visminer.model.business.Metric;
import br.edu.ufba.softvis.visminer.model.business.Project;
import br.edu.ufba.softvis.visminer.model.business.Repository;
import br.edu.ufba.softvis.visminer.model.business.Tree;
import br.edu.ufba.softvis.visminer.persistence.PersistenceInterface;

public class VisMinerRetriever {

	public Repository findRepository(String repositoryPath){
	
		File file = new File(repositoryPath);
		String absolutePath = file.getAbsolutePath().replace("\\", "/");
		
		PersistenceInterface persistence = new PersistenceInterface();
		Repository repository = persistence.findRepository(absolutePath);
		
		List<Tree> trees = persistence.findTrees(repository.getId());
		List<Committer> committers = persistence.findCommitters(repository.getId());
		
		repository.setCommitters(committers);
		repository.setTrees(trees);
		
		Project project = new Project(repository);
		for(Tree t : trees){
			if(t.getName().equals("master") || t.getName().equals("head")){
				project.setCurrentTree(t);
			}
		}
		
		if(project.getCurrentTree() == null){
			project.setCurrentTree(trees.get(0));
		}
		
		repository.setProject(project);
		persistence.close();
		return repository;
		
	}
	
	public List<Metric> findMetrics(){
		PersistenceInterface persistence = new PersistenceInterface();
		List<Metric> metrics = persistence.findAllMetrics();
		persistence.close();
		return metrics;
	}
	
}
