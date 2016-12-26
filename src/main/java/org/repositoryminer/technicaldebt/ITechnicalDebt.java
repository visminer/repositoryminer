package org.repositoryminer.technicaldebt;

import org.bson.Document;

public interface ITechnicalDebt {

	public void detect(Document workingDirectory);
	
}