package org.repositoryminer.web.scm.hostingservice;

import java.util.List;

import org.repositoryminer.web.scm.WebSCMConfig;
import org.repositoryminer.web.scm.model.Issue;
import org.repositoryminer.web.scm.model.Milestone;

public interface IHostingService {

	/**
	 * Initialize connection with web service using login and a security token or a
	 * password.
	 * 
	 * @param config
	 */
	public void connect(WebSCMConfig config);

	/**
	 * @return All issues from web repository service.
	 */
	public List<Issue> getAllIssues();

	/**
	 * @return All milestones from web repository service.
	 */
	public List<Milestone> getAllMilestones();

}