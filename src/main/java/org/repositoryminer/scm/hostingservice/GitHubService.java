package org.repositoryminer.scm.hostingservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CollaboratorService;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.MilestoneService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.repositoryminer.listener.IHostServiceListener;
import org.repositoryminer.model.Comment;
import org.repositoryminer.model.Contributor;
import org.repositoryminer.model.Issue;
import org.repositoryminer.model.Label;
import org.repositoryminer.model.Milestone;

public class GitHubService implements IHostingService {

	private IssueService issueServ;
	private MilestoneService milestoneServ;
	private RepositoryId repositoryId;
	private CollaboratorService collaboratorServ;
	private RepositoryService repoServ;
	private IHostServiceListener listener;

	// Initializes repository and needed services.
	private void init(String name, String owner, GitHubClient client, IHostServiceListener listener) {
		this.repositoryId = new RepositoryId(owner, name);
		this.issueServ = new IssueService(client);
		this.milestoneServ = new MilestoneService(client);
		this.collaboratorServ = new CollaboratorService(client);
		this.repoServ = new RepositoryService(client);
		this.listener = listener;
	}

	@Override
	public void connect(String owner, String name, String login, String password, IHostServiceListener listener) {
		GitHubClient client = new GitHubClient();
		client.setCredentials(login, password);
		init(name, owner, client, listener);
	}

	@Override
	public void connect(String owner, String name, String token, IHostServiceListener listener) {
		GitHubClient client = new GitHubClient();
		client.setOAuth2Token(token);
		init(name, owner, client, listener);
	}

	@Override
	public List<Issue> getAllIssues() {
		if (listener != null) {
			listener.initIssuesProcessing();
		}

		int number = 1;
		List<Issue> issues = new ArrayList<Issue>();

		while (true) {
			try {
				org.eclipse.egit.github.core.Issue izzue = issueServ.getIssue(repositoryId, number);
				Issue issue = new Issue(izzue.getUser().getLogin(), izzue.getClosedAt(), izzue.getCreatedAt(),
						izzue.getNumber(), StatusType.parse(izzue.getState()), izzue.getTitle(), izzue.getUpdatedAt(),
						izzue.getBody());

				if (izzue.getAssignee() != null) {
					issue.setAssignee(izzue.getAssignee().getLogin());
				}

				if (izzue.getMilestone() != null) {
					issue.setMilestone(izzue.getMilestone().getNumber());
				}

				if (izzue.getLabels() != null) {
					List<Label> labels = new ArrayList<Label>();
					for (org.eclipse.egit.github.core.Label l : izzue.getLabels()) {
						labels.add(new Label(l.getName(), l.getColor()));
					}

					issue.setLabels(labels);
				}

				List<org.eclipse.egit.github.core.Comment> commentz = issueServ.getComments(repositoryId.getOwner(),
						repositoryId.getName(), number++);
				if (commentz != null) {
					List<Comment> comments = new ArrayList<Comment>();
					for (org.eclipse.egit.github.core.Comment c : commentz) {
						String user = c.getUser().getName();
						if (user == null) {
							user = c.getUser().getLogin();
							if (user == null) {
								user = c.getUser().getEmail();
								if (user == null) {
									user = "UNDEFINED";
								}
							}
						}
						Comment comment = new Comment(user, c.getBody());
						comments.add(comment);
					}

					issue.setComments(comments);
				}

				issues.add(issue);
			} catch (IOException e) {
				break;
			}
		}

		return issues;
	}

	@Override
	public List<Milestone> getAllMilestones() {
		if (listener != null) {
			listener.initMilestonesProcessing();
		}

		int number = 1;
		List<Milestone> milesDB = new ArrayList<Milestone>();
		while (true) {
			try {
				org.eclipse.egit.github.core.Milestone mile = milestoneServ.getMilestone(repositoryId, number++);
				Milestone mileDB = new Milestone(mile.getNumber(), StatusType.parse(mile.getState()), mile.getTitle(),
						mile.getDescription(), mile.getOpenIssues(), mile.getClosedIssues(), mile.getCreatedAt(),
						mile.getDueOn());

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
	public List<Contributor> getAllContributors() {
		if (listener != null) {
			listener.initContributorsProcessing();
		}

		List<Contributor> contributors = new ArrayList<Contributor>();
		try {
			for (org.eclipse.egit.github.core.Contributor contributor : repoServ.getContributors(repositoryId, true)) {
				contributors.add(new Contributor(contributor.getName(), contributor.getLogin(),
						contributor.getAvatarUrl(), false));
			}

			for (User user : collaboratorServ.getCollaborators(repositoryId)) {
				for (Contributor contributor : contributors) {
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