package org.repositoryminer.remoteminer.hostingservice;

import java.util.List;

import org.repositoryminer.remoteminer.mining.RemoteRepositoryMiner;
import org.repositoryminer.model.Contributor;
import org.repositoryminer.remoteminer.model.Issue;
import org.repositoryminer.remoteminer.model.Milestone;

public interface IHostingService {

	/**
	 * @param hostingMiner
	 * @param login
	 * @param password
	 * 
	 * Initialize connection with web service using login and password.
	 */
	public void connect(RemoteRepositoryMiner hostingMiner, String login, String password);
	
	/**
	 * @param hostingMiner
	 * @param token
	 * 
	 * Initialize connection with web service using token.
	 */
	public void connect(RemoteRepositoryMiner hostingMiner, String token);
	
	/**
	 * @return All issues from web repository service.
	 */
	public List<Issue> getAllIssues();
	
	/**
	 * @return All milestones from web repository service.
	 */
	public List<Milestone> getAllMilestones();
	
	/**
	 * @return All contributors from a repository.
	 */
	public List<Contributor> getAllContributors();
	
}