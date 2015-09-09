package br.edu.ufba.softvis.visminer.example;


import java.util.List;

import br.edu.ufba.softvis.visminer.main.VisMiner;
import br.edu.ufba.softvis.visminer.model.business.Commit;
import br.edu.ufba.softvis.visminer.retriever.CommitRetriever;

//This example shows how to list all commits in a repository.
public class ListAllCommitsFromRepository {

	public static void main(String[] args) {
		
		VisMiner visminer = new VisMiner();
		visminer.setDBConfig(DatabaseConfig.getDBConfig());
		
		PersistRepository persistRepo = new PersistRepository();
		
		CommitRetriever retriever = new CommitRetriever();
		List<Commit> commits = retriever.retrieveByRepository(persistRepo.getRepositoryPath());
		
		for(Commit c : commits){
			System.out.println(c.getDate().toString());
			System.out.printf("Committer Name: %s\n", c.getCommitter().getName());
			System.out.printf("Committer Email: %s\n", c.getCommitter().getEmail());
			System.out.printf("Commit Name: %s\n", c.getName());
			System.out.printf("Number of files Commited: %d\n", c.getCommitedFiles().size());
		}
		
	}

}
