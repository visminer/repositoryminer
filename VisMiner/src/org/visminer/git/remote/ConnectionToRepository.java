package org.visminer.git.remote;

import java.io.IOException;

import org.kohsuke.github.GitHub;

public class ConnectionToRepository {
	
	private String idGit;
	private NameRepositories nameRepRemote;
	
	
	public ConnectionToRepository(String ownerRepository, String nameRepository, NameRepositories nameRepRemote){
		
		this.idGit = ownerRepository+ "/"+ nameRepository;
		this.nameRepRemote = nameRepRemote;
		
	}
	
	
	public Object getConnection(String login, String password){
		
		if(nameRepRemote == NameRepositories.GITHUB){
			
			try {
				GitHub gh = GitHub.connectUsingPassword(login, password);
				return gh.getRepository(idGit);

			} catch (IOException e) {
				System.out.println("Problems in connection:\n" +
						"\n1-probable this repository doesn't exist:" +
						"\n   verify ownerRepository and nameRepository\n"+
						"\n2- probable login or password wrong\n");
			}
		
		}
		return null;
		//TODO make connection to repository BITBUCKET
		
	}


	public NameRepositories getNameRepRemote() {
		return nameRepRemote;
	}
	
	

}
