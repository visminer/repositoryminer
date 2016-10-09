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
import org.repositoryminer.scm.hostingservice.IHostingService;
import org.repositoryminer.scm.hostingservice.HostingServiceFactory;
import org.repositoryminer.scm.hostingservice.HostingServiceType;

/**
 * This is the entry point to configure the parameters that enable the
 * synchronization of analyzed project with some web hosting service,
 * <i>e.g.</i> github.
 * <p>
 * <b>This analysis must to execute after mining the repository with
 * {@link RepositoryMiner}.</b>
 * <p>
 * Check more informations in the service that will be used about how to know
 * the owner and a name of a repository, <i>e.g.</i> in GitHUB we access a
 * repository like this, github.com/<owner>/<repository_name>.
 * <p>
 * Check if the user has the enough rights in the repository, otherwise the
 * synchronization will not retrieve some data
 */
public class HostingServiceMiner {

	private String repositoryId;
	private String owner;
	private String name;
	private HostingServiceType serviceType;
	private int issueMaxHops;
	private int milestoneMaxHops;

	private IHostingService service;

	private IHostServiceListener listener;

	/**
	 * Use this void constructor if parameters are going to be set later.
	 */
	public HostingServiceMiner() {
	}

	/**
	 * Use this non-void constructor if mandatory parameters are known
	 * <p>
	 * 
	 * @param repositoryId
	 *            the ID of repository mined before
	 * @param owner
	 *            repository owner username
	 * @param name
	 *            repository name
	 * @param serviceType
	 *            the web hosting service ({@link HostingServiceType}) we want
	 *            do the synchronization
	 */
	public HostingServiceMiner(String repositoryId, String owner, String name, HostingServiceType serviceType) {
		super();
		this.repositoryId = repositoryId;
		this.owner = owner;
		this.name = name;
		this.serviceType = serviceType;
	}

	/**
	 * This method is used to start the synchronization using login and password
	 * credentials
	 * <p>
	 * <b>We not store your credentials</b>
	 * <p>
	 * 
	 * @param login
	 *            check the service to know the login options, generally is
	 *            email or username
	 * @param password
	 */
	public HostingServiceMiner sync(String login, String password) {
		service = HostingServiceFactory.getHostingService(serviceType);
		service.connect(this, login, password);
		process();

		return this;
	}

	/**
	 * This method is used to start the synchronization using an access token
	 * provided by the service
	 * <p>
	 * <b>We not store your credentials</b><br>
	 * <b>We encourage the use of this method when possible, because is more
	 * secure</b>
	 * <p>
	 * 
	 * @param token
	 */
	public HostingServiceMiner sync(String token) {
		service = HostingServiceFactory.getHostingService(serviceType);
		service.connect(this, token);
		process();
		
		return this;
	}

	private HostingServiceMiner process() {
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
		
		return this;
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
	
	public String getRepositoryId() {
		return repositoryId;
	}

	public HostingServiceMiner setRepositoryId(String repositoryId) {
		this.repositoryId = repositoryId;
		return this;
	}

	public String getOwner() {
		return owner;
	}

	public HostingServiceMiner setOwner(String owner) {
		this.owner = owner;
		return this;
	}

	public String getName() {
		return name;
	}

	public HostingServiceMiner setName(String name) {
		this.name = name;
		return this;
	}

	public HostingServiceType getServiceType() {
		return serviceType;
	}

	public HostingServiceMiner setServiceType(HostingServiceType serviceType) {
		this.serviceType = serviceType;
		return this;
	}

	public int getIssueMaxHops() {
		return issueMaxHops;
	}

	public void setIssueMaxHops(int issueMaxHops) {
		this.issueMaxHops = issueMaxHops;
	}

	public int getMilestoneMaxHops() {
		return milestoneMaxHops;
	}

	public void setMilestoneMaxHops(int milestoneMaxHops) {
		this.milestoneMaxHops = milestoneMaxHops;
	}

	public IHostServiceListener getListener() {
		return listener;
	}

	public HostingServiceMiner setListener(IHostServiceListener listener) {
		this.listener = listener;
		return this;
	}

}