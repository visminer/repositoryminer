package org.repositoryminer.mining.model;

import org.repositoryminer.scm.DiffType;

public class Diff {

	private String path;
	private String oldPath;
	private String hash;
	private int linesAdded;
	private int linesRemoved;
	private DiffType type;
	
}
