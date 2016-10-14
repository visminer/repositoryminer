package org.repositoryminer.mining;

import org.repositoryminer.listener.IHostServiceListener;
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
// TODO: Documentation needs refactoring
public class HostingServiceMiner {

	private String repositoryId;
	private String owner;
	private String name;
	private HostingServiceType serviceType;
	private int issueMaxHops = 1000;
	private int milestoneMaxHops = 50;
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
	public void sync(String login, String password) {
		HostingProcessor hp = new HostingProcessor();
		hp.connectToService(this, login, password);
		hp.mine();
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
		HostingProcessor hp = new HostingProcessor();
		hp.connectToService(this, token);
		hp.mine();
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