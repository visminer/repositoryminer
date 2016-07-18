package org.repositoryminer.scm.hostingservice;

public class HostingServiceFactory {

	public static HostingService getHostingService(HostingServiceType scmHosting) {
		switch (scmHosting) {
		case GITHUB:
			return new GitHubService();
		default:
			return null;
		}
	}

}