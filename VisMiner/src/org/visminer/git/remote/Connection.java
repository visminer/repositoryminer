package org.visminer.git.remote;

/**
 * Utilized for connecting to a specified repository git.
 *
 */
public interface Connection {
	
	public void makeConnection(String login, String password, String idGit);
	
	/**
	 * Return a object to the specified repository git.
	 */
	public Object getConnection();

}
