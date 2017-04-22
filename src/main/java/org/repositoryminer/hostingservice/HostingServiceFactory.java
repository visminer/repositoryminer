package org.repositoryminer.hostingservice;

public class HostingServiceFactory {

	public static IHostingService getHostingService(HostingServiceType scmHosting) {
		switch (scmHosting) {
		case GITHUB:
			return new GitHubService();
		default:
			return null;
		}
	}

}