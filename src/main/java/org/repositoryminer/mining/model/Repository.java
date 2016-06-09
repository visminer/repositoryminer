package org.repositoryminer.mining.model;

import java.util.List;
import java.util.Map;

import org.repositoryminer.scm.SCMType;

public class Repository {

	private String id;
	private String name;
	private String description;
	private String path;
	private SCMType scm;
	private List<Commit> commits;
	private List<Reference> branches;
	private List<Reference> tags;
	private List<Contributor> contributors;
	private Map<String, String> workingDirectory;
	
	private int currentCommit;
	private Reference currentReference;
	
}
