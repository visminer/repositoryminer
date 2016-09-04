package org.repositoryminer.scm.hostingservice;

import java.util.List;

import org.repositoryminer.model.Contributor;
import org.repositoryminer.model.Issue;
import org.repositoryminer.model.Milestone;

public interface HostingService {

	/**
	 * @param owner
	 * @param name
	 * @param login
	 * @param password
	 * Initialize connection with web service using login and password.
	 */
	public void connect(String owner, String name, String login, String password);
	
	/**
	 * @param owner
	 * @param name
	 * @param token
	 * Initialize connection with web service using token.
	 */
	public void connect(String owner, String name, String token);
	
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