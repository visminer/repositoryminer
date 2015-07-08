package br.edu.ufba.softvis.visminer.analyzer.local;

import br.edu.ufba.softvis.visminer.constant.RepositoryType;

/**
 * @version 0.9
 * Manage all the repositories and web repositories supported by VisMiner.
 */

public class SupportedRepository {

	/**
	 * @param repositoryPath
	 * @param repositoryType
	 * @return Instance of the class that manage the repository. 
	 */
	public static IRepositorySystem getRepository(String repositoryPath, RepositoryType repositoryType){

		switch(repositoryType){
			case GIT : return new GitRepository(repositoryPath);
			default : return null;
		}
		
	}
	
}