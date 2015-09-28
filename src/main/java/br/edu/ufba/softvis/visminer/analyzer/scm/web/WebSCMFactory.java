package br.edu.ufba.softvis.visminer.analyzer.scm.web;

import br.edu.ufba.softvis.visminer.constant.WebSCMType;

public class WebSCMFactory {

	public static WebSCM getService(WebSCMType serviceType){
		
		switch(serviceType){
			case GITHUB: return new GitHubRepository();
			default: return null;
		}
		
	}
	
}
