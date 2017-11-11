package org.repositoryminer.web.scm;

import org.repositoryminer.web.scm.hostingservice.HostingServiceType;

/**
 * Here you can configure the parameters that enable the synchronization of
 * analyzed project with some web hosting service, <i>e.g.</i> github.
 * <p>
 * Check more informations in the service that will be used about how to know
 * the owner and a name of a repository, <i>e.g.</i> in GitHUB we access a
 * repository like this, github.com/<owner>/<repository_name>.
 * <p>
 * Check if the user has the enough rights in the repository, otherwise the
 * synchronization will not retrieve some data.
 * <p>
 * <b>We not store your credentials.</b><br>
 * <b>We encourage the use of access tokens instead of passwords, because is
 * more secure.</b>
 */
public class RemoteMiningConfig {

	private String owner;
	private String name;
	private HostingServiceType serviceType;
	private String username;
	private String token;

	/**
	 * @param owner
	 *            repository owner user.
	 * @param name
	 *            repository name.
	 * @param serviceType
	 *            the web hosting service ({@link HostingServiceType}) we want do
	 *            the synchronization.
	 * @param username
	 *            the user at the hosting service.
	 * @param token
	 *            the token can be your password or an access token at the hosting
	 *            service.
	 * 
	 */
	public RemoteMiningConfig(String owner, String name, HostingServiceType serviceType, String username,
			String token) {
		this.owner = owner;
		this.name = name;
		this.serviceType = serviceType;
		this.username = username;
		this.token = token;
	}

	public String getOwner() {
		return owner;
	}

	public RemoteMiningConfig setOwner(String owner) {
		this.owner = owner;
		return this;
	}

	public String getName() {
		return name;
	}

	public RemoteMiningConfig setName(String name) {
		this.name = name;
		return this;
	}

	public HostingServiceType getServiceType() {
		return serviceType;
	}

	public RemoteMiningConfig setServiceType(HostingServiceType serviceType) {
		this.serviceType = serviceType;
		return this;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}