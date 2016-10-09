package org.repositoryminer.scm.hostingservice;

import java.util.List;

import org.repositoryminer.mining.HostingServiceMiner;
import org.repositoryminer.model.Contributor;
import org.repositoryminer.model.Issue;
import org.repositoryminer.model.Milestone;

public interface IHostingService {

	/**
	 * @param hostingMiner
	 * @param login
	 * @param password
	 * 
	 * Initialize connection with web service using login and password.
	 */
	public void connect(HostingServiceMiner hostingMiner, String login, String password);
	
	/**
	 * @param hostingMiner
	 * @param token
	 * 
	 * Initialize connection with web service using token.
	 */
	public void connect(HostingServiceMiner hostingMiner, String token);
	
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