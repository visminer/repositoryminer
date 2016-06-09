package org.repositoryminer.mining.model;

import java.util.Date;
import java.util.List;

public class Commit {

	private String id;
	private String message;
	private Date authoredDate;
	private Date commitDate;
	private List<String> parents;
	private Contributor author;
	private Contributor committer;
	private List<Diff> diffs;
	
}
