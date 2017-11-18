package org.repositoryminer.technicaldebt;

import java.util.Set;

import org.repositoryminer.domain.Commit;
import org.repositoryminer.plugin.SnapshotAnalysisPlugin;
import org.repositoryminer.technicaldebt.model.TDIndicator;

public class RepositoryMinerTechnicalDebt extends SnapshotAnalysisPlugin<Set<TDIndicator>>{

	@Override
	public void run(String snapshot, Set<TDIndicator> config) {
		Commit commit = scm.resolve(snapshot);
	}
	
}