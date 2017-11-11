package org.repositoryminer.findbugs;

import java.util.HashSet;
import java.util.Set;

import org.repositoryminer.findbugs.configuration.Effort;
import org.repositoryminer.findbugs.configuration.Priority;

public class FindBugsConfig {

	private Priority priority = Priority.NORMAL;
	private Effort effort = Effort.DEFAULT;
	private Set<String> analysisClasspath = new HashSet<String>();
	private boolean insideRepository = false;

	public FindBugsConfig() {}

	public FindBugsConfig(Priority priority, Effort effort, Set<String> analysisClasspath, boolean insideRepository) {
		this.priority = priority;
		this.effort = effort;
		this.analysisClasspath = analysisClasspath;
		this.insideRepository = insideRepository;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public Effort getEffort() {
		return effort;
	}

	public void setEffort(Effort effort) {
		this.effort = effort;
	}

	public Set<String> getAnalysisClasspath() {
		return analysisClasspath;
	}

	public void setAnalysisClasspath(Set<String> analysisClasspath) {
		this.analysisClasspath = analysisClasspath;
	}

	public boolean isInsideRepository() {
		return insideRepository;
	}

	public void setInsideRepository(boolean insideRepository) {
		this.insideRepository = insideRepository;
	}

}