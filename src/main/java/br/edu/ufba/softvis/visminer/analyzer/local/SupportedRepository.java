package br.edu.ufba.softvis.visminer.analyzer.local;

import br.edu.ufba.softvis.visminer.constant.RepositoryType;

/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * Manage all the repositories and web repositories supported by VisMiner.
 */

public class SupportedRepository {

	/**
	 * @param repositoryType
	 * @return Instance of the class that manage the repository. 
	 */
	public static IRepositorySystem getRepository(RepositoryType repositoryType){

		switch(repositoryType){
			case GIT : return new GitRepository();
			default : return null;
		}
		
	}
	
}