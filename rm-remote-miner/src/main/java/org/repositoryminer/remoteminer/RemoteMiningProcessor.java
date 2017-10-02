package org.repositoryminer.remoteminer;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.exception.ErrorMessage;
import org.repositoryminer.exception.RepositoryMinerException;
import org.repositoryminer.persistence.dao.RepositoryDAO;
import org.repositoryminer.remoteminer.hostingservice.HostingServiceFactory;
import org.repositoryminer.remoteminer.hostingservice.IHostingService;
import org.repositoryminer.remoteminer.model.Issue;
import org.repositoryminer.remoteminer.model.Milestone;
import org.repositoryminer.remoteminer.persistence.IssueDAO;
import org.repositoryminer.remoteminer.persistence.MilestoneDAO;

public class RemoteMiningProcessor {

	private IHostingService service;
	private String repositoryId;

	public void connectToService(RemoteRepositoryMiner hostingServiceMiner, String login, String password) {
		init(hostingServiceMiner);
		service.connect(hostingServiceMiner, login, password);
	}

	public void connectToService(RemoteRepositoryMiner hostingServiceMiner, String token) {
		init(hostingServiceMiner);
		service.connect(hostingServiceMiner, token);
	}

	private void init(RemoteRepositoryMiner hostingServiceMiner) {
		repositoryId = hostingServiceMiner.getRepositoryId();
		service = HostingServiceFactory.getHostingService(hostingServiceMiner.getServiceType());
	}

	public void mine() {
		RepositoryDAO repoDocHandler = new RepositoryDAO();
		if (!repoDocHandler.wasMined(repositoryId)) {
			throw new RepositoryMinerException(ErrorMessage.REPOSITORY_NOT_FOUND.toString());
		}

		List<Issue> issues = service.getAllIssues();
		List<Milestone> milestones = service.getAllMilestones();

		processMilestones(milestones, issues);
		processIssues(issues);
	}

	private void processMilestones(List<Milestone> milestones, List<Issue> issues) {
		MilestoneDAO mileDocHandler = new MilestoneDAO();
		List<Document> milesDocs = new ArrayList<Document>(milestones.size());

		mileDocHandler.deleteByRepository(repositoryId);

		if (milestones.size() > 0) {
			for (Milestone mile : milestones) {
				mile.setIssues(new ArrayList<Integer>());
				for (Issue i : issues) {
					if (i.getMilestone() == mile.getNumber()) {
						mile.getIssues().add(i.getNumber());
					}
				}

				mile.setRepository(repositoryId);
				milesDocs.add(mile.toDocument());
			}

			mileDocHandler.insertMany(milesDocs);
		}
	}

	private void processIssues(List<Issue> issues) {
		IssueDAO issueDocHandler = new IssueDAO();
		List<Document> issuesDocs = new ArrayList<Document>(issues.size());

		issueDocHandler.deleteByRepository(repositoryId);

		if (issues.size() > 0) {
			for (Issue issue : issues) {
				issue.setRepository(repositoryId);
				issuesDocs.add(issue.toDocument());
			}
			issueDocHandler.insertMany(issuesDocs);
		}

	}

}