package org.repositoryminer.mining;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.exceptions.ErrorMessage;
import org.repositoryminer.exceptions.VisMinerAPIException;
import org.repositoryminer.persistence.handler.IssueDocumentHandler;
import org.repositoryminer.persistence.handler.MilestoneDocumentHandler;
import org.repositoryminer.persistence.handler.RepositoryDocumentHandler;
import org.repositoryminer.persistence.model.ContributorDB;
import org.repositoryminer.persistence.model.IssueDB;
import org.repositoryminer.persistence.model.MilestoneDB;
import org.repositoryminer.scm.hostingservice.HostingService;
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

	private HostingService service;

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
	 *            check the service to know the login options,
	 *            generally is email or username
	 * @param password
	 */
	public void sync(String login, String password) {
		service = HostingServiceFactory.getHostingService(serviceType);
		service.connect(owner, name, login, password);
		process();
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
	public void sync(String token) {
		service = HostingServiceFactory.getHostingService(serviceType);
		service.connect(owner, name, token);
		process();
	}

	@SuppressWarnings("unchecked")
	private void process() {
		RepositoryDocumentHandler repoDocHandler = new RepositoryDocumentHandler();
		if (!repoDocHandler.checkIfRepositoryExists(repositoryId)) {
			throw new VisMinerAPIException(ErrorMessage.REPOSITORY_NOT_FOUND.toString());
		}

		IssueDocumentHandler issueDocHandler = new IssueDocumentHandler();
		MilestoneDocumentHandler mileDocHandler = new MilestoneDocumentHandler();

		issueDocHandler.deleteByRepository(repositoryId);
		mileDocHandler.deleteByRepository(repositoryId);

		Document repoDoc = repoDocHandler.findOnlyContributors(repositoryId);
		List<ContributorDB> contributorsDb = service.getAllContributors();

		for (Document contributorDoc : (List<Document>) repoDoc.get("contributors")) {
			String name = contributorDoc.getString("name");
			for (ContributorDB contributorDb : contributorsDb) {
				if (name.equals(contributorDb.getName())) {
					contributorDoc.put("login", contributorDb.getLogin());
					contributorDoc.put("avatar_url", contributorDb.getAvatarUrl());
					contributorDoc.put("collaborator", contributorDb.isCollaborator());
					break;
				}
			}
		}

		repoDocHandler.updateOnlyContributors(repoDoc);
		List<IssueDB> issues = service.getAllIssues();
		List<MilestoneDB> milestones = service.getAllMilestones();

		// connect issues to milestones
		if (milestones.size() > 0) {
			for (MilestoneDB m : milestones) {
				m.setIssues(new ArrayList<Integer>());
				for (IssueDB i : issues) {
					if (i.getMilestone() == m.getNumber()) {
						m.getIssues().add(i.getNumber());
					}
				}
			}
		}

		List<Document> issuesDocs = new ArrayList<Document>(issues.size());
		List<Document> milesDocs = new ArrayList<Document>(milestones.size());

		if (issues.size() > 0) {
			for (IssueDB issue : issues) {
				issue.setRepository(repositoryId);
				issuesDocs.add(issue.toDocument());
			}
			issueDocHandler.insertMany(issuesDocs);
		}

		if (milestones.size() > 0) {
			for (MilestoneDB mile : milestones) {
				mile.setRepository(repositoryId);
				milesDocs.add(mile.toDocument());
			}
			mileDocHandler.insertMany(milesDocs);
		}
	}

	public String getRepositoryId() {
		return repositoryId;
	}

	public void setRepositoryId(String repositoryId) {
		this.repositoryId = repositoryId;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HostingServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(HostingServiceType serviceType) {
		this.serviceType = serviceType;
	}

}