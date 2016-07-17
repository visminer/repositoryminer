package org.repositoryminer.listener;

import java.util.Map;

public interface ICommitCodeSmellDetectionListener {

	public void updateSmellDetection(String smellName, boolean detected);
	
	public void updateMethodBasedSmellDetection(String smellName, Map<String, Boolean> detectionsPerMethod);
	
}
