package br.edu.ufba.softvis.visminer.main;

import java.io.File;
import java.util.List;

import br.edu.ufba.softvis.visminer.model.business.Committer;
import br.edu.ufba.softvis.visminer.model.business.Metric;
import br.edu.ufba.softvis.visminer.model.business.Project;
import br.edu.ufba.softvis.visminer.model.business.Repository;
import br.edu.ufba.softvis.visminer.model.business.Tree;
import br.edu.ufba.softvis.visminer.persistence.PersistenceInterface;

/**
 * @version 0.9
 * 
 * Provides informations queries in database.
 */
public class VisMinerRetriever {

	/**
	 * @param repositoryPath
	 * @return Repository ready for access all its data.
	 */
	public Repository findRepository(String repositoryPath){
	
		File file = new File(repositoryPath);
		String absolutePath = file.getAbsolutePath().replace("\\", "/");
		
		PersistenceInterface persistence = new PersistenceInterface();
		Repository repository = persistence.findRepository(absolutePath);
		
		if(repository == null){
			return null;
		}
		
		List<Tree> trees = persistence.findTrees(repository.getId());
		List<Committer> committers = persistence.findCommitters(repository.getId());
		
		repository.setCommitters(committers);
		repository.setTrees(trees);
		
		Project project = new Project(repository);
		for(Tree t : trees){
			String s = t.getName().toLowerCase();
			if(s.equals("master") || s.equals("head")){
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
	
	/**
	 * @return List of supported metrics.
	 */
	public List<Metric> findMetrics(){
		PersistenceInterface persistence = new PersistenceInterface();
		List<Metric> metrics = persistence.findAllMetrics();
		persistence.close();
		return metrics;
	}
	
}
