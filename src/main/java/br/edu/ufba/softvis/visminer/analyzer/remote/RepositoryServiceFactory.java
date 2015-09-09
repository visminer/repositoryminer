package br.edu.ufba.softvis.visminer.analyzer.remote;

import br.edu.ufba.softvis.visminer.constant.WebServiceType;

public class RepositoryServiceFactory {

	public static IRepositoryService getService(WebServiceType serviceType){
		
		switch(serviceType){
			case GITHUB: return new GitHubService();
			default: return null;
		}
		
	}
	
}
