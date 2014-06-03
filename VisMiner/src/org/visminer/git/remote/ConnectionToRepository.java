package org.visminer.git.remote;

public class ConnectionToRepository {
	
	private String idGit;
	private Connection repRemote;
	
	
	public ConnectionToRepository(String ownerRepository, String nameRepository, Connection repRemote){
		
		this.idGit = ownerRepository+ "/"+ nameRepository;
		this.repRemote = repRemote;
		
	}
	
	
	public Object getConnection(String login, String password){
		
		repRemote.makeConnection(login, password, idGit);
		return repRemote.getConnection();
		
	}

}
