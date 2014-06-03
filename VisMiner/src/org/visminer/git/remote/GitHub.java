package org.visminer.git.remote;

import java.io.IOException;

public class GitHub implements Connection{
	
	private org.kohsuke.github.GHRepository ghrConnection = null;

		
	@Override
	public void makeConnection(String login, String password, String idGit) {
		
		try {
			org.kohsuke.github.GitHub gh = org.kohsuke.github.GitHub.connectUsingPassword(login, password);
			ghrConnection =  gh.getRepository(idGit);

		} catch (IOException e) {
			System.out.println("Problems in connection:\n" +
					"\n1-probable this repository doesn't exist:" +
					"\n   verify ownerRepository and nameRepository\n"+
					"\n2- probable login or password wrong\n");
		}
		
	}

	@Override
	public Object getConnection() {
		
		return ghrConnection;
	}
	

}
