package org.repositoryminer.exceptions;

public enum ErrorMessage {

	AST_GENERATOR_FACTORY_ERROR("It was impossible to create an instance of an ASTGenerator."),
	GIT_BRANCH_LIST_ERROR("GIT - It was impossible to list the branches."), 
	GIT_TAG_LIST_ERROR("GIT - It was impossible to list the tags."),
	GIT_LOG_COMMIT_ERROR("GIT - It was impossible to list the commits."),
	GIT_RETRIEVE_CHANGES_ERROR("GIT - It was impossibe to retieve the changes made in one commit."),
	GIT_CHECKOUT_ERROR("GIT - It was impossible make checkout."), 
	GIT_RESET_ERROR("GIT - It was impossible to reset the repository."), 
	GIT_BRANCH_COMMITS_ERROR("GIT - It was impossible to retrieve the commits of a branch."),
	GIT_TAG_COMMITS_ERROR("GIT - It was impossible to retrieve the commits of a tag."),
	GIT_RETRIEVE_DATA_ERROR("GIT - It was impossible to retrieve the data from a file."),
	GIT_REPOSITORY_IOERROR("GIT - It was impossible to analyze the repository, some unexpected IO error has ocurred."),
	SCM_NOT_FOUND("Not found SCM system."),
	DUPLICATE_REPOSITORY("Repository already exists in database."),
	REPOSITORY_NOT_FOUND("Repository not found.");
	
	private String message;
	
	private ErrorMessage(String message){
		this.message = message;
	}
	
	@Override
	public String toString(){
		return this.message;
	}
	
}