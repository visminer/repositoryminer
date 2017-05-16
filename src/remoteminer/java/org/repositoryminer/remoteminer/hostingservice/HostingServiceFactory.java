package org.repositoryminer.remoteminer.hostingservice;

import org.repositoryminer.hostingservice.GitHubService;
import org.repositoryminer.hostingservice.HostingServiceType;
import org.repositoryminer.hostingservice.IHostingService;

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