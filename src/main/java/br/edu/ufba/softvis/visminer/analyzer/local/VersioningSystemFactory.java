package br.edu.ufba.softvis.visminer.analyzer.local;

import br.edu.ufba.softvis.visminer.constant.VersioningSystemType;

/**
 * @version 0.9
 * Manage all the repositories and web repositories supported by VisMiner.
 */

public class VersioningSystemFactory {

	/**
	 * @param repositoryType
	 * @return Instance of the class that manage the repository. 
	 */
	public static IVersioningSystem getRepository(VersioningSystemType repositoryType){

		switch(repositoryType){
			case GIT : return new GitSystem();
			default : return null;
		}
		
	}
	
}