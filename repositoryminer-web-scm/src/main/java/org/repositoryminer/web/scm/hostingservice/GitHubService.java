package org.repositoryminer.web.scm.hostingservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.egit.github.core.IssueEvent;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.PageIterator;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.MilestoneService;
import org.repositoryminer.RepositoryMinerException;
import org.repositoryminer.web.scm.RemoteMiningConfig;
import org.repositoryminer.web.scm.model.Comment;
import org.repositoryminer.web.scm.model.Event;
import org.repositoryminer.web.scm.model.Issue;
import org.repositoryminer.web.scm.model.Label;
import org.repositoryminer.web.scm.model.Milestone;
import org.repositoryminer.web.scm.model.StatusType;

public class GitHubService implements IHostingService {

	private IssueService issueServ;
	private MilestoneService milestoneServ;
	private RepositoryId repositoryId;


	@Override
	public void connect(RemoteMiningConfig config, String username, String token) {
		GitHubClient client = new GitHubClient();
		client.setCredentials(username, token);
		
		this.repositoryId = new RepositoryId(config.getOwner(), config.getName());
		this.issueServ = new IssueService(client);
		this.milestoneServ = new MilestoneService(client);
	}

	@Override
	public List<Issue> getAllIssues() {
		List<Issue> issues = new ArrayList<Issue>();
		Map<String, String> parameters = new HashMap<>();
		parameters.put("state", "all");
		
		try {
			for (org.eclipse.egit.github.core.Issue izzue : issueServ.getIssues(repositoryId, parameters)) {
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

				issue.setComments(getAllComments(issue.getNumber()));
				issue.setEvents(getlAllEvents(issue.getNumber()));

				issues.add(issue);
			}
		} catch (IOException e) {
			throw new RepositoryMinerException("Was not possible to get the issues.", e);
		}

		return issues;
	}

	@Override
	public List<Milestone> getAllMilestones() {
		List<Milestone> milesDB = new ArrayList<Milestone>();

		try {
			for (org.eclipse.egit.github.core.Milestone mile : milestoneServ.getMilestones(repositoryId, "all")) {
				Milestone mileDB = new Milestone(mile.getNumber(), StatusType.parse(mile.getState()), mile.getTitle(),
						mile.getDescription(), mile.getOpenIssues(), mile.getClosedIssues(), mile.getCreatedAt(),
						mile.getDueOn());

				if (mile.getCreator() != null) {
					mileDB.setCreator(mile.getCreator().getLogin());
				}
				milesDB.add(mileDB);
			}
		} catch (IOException e) {
			throw new RepositoryMinerException("Was not possible to get the milestones.", e);
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

		PageIterator<IssueEvent> eventsPages = issueServ.pageIssueEvents(repositoryId.getOwner(),
				repositoryId.getName(), issueId);
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