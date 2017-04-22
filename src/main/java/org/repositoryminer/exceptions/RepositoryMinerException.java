package org.repositoryminer.exceptions;

public class RepositoryMinerException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public RepositoryMinerException(String message){
		super(message);
	}

	public RepositoryMinerException(String message, Throwable cause){
		super(message, cause);
	}
	
}
