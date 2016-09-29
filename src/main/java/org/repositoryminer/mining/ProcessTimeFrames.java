package org.repositoryminer.mining;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bson.Document;
import org.repositoryminer.listener.IProgressListener;
import org.repositoryminer.model.Commit;
import org.repositoryminer.model.Reference;
import org.repositoryminer.persistence.handler.ReferenceDocumentHandler;
import org.repositoryminer.scm.ReferenceType;

public class ProcessTimeFrames {

	private static final String TAG_PATH = "timetags/";
	
	private Map<String, Reference> refs;
	private String repositoryId;
	private Map<String, Commit> commitsMap;
	private IProgressListener progressListener;

	public ProcessTimeFrames(String repositoryId, Map<String, Commit> commitsMap, IProgressListener progressListener) {
		this.repositoryId = repositoryId;
		this.commitsMap = commitsMap;
		this.progressListener = progressListener;
	}

	public Collection<? extends Reference> analyzeCommits(Reference reference, TimeFrameType[] value) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<Reference> analyzeCommits(List<Commit> commits, List<TimeFrameType> timeFrames, IProgressListener progressListener) {
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

		int idx = 0;
		for (Commit c : commits) {
			if (progressListener != null) {
				progressListener.commitsProgressChange(++idx, commits.size());
			}
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(c.getCommitDate());

			int month = calendar.get(Calendar.MONTH);
			int year = calendar.get(Calendar.YEAR);
			String hash = c.getId();

			if (monthFrame) {
				analyzeMonth(hash, year, month);
			}
			
			if (trimesterFrame) {
				analyzeTrimester(hash, year, month);
			}
			
			if (semesterFrame) {
				analyzeSemester(hash, year, month);
			}
			
			if (yearFrame) {
				analyzeYear(hash, year, month);
			}
		}

		List<Document> docs = new ArrayList<Document>();
		List<Reference> timeRefs = new ArrayList<Reference>(refs.values());
		idx = 0;
		for (Reference r : timeRefs) {
			if (progressListener != null) {
				progressListener.timeFramesProgressChange(++idx, timeRefs.size());
			}
			
			Collections.reverse(r.getCommits());
			docs.add(r.toDocument());
		}
		
		ReferenceDocumentHandler docHandler = new ReferenceDocumentHandler();
		docHandler.insertMany(docs);
		return timeRefs;
	}

	private void analyzeYear(String hash, int year, int month) {
		String name = String.valueOf(year);
		checkReference(name, hash);
	}

	private void analyzeTrimester(String hash, int year, int month) {
		String name = year + "-TRIMESTER-";

		if (month >= 0 && month <= 2) {
			name += 1;
		} else if (month >= 3 && month <= 5) {
			name += 2;
		} else if (month >= 6 && month <= 8) {
			name += 3;
		} else {
			name += 4;
		}

		checkReference(name, hash);
	}

	private void analyzeSemester(String hash, int year, int month) {
		String name = year + "-SEMESTER-";

		if (month >= 0 && month <= 5) {
			name += 1;
		} else {
			name += 2;
		}

		checkReference(name, hash);
	}

	private void analyzeMonth(String hash, int year, int month) {
		String name = year + "-MONTH-" + (month + 1);
		checkReference(name, hash);
	}

	private void checkReference(String name, String hash) {
		if (refs.containsKey(name)) {
			Reference tempRef = refs.get(name);
			tempRef.getCommits().add(hash);
		} else {
			Reference r = new Reference(null, repositoryId, name, TAG_PATH + name, ReferenceType.TIME_TAG, new ArrayList<String>());
			r.getCommits().add(hash);
			refs.put(name, r);
		}
	}

}