package org.repositoryminer.mining;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.exceptions.ErrorMessage;
import org.repositoryminer.exceptions.VisMinerAPIException;
import org.repositoryminer.listener.IHostServiceListener;
import org.repositoryminer.model.Contributor;
import org.repositoryminer.model.Issue;
import org.repositoryminer.model.Milestone;
import org.repositoryminer.persistence.handler.IssueDocumentHandler;
import org.repositoryminer.persistence.handler.MilestoneDocumentHandler;
import org.repositoryminer.persistence.handler.RepositoryDocumentHandler;
import org.repositoryminer.scm.hostingservice.HostingServiceFactory;
import org.repositoryminer.scm.hostingservice.IHostingService;

// TODO: Needs documentation
public class HostingProcessor {
	
	private IHostingService service;
	private IHostServiceListener listener;
	private String repositoryId;
	
	public void connectToService(HostingServiceMiner hostingServiceMiner, String login, String password) {
		init(hostingServiceMiner);
		service = HostingServiceFactory.getHostingService(hostingServiceMiner.getServiceType());
		service.connect(hostingServiceMiner, login, password);
	}
	
	public void connectToService(HostingServiceMiner hostingServiceMiner, String token) {
		init(hostingServiceMiner);
		service = HostingServiceFactory.getHostingService(hostingServiceMiner.getServiceType());
		service.connect(hostingServiceMiner, token);
	}
	
	public void mine() {
		RepositoryDocumentHandler repoDocHandler = new RepositoryDocumentHandler();
		if (!repoDocHandler.checkIfRepositoryExistsById(repositoryId)) {
			throw new VisMinerAPIException(ErrorMessage.REPOSITORY_NOT_FOUND.toString());
		}

		processContributors(repoDocHandler);

		List<Issue> issues = service.getAllIssues();
		List<Milestone> milestones = service.getAllMilestones();

		connectMilestonesAndIssues(milestones, issues);
		processMilestones(milestones);
		processIssues(issues);
	}

	private void init(HostingServiceMiner hostingServiceMiner) {
		listener = hostingServiceMiner.getListener();
		repositoryId = hostingServiceMiner.getRepositoryId();
	}
	
	@SuppressWarnings("unchecked")
	private void processContributors(RepositoryDocumentHandler repoDocHandler) {
		Document repositoryDoc = repoDocHandler.findOnlyContributors(repositoryId);
		List<Document> contributorsDoc = (List<Document>) repositoryDoc.get("contributors");

		List<Contributor> contributors = service.getAllContributors();

		int contributorIndex = 0;
		for (Document contributorDoc : contributorsDoc) {
			if (listener != null) {
				listener.contributorsProgressChange(++contributorIndex, contributorsDoc.size());
			}

			String name = contributorDoc.getString("name");
			for (Contributor contributorDb : contributors) {
				if (name.equals(contributorDb.getName())) {
					contributorDoc.put("login", contributorDb.getLogin());
					contributorDoc.put("avatar_url", contributorDb.getAvatarUrl());
					contributorDoc.put("collaborator", contributorDb.isCollaborator());
					break;
				}
			}
		}

		repoDocHandler.updateOnlyContributors(repositoryDoc);
	}

	private void connectMilestonesAndIssues(List<Milestone> milestones, List<Issue> issues) {
		// connect issues to milestones
		if (milestones.size() > 0) {
			if (listener != null) {
				listener.initMilestonesIssuesConnection();
			}

			for (Milestone m : milestones) {
				m.setIssues(new ArrayList<Integer>());
				for (Issue i : issues) {
					if (i.getMilestone() == m.getNumber()) {
						m.getIssues().add(i.getNumber());
					}
				}
			}
		}
	}

	private void processIssues(List<Issue> issues) {
		IssueDocumentHandler issueDocHandler = new IssueDocumentHandler();
		List<Document> issuesDocs = new ArrayList<Document>(issues.size());

		issueDocHandler.deleteByRepository(repositoryId);

		if (issues.size() > 0) {
			int issuesIndex = 0;
			for (Issue issue : issues) {
				if (listener != null) {
					listener.issuesProgressChange(++issuesIndex, issues.size());
				}
				issue.setRepository(repositoryId);
				issuesDocs.add(issue.toDocument());
			}
			issueDocHandler.insertMany(issuesDocs);
		}
	}

	private void processMilestones(List<Milestone> milestones) {
		MilestoneDocumentHandler mileDocHandler = new MilestoneDocumentHandler();
		List<Document> milesDocs = new ArrayList<Document>(milestones.size());

		mileDocHandler.deleteByRepository(repositoryId);

		if (milestones.size() > 0) {
			int milestonesIndex = 0;
			for (Milestone mile : milestones) {
				if (listener != null) {
					listener.milestonesProgressChange(++milestonesIndex, milestones.size());
				}
				mile.setRepository(repositoryId);
				milesDocs.add(mile.toDocument());
			}
			mileDocHandler.insertMany(milesDocs);
		}
	}

}
