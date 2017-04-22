package org.repositoryminer.mining.hosting;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.exceptions.ErrorMessage;
import org.repositoryminer.exceptions.RepositoryMinerException;
import org.repositoryminer.listener.servicemining.IServiceMiningListener;
import org.repositoryminer.model.Contributor;
import org.repositoryminer.model.Issue;
import org.repositoryminer.model.Milestone;
import org.repositoryminer.persistence.handler.IssueDocumentHandler;
import org.repositoryminer.persistence.handler.MilestoneDocumentHandler;
import org.repositoryminer.persistence.handler.RepositoryDocumentHandler;
import org.repositoryminer.scm.hostingservice.HostingServiceFactory;
import org.repositoryminer.scm.hostingservice.IHostingService;

public class HostingProcessor {

	private IHostingService service;
	private IServiceMiningListener listener;
	private HostingServiceMiner hostingServiceMiner;
	private String repositoryId;

	public void connectToService(HostingServiceMiner hostingServiceMiner, String login, String password) {
		init(hostingServiceMiner);
		service.connect(hostingServiceMiner, login, password);
	}

	public void connectToService(HostingServiceMiner hostingServiceMiner, String token) {
		init(hostingServiceMiner);
		service.connect(hostingServiceMiner, token);
	}

	private void init(HostingServiceMiner hostingServiceMiner) {
		this.hostingServiceMiner = hostingServiceMiner;
		repositoryId = hostingServiceMiner.getRepositoryId();
		service = HostingServiceFactory.getHostingService(hostingServiceMiner.getServiceType());
		listener = hostingServiceMiner.getServiceMiningListener();
	}

	public void mine() {
		RepositoryDocumentHandler repoDocHandler = new RepositoryDocumentHandler();
		if (!repoDocHandler.checkIfRepositoryExistsById(repositoryId)) {
			throw new RepositoryMinerException(ErrorMessage.REPOSITORY_NOT_FOUND.toString());
		}

		listener.notifyServiceMiningStart(hostingServiceMiner.getName());

		processContributors(repoDocHandler);

		List<Issue> issues = service.getAllIssues();
		List<Milestone> milestones = service.getAllMilestones();

		processMilestones(milestones, issues);
		processIssues(issues);

		listener.notifyServiceMiningEnd(hostingServiceMiner.getName());
	}

	@SuppressWarnings("unchecked")
	private void processContributors(RepositoryDocumentHandler repoDocHandler) {
		Document repositoryDoc = repoDocHandler.findOnlyContributors(repositoryId);
		List<Document> contributorsDoc = (List<Document>) repositoryDoc.get("contributors");

		List<Contributor> contributors = service.getAllContributors();

		listener.notifyContributorsMiningStart(contributors.size());
		int index = 1;

		for (Document contributorDoc : contributorsDoc) {
			String name = contributorDoc.getString("name");
			listener.notifyContributorsMiningProgress(name, index++, contributors.size());

			for (Contributor contributorDb : contributors) {
				if (name.equals(contributorDb.getName())) {
					contributorDoc.put("login", contributorDb.getLogin());
					contributorDoc.put("avatar_url", contributorDb.getAvatarUrl());
					contributorDoc.put("collaborator", contributorDb.isCollaborator());
					break;
				}
			}
		}

		repoDocHandler.updateOnlyContributors(repositoryId, contributorsDoc);
		listener.notifyContributorsMiningEnd(contributors.size());
	}

	private void processMilestones(List<Milestone> milestones, List<Issue> issues) {
		MilestoneDocumentHandler mileDocHandler = new MilestoneDocumentHandler();
		List<Document> milesDocs = new ArrayList<Document>(milestones.size());

		listener.notifyMilestonesMiningStart(milestones.size());
		mileDocHandler.deleteByRepository(repositoryId);

		if (milestones.size() > 0) {
			int index = 1;

			for (Milestone mile : milestones) {
				listener.notifyMilestonesMiningProgress(mile.getNumber(), mile.getTitle(), index++, milestones.size());

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

		listener.notifyMilestonesMiningEnd(milestones.size());
	}

	private void processIssues(List<Issue> issues) {
		IssueDocumentHandler issueDocHandler = new IssueDocumentHandler();
		List<Document> issuesDocs = new ArrayList<Document>(issues.size());

		listener.notifyIssuesMiningStart(issues.size());
		issueDocHandler.deleteByRepository(repositoryId);

		if (issues.size() > 0) {
			int index = 1;

			for (Issue issue : issues) {
				listener.notifyIssuesMiningProgress(issue.getNumber(), issue.getTitle(), index, issues.size());

				issue.setRepository(repositoryId);
				issuesDocs.add(issue.toDocument());
			}

			issueDocHandler.insertMany(issuesDocs);
		}

		listener.notifyIssuesMiningEnd(issues.size());
	}

}