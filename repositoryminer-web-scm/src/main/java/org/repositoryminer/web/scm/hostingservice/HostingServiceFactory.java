package org.repositoryminer.web.scm.hostingservice;

public class HostingServiceFactory {

	public static IHostingService getHostingService(HostingServiceType type) {
		switch (type) {
		case GITHUB:
			return new GitHubService();
		default:
			return null;
		}
	}

}