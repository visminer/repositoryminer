package org.repositoryminer.exceptions;

public class VisMinerAPIException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public VisMinerAPIException(String message){
		super(message);
	}

	public VisMinerAPIException(String message, Throwable cause){
		super(message, cause);
	}
	
}
