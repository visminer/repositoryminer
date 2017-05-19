package org.repositoryminer.listener.mining;

import java.util.ArrayList;
import java.util.List;

import org.repositoryminer.scm.ReferenceType;

public class MultiMiningListener implements IMiningListener {

	private List<IMiningListener> listeners = new ArrayList<IMiningListener>();
	
	public List<IMiningListener> getListeners() {
		return listeners;
	}

	public void setListeners(List<IMiningListener> listeners) {
		this.listeners = listeners;
	}

	public void addListener(IMiningListener listener) {
		listeners.add(listener);
	}
	
	@Override
	public void notifyMiningStart(String repositoryName) {
		for (IMiningListener listener : listeners) {
			listener.notifyMiningStart(repositoryName);
		}
	}

	@Override
	public void notifyMiningEnd(String repositoryName) {
		for (IMiningListener listener : listeners) {
			listener.notifyMiningEnd(repositoryName);
		}
	}

	@Override
	public void notifyReferencesMiningStart(int referencesQtd) {
		for (IMiningListener listener : listeners) {
			listener.notifyReferencesMiningStart(referencesQtd);
		}
	}

	@Override
	public void notifyReferencesMiningProgress(String referenceName, ReferenceType referenceType) {
		for (IMiningListener listener : listeners) {
			listener.notifyReferencesMiningProgress(referenceName, referenceType);
		}
	}

	@Override
	public void notifyReferencesMiningEnd(int referencesQtd) {
		for (IMiningListener listener : listeners) {
			listener.notifyReferencesMiningEnd(referencesQtd);
		}
	}

	@Override
	public void notifyCommitsMiningStart(String referenceName, ReferenceType referenceType, int commitsQtd) {
		for (IMiningListener listener : listeners) {
			listener.notifyCommitsMiningStart(referenceName, referenceType, commitsQtd);
		}
	}

	@Override
	public void notifyCommitsMiningProgress(String referenceName, ReferenceType referenceType, String commit) {
		for (IMiningListener listener : listeners) {
			listener.notifyCommitsMiningProgress(referenceName, referenceType, commit);
		}
	}

	@Override
	public void notifyCommitsMiningEnd(String referenceName, ReferenceType referenceType, int commitsQtd) {
		for (IMiningListener listener : listeners) {
			listener.notifyCommitsMiningEnd(referenceName, referenceType, commitsQtd);
		}
	}

	@Override
	public void notifyWorkingDirectoriesMiningStart(String referenceName, ReferenceType referenceType, int commitsQtd) {
		for (IMiningListener listener : listeners) {
			listener.notifyWorkingDirectoriesMiningStart(referenceName, referenceType, commitsQtd);
		}
	}

	@Override
	public void notifyWorkingDirectoriesMiningProgress(String referenceName, ReferenceType referenceType,
			String commit) {
		for (IMiningListener listener : listeners) {
			listener.notifyWorkingDirectoriesMiningProgress(referenceName, referenceType, commit);
		}
	}

	@Override
	public void notifyWorkingDirectoriesMiningEnd(String referenceName, ReferenceType referenceType, int commitsQtd) {
		for (IMiningListener listener : listeners) {
			listener.notifyWorkingDirectoriesMiningEnd(referenceName, referenceType, commitsQtd);
		}
	}

	@Override
	public void notifyDirectCodeAnalysisStart(int totalCommits) {
		for (IMiningListener listener : listeners) {
			listener.notifyDirectCodeAnalysisStart(totalCommits);
		}
	}

	@Override
	public void notifyDirectCodeAnalysisProgress(String commit, int index, int totalCommits) {
		for (IMiningListener listener : listeners) {
			listener.notifyDirectCodeAnalysisProgress(commit, index, totalCommits);
		}
	}

	@Override
	public void notifyDirectCodeAnalysisEnd(int totalCommits) {
		for (IMiningListener listener : listeners) {
			listener.notifyDirectCodeAnalysisEnd(totalCommits);
		}
	}

	@Override
	public void notifyIndirectCodeAnalysisStart(int totalSnapshots) {
		for (IMiningListener listener : listeners) {
			listener.notifyIndirectCodeAnalysisStart(totalSnapshots);
		}
	}

	@Override
	public void notifyIndirectCodeAnalysisProgress(String snapshot, int index, int totalSnapshots) {
		for (IMiningListener listener : listeners) {
			listener.notifyIndirectCodeAnalysisProgress(snapshot, index, totalSnapshots);
		}
	}

	@Override
	public void notifyIndirectCodeAnalysisEnd(int totalSnapshots) {
		for (IMiningListener listener : listeners) {
			listener.notifyIndirectCodeAnalysisEnd(totalSnapshots);
		}
	}

}