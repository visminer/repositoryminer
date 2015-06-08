package br.edu.ufba.softvis.visminer.analyzer.local;

import br.edu.ufba.softvis.visminer.constant.RepositoryType;

public class SupportedRepository {

	public static IRepositorySystem getRepository(RepositoryType type){

		switch(type){
			case GIT : return new GitRepositorySystem();
			default : return null;
		}
		
	}
	
}