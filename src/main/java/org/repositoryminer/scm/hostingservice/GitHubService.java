package org.repositoryminer.scm.hostingservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.egit.github.core.Contributor;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Label;
import org.eclipse.egit.github.core.Milestone;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CollaboratorService;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.MilestoneService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.repositoryminer.persistence.model.ContributorDB;
import org.repositoryminer.persistence.model.IssueDB;
import org.repositoryminer.persistence.model.LabelDB;
import org.repositoryminer.persistence.model.MilestoneDB;

public class GitHubService implements HostingService {

	private IssueService issueServ;
	private MilestoneService milestoneServ;
	private RepositoryId repositoryId;
	private CollaboratorService collaboratorServ;
	private RepositoryService repoServ;

	// Initializes repository and needed services.
	private void init(String name, String owner, GitHubClient client) {
		repositoryId = new RepositoryId(owner, name);
		issueServ = new IssueService(client);
		milestoneServ = new MilestoneService(client);
		collaboratorServ = new CollaboratorService(client);
		repoServ = new RepositoryService(client);
	}

	@Override
	public void connect(String owner, String name, String login, String password) {
		GitHubClient client = new GitHubClient();
		client.setCredentials(login, password);
		init(name, owner, client);
	}

	@Override
	public void connect(String owner, String name, String token) {
		GitHubClient client = new GitHubClient();
		client.setOAuth2Token(token);
		init(name, owner, client);
	}

	@Override
	public List<IssueDB> getAllIssues() {
		int number = 1;
		List<IssueDB> issuesDB = new ArrayList<IssueDB>();

		while (true) {
			try {
				Issue issue = issueServ.getIssue(repositoryId, number++);
				IssueDB issueDB = new IssueDB(issue.getUser().getLogin(), issue.getClosedAt(), issue.getComments(),
						issue.getCreatedAt(), issue.getNumber(), StatusType.parse(issue.getState()), issue.getTitle(),
						issue.getUpdatedAt(), issue.getBody());

				if (issue.getAssignee() != null) {
					issueDB.setAssignee(issue.getAssignee().getLogin());
				}

				if (issue.getMilestone() != null) {
					issueDB.setMilestone(issue.getMilestone().getNumber());
				}

				if (issue.getLabels() != null) {
					List<LabelDB> labels = new ArrayList<LabelDB>();
					for (Label l : issue.getLabels()) {
						labels.add(new LabelDB(l.getName(), l.getColor()));
					}
					issueDB.setLabels(labels);
				}

				issuesDB.add(issueDB);
			} catch (IOException e) {
				break;
			}
		}

		return issuesDB;
	}

	@Override
	public List<MilestoneDB> getAllMilestones() {
		int number = 1;
		List<MilestoneDB> milesDB = new ArrayList<MilestoneDB>();

		while (true) {
			try {
				Milestone mile = milestoneServ.getMilestone(repositoryId, number++);
				MilestoneDB mileDB = new MilestoneDB(mile.getNumber(), StatusType.parse(mile.getState()),
						mile.getTitle(), mile.getDescription(), mile.getOpenIssues(), mile.getClosedIssues(),
						mile.getCreatedAt(), mile.getDueOn());

				if (mile.getCreator() != null) {
					mileDB.setCreator(mile.getCreator().getLogin());
				}

				milesDB.add(mileDB);
			} catch (IOException e) {
				break;
			}
		}

		return milesDB;
	}

	@Override
	public List<ContributorDB> getAllContributors() {
		List<ContributorDB> contributors = new ArrayList<ContributorDB>();

		try {
			for (Contributor contributor : repoServ.getContributors(repositoryId, true)) {
				contributors.add(new ContributorDB(contributor.getName(), contributor.getLogin(),
						contributor.getAvatarUrl(), false));
			}

			for (User user : collaboratorServ.getCollaborators(repositoryId)) {
				for (ContributorDB contributor : contributors) {
					if (contributor.getLogin().equals(user.getLogin())) {
						contributor.setCollaborator(true);
						contributor.setEmail(user.getEmail());
					}
				}
			}

			return contributors;
		} catch (IOException e) {
			return contributors;
		}
	}

}