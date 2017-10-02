package org.repositoryminer.remoteminer.hostingservice;

import org.repositoryminer.remoteminer.hostingservice.GitHubService;
import org.repositoryminer.remoteminer.hostingservice.HostingServiceType;
import org.repositoryminer.remoteminer.hostingservice.IHostingService;

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