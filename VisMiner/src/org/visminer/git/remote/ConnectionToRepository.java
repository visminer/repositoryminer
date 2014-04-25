package org.visminer.git.remote;

import java.io.IOException;

import org.kohsuke.github.GitHub;

public class ConnectionToRepository {
	
	private String ownerRepository;	
	private String nameRepository;
	private NameRepositories nameRepRemote;
	
	
	public ConnectionToRepository(String ownerRepository, String nameRepository, NameRepositories nameRepRemote){
		
		this.ownerRepository = ownerRepository;
		this.nameRepository = nameRepository;
		this.nameRepRemote = nameRepRemote;
		
	}
	
	
	public Object getConnection(String login, String password){
		
		if(nameRepRemote == NameRepositories.GITHUB){
			
			try {
				GitHub gh = GitHub.connectUsingPassword(login, password);
				return gh.getRepository(ownerRepository+ "/"+ nameRepository);

			} catch (IOException e) {
				System.out.println("Problems in connection:\n1-probable this repository doesn't exist" +
						"\n2-login or password wrong\n");
			}
		
		}
		return null;
		//TODO make connection to repository BITBUCKET
		
	}


	public NameRepositories getNameRepRemote() {
		return nameRepRemote;
	}
	
	

}
