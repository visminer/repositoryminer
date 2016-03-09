package br.edu.ufba.softvis.visminer.analyzer.scm;

import java.io.IOException;

import br.edu.ufba.softvis.visminer.constant.SCMType;

/**
 * Manage all the repositories and web repositories supported by VisMiner.
 */

public class SCMFactory {

	/**
	 * @param repositoryType
	 * @return Instance of the class that manage the repository. 
	 */
	public static SCM getRepository(SCMType repositoryType){

		switch(repositoryType){
			case GIT : return checkGIT();
			default : return null;
		}
		
	}
	
	//Checks if git is installed, if yes git is used, otherwise jgit is used.
	private static SCM checkGIT(){
		
		try {
			Runtime.getRuntime().exec("git --version");
			return new GitRepository();
		} catch (IOException e) {
			return new JGitRepository();
		}
		
	}
	
}