package br.edu.ufba.softvis.visminer.analyzer.remote;

import br.edu.ufba.softvis.visminer.constant.RepositoryServiceType;

public class SupportedService {

	public static IRepositoryService getRepositoryService(RepositoryServiceType type){
		
		switch(type){
			case GITHUB : return new GitHubRepositoryService();
			default : return null;
		}
		
	}
	
}
