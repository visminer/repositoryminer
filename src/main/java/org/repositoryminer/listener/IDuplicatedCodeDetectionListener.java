package org.repositoryminer.listener;

import org.repositoryminer.parser.Parser;

import net.sourceforge.pmd.cpd.Match;

public interface IDuplicatedCodeDetectionListener extends ITagCodeSmellDetectionListener {

	public void initDuplicationDetection();
	
	public void updateDuplicationDetection(Match match, Parser parse);
	
	public void endOfDuplicationDetection();
	
}
