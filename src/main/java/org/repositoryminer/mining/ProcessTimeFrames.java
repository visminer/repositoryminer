package org.repositoryminer.mining;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.repositoryminer.listener.IMiningListener;
import org.repositoryminer.model.Commit;
import org.repositoryminer.model.Reference;
import org.repositoryminer.persistence.handler.ReferenceDocumentHandler;
import org.repositoryminer.scm.ReferenceType;

public class ProcessTimeFrames {

	private static final String REF_PATH = "timerefs/";

	private Map<String, Reference> refs;
	private String repositoryId;
	private Map<String, Commit> commitsMap;
	private IMiningListener listener;

	public ProcessTimeFrames(String repositoryId, Map<String, Commit> commitsMap, IMiningListener listener) {
		this.repositoryId = repositoryId;
		this.commitsMap = commitsMap;
		this.listener = listener;
	}

	public Collection<? extends Reference> analyzeCommits(Reference reference, TimeFrameType[] timeFrames) {
		this.refs = new HashMap<String, Reference>();

		boolean monthFrame = false;
		boolean trimesterFrame = false;
		boolean semesterFrame = false;
		boolean yearFrame = false;

		for (TimeFrameType type : timeFrames) {
			switch (type) {
			case MONTH:
				monthFrame = true;
				break;

			case TRIMESTER:
				trimesterFrame = true;
				break;

			case SEMESTER:
				semesterFrame = true;
				break;

			case YEAR:
				yearFrame = true;
				break;
			}
		}

		String refPath = REF_PATH;
		if (reference.getType() == ReferenceType.TAG) {
			refPath += "tags/" + reference.getName() + "/";
		} else if (reference.getType() == ReferenceType.BRANCH) {
			refPath += "heads/" + reference.getName() + "/";
		}

		int idx = 0;
		List<String> commits = reference.getCommits();
		for (String hash : commits) {
			if (listener != null) {
				listener.commitsProgressChange(++idx, commits.size());
			}

			Commit commit = commitsMap.get(hash);

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(commit.getCommitDate());

			int month = calendar.get(Calendar.MONTH);
			int year = calendar.get(Calendar.YEAR);

			if (monthFrame) {
				analyzeMonth(refPath, year, month, hash);
			}

			if (trimesterFrame) {
				analyzeTrimester(refPath, year, month, hash);
			}

			if (semesterFrame) {
				analyzeSemester(refPath, year, month, hash);
			}

			if (yearFrame) {
				analyzeYear(refPath, year, month, hash);
			}
		}

		List<Document> docs = new ArrayList<Document>();
		List<Reference> timeRefs = new ArrayList<Reference>(refs.values());

		idx = 0;
		for (Reference r : timeRefs) {
			if (listener != null) {
				listener.timeFramesProgressChange(reference.getName(), ++idx, timeRefs.size());
			}
			docs.add(r.toDocument());
		}

		ReferenceDocumentHandler docHandler = new ReferenceDocumentHandler();
		docHandler.insertMany(docs);
		return timeRefs;
	}

	private void analyzeYear(String refPath, int year, int month, String hash) {
		String name = "YEAR-" + year;
		checkReference(name, refPath + name, hash);
	}

	private void analyzeTrimester(String refPath, int year, int month, String hash) {
		String name = "YEAR-" + year + "-TRIMESTER-";

		if (month >= 0 && month <= 2) {
			name += 1;
		} else if (month >= 3 && month <= 5) {
			name += 2;
		} else if (month >= 6 && month <= 8) {
			name += 3;
		} else {
			name += 4;
		}

		checkReference(name, refPath + name, hash);
	}

	private void analyzeSemester(String refPath, int year, int month, String hash) {
		String name = "YEAR-" + year + "-SEMESTER-";

		if (month >= 0 && month <= 5) {
			name += 1;
		} else {
			name += 2;
		}

		checkReference(name, refPath + name, hash);
	}

	private void analyzeMonth(String refPath, int year, int month, String hash) {
		String name = "YEAR-" + year + "-MONTH-" + (month + 1);
		checkReference(name, refPath + name, hash);
	}

	private void checkReference(String name, String path, String hash) {
		if (refs.containsKey(name)) {
			Reference tempRef = refs.get(name);
			tempRef.getCommits().add(hash);
		} else {
			Reference r = new Reference(null, repositoryId, name, path, ReferenceType.TIME_TAG,
					new ArrayList<String>());
			r.getCommits().add(hash);
			refs.put(name, r);
		}
	}

}