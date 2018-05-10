package org.repositoryminer.web.scm;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.plugin.MiningPlugin;
import org.repositoryminer.web.scm.hostingservice.HostingServiceFactory;
import org.repositoryminer.web.scm.hostingservice.IHostingService;
import org.repositoryminer.web.scm.model.Issue;
import org.repositoryminer.web.scm.model.Milestone;
import org.repositoryminer.web.scm.persistence.IssueDAO;
import org.repositoryminer.web.scm.persistence.MilestoneDAO;

public class RepositoryMinerWebSCM extends MiningPlugin<WebSCMConfig>{

	private IHostingService service;
	
	public RepositoryMinerWebSCM(String repositoryKey) {
		super(repositoryKey);
	}

	@Override
	public void mine(WebSCMConfig config) {
		service = HostingServiceFactory.getHostingService(config.getServiceType());
		service.connect(config);
		
		List<Issue> issues = service.getAllIssues();
		List<Milestone> milestones = service.getAllMilestones();

		processMilestones(milestones, issues);
		processIssues(issues);
	}
	
	private void processMilestones(List<Milestone> milestones, List<Issue> issues) {
		MilestoneDAO mileDocHandler = new MilestoneDAO();
		List<Document> milesDocs = new ArrayList<Document>(milestones.size());

		if (milestones.size() > 0) {
			for (Milestone mile : milestones) {
				mile.setIssues(new ArrayList<Integer>());
				for (Issue i : issues) {
					if (i.getMilestone() == mile.getNumber()) {
						mile.getIssues().add(i.getNumber());
					}
				}

				mile.setRepository(repository.getId());
				milesDocs.add(mile.toDocument());
			}

			mileDocHandler.insertMany(milesDocs);
		}
	}

	private void processIssues(List<Issue> issues) {
		IssueDAO issueDocHandler = new IssueDAO();
		List<Document> issuesDocs = new ArrayList<Document>(issues.size());

		if (issues.size() > 0) {
			for (Issue issue : issues) {
				issue.setRepository(repository.getId());
				issuesDocs.add(issue.toDocument());
			}
			issueDocHandler.insertMany(issuesDocs);
		}

	}

}