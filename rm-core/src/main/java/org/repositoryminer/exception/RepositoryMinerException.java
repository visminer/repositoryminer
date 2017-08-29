package org.repositoryminer.exception;

/**
 * This class defines the API exception. 
 */
public class RepositoryMinerException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public RepositoryMinerException(ErrorMessage message) {
		super(message.toString());
	}
	
	public RepositoryMinerException(String message){
		super(message);
	}

	public RepositoryMinerException(String message, Throwable cause){
		super(message, cause);
	}
	
}
