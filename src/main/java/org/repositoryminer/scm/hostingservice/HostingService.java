package org.repositoryminer.scm.hostingservice;

import java.util.List;

import org.repositoryminer.persistence.model.IssueDB;
import org.repositoryminer.persistence.model.MilestoneDB;

public interface HostingService {

	/**
	 * @param owner
	 * @param name
	 * @param user
	 * @param password
	 * Initialize connection with web service using user and password.
	 */
	public void connect(String owner, String name, String user, String password);
	
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
	public List<IssueDB> getAllIssues();
	
	/**
	 * @return All milestones from web repository service.
	 */
	public List<MilestoneDB> getAllMilestones();
	
}