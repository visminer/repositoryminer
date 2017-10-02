package org.repositoryminer.remoteminer.hostingservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.egit.github.core.IssueEvent;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.PageIterator;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.MilestoneService;
import org.repositoryminer.remoteminer.RemoteRepositoryMiner;
import org.repositoryminer.remoteminer.model.Comment;
import org.repositoryminer.remoteminer.model.Event;
import org.repositoryminer.remoteminer.model.Issue;
import org.repositoryminer.remoteminer.model.Label;
import org.repositoryminer.remoteminer.model.Milestone;

public class GitHubService implements IHostingService {

	private IssueService issueServ;
	private MilestoneService milestoneServ;
	private RepositoryId repositoryId;
	private RemoteRepositoryMiner hostingMiner;

	// Initializes repository and needed services.
	private void init(RemoteRepositoryMiner hostingMiner, GitHubClient client) {
		this.repositoryId = new RepositoryId(hostingMiner.getOwner(), hostingMiner.getName());
		this.issueServ = new IssueService(client);
		this.milestoneServ = new MilestoneService(client);
		this.hostingMiner = hostingMiner;
	}

	@Override
	public void connect(RemoteRepositoryMiner hostingMiner, String login, String password) {
		GitHubClient client = new GitHubClient();
		client.setCredentials(login, password);
		init(hostingMiner, client);
	}

	@Override
	public void connect(RemoteRepositoryMiner hostingMiner, String token) {
		GitHubClient client = new GitHubClient();
		client.setOAuth2Token(token);
		init(hostingMiner, client);
	}

	@Override
	public List<Issue> getAllIssues() {
		int number = 1;
		List<Issue> issues = new ArrayList<Issue>();

		while (number <= hostingMiner.getIssueMaxHops()) {
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

				issue.setComments(getAllComments(number));
				issue.setEvents(getlAllEvents(number));

				number++;
				issues.add(issue);
			} catch (IOException e) {
				number++;
				continue;
			}
		}

		return issues;
	}

	@Override
	public List<Milestone> getAllMilestones() {
		int number = 1;
		List<Milestone> milesDB = new ArrayList<Milestone>();

		while (number <= hostingMiner.getMilestoneMaxHops()) {
			try {
				org.eclipse.egit.github.core.Milestone mile = milestoneServ.getMilestone(repositoryId, number);
				Milestone mileDB = new Milestone(mile.getNumber(), StatusType.parse(mile.getState()), mile.getTitle(),
						mile.getDescription(), mile.getOpenIssues(), mile.getClosedIssues(), mile.getCreatedAt(),
						mile.getDueOn());

				if (mile.getCreator() != null) {
					mileDB.setCreator(mile.getCreator().getLogin());
				}

				number++;
				milesDB.add(mileDB);
			} catch (IOException e) {
				number++;
				continue;
			}
		}

		return milesDB;
	}

	private List<Comment> getAllComments(int issueId) throws IOException {
		List<Comment> comments = new ArrayList<Comment>();

		List<org.eclipse.egit.github.core.Comment> commentz = issueServ.getComments(repositoryId, issueId);
		if (commentz != null) {
			for (org.eclipse.egit.github.core.Comment c : commentz) {
				Comment comment = new Comment(c.getUser().getLogin(), c.getBody(), c.getCreatedAt(), c.getUpdatedAt());
				comments.add(comment);
			}
		}

		return comments;
	}

	private List<Event> getlAllEvents(int issueId) {
		List<Event> events = new ArrayList<Event>();

		PageIterator<IssueEvent> eventsPages = issueServ.pageIssueEvents(hostingMiner.getOwner(),
				hostingMiner.getName(), issueId);
		if (eventsPages != null) {
			while (eventsPages.hasNext()) {
				Collection<IssueEvent> issueEvents = eventsPages.next();

				for (IssueEvent issueEvent : issueEvents) {
					Event event = new Event();
					event.setDescription(issueEvent.getEvent());
					User user = issueEvent.getActor();
					if (user != null) {
						event.setCreator(user.getName());
					}
					event.setCreatedAt(issueEvent.getCreatedAt());
					event.setCommitId(issueEvent.getCommitId());

					events.add(event);
				}
			}
		}

		return events;
	}

}