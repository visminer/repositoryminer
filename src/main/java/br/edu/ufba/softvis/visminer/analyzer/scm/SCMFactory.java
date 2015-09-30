package br.edu.ufba.softvis.visminer.analyzer.scm;

import br.edu.ufba.softvis.visminer.constant.SCMType;

/**
 * @version 0.9
 * Manage all the repositories and web repositories supported by VisMiner.
 */

public class SCMFactory {

	/**
	 * @param repositoryType
	 * @return Instance of the class that manage the repository. 
	 */
	public static SCM getRepository(SCMType repositoryType){

		switch(repositoryType){
			case GIT : return new GitRepository();
			default : return null;
		}
		
	}
	
}