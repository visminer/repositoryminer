package org.repositoryminer.mining;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bson.Document;
import org.repositoryminer.persistence.handler.ReferenceDocumentHandler;
import org.repositoryminer.persistence.model.CommitDB;
import org.repositoryminer.persistence.model.ReferenceDB;
import org.repositoryminer.scm.ReferenceType;
import org.repositoryminer.utility.HashHandler;

public class ProcessTimeFrames {

	private static final String RM_TAG_NAME = "CUSTOM_TAG-";

	private Map<String, ReferenceDB> refs;
	private String repositoryPath;
	private String repositoryId;

	public ProcessTimeFrames(String repositoryPath, String repositoryId) {
		this.refs = new TreeMap<String, ReferenceDB>();
		this.repositoryPath = repositoryPath;
		this.repositoryId = repositoryId;
	}

	public List<ReferenceDB> analyzeCommits(List<CommitDB> commits, List<TimeFrameType> timeFrames) {
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

		for (CommitDB c : commits) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(c.getCommitDate());

			int month = calendar.get(Calendar.MONTH);
			int year = calendar.get(Calendar.YEAR);
			String hash = c.getId();

			if (monthFrame)
				analyzeMonth(hash, year, month);
			if (trimesterFrame)
				analyzeTrimester(hash, year, month);
			if (semesterFrame)
				analyzeSemester(hash, year, month);
			if (yearFrame)
				analyzeYear(hash, year, month);
		}

		List<Document> docs = new ArrayList<Document>();
		List<ReferenceDB> timeRefs = new ArrayList<ReferenceDB>(refs.values());
		for (ReferenceDB r : timeRefs) {
			Collections.reverse(r.getCommits());
			docs.add(r.toDocument());
		}
		
		ReferenceDocumentHandler docHandler = new ReferenceDocumentHandler();
		docHandler.insertMany(docs);
		return timeRefs;
	}

	private void analyzeYear(String hash, int year, int month) {
		String name = String.valueOf(year);
		String key = RM_TAG_NAME + name;
		checkReference(key, name, hash);
	}

	private void analyzeTrimester(String hash, int year, int month) {
		String name = year + "-TRIMESTER-";
		String key = RM_TAG_NAME;

		if (month >= 0 && month <= 2) {
			name += 1;
		} else if (month >= 3 && month <= 5) {
			name += 2;
		} else if (month >= 6 && month <= 8) {
			name += 3;
		} else {
			name += 4;
		}

		key += name;
		checkReference(key, name, hash);
	}

	private void analyzeSemester(String hash, int year, int month) {
		String name = year + "-SEMESTER-";
		String key = RM_TAG_NAME;

		if (month >= 0 && month <= 5) {
			name += 1;
		} else {
			name += 2;
		}

		key += name;
		checkReference(key, name, hash);
	}

	private void analyzeMonth(String hash, int year, int month) {
		String name = year + "-MONTH-" + (month + 1);
		String key = RM_TAG_NAME + name;
		checkReference(key, name, hash);
	}

	private void checkReference(String key, String name, String hash) {
		if (refs.containsKey(key)) {
			ReferenceDB tempRef = refs.get(key);
			tempRef.getCommits().add(hash);
		} else {
			String id = HashHandler.SHA1(repositoryPath + "/" + key);
			ReferenceDB r = new ReferenceDB(id, repositoryId, name, key, ReferenceType.CUSTOM_TIME_TAG);
			r.setCommits(new ArrayList<String>());
			r.getCommits().add(hash);
			refs.put(key, r);
		}
	}

}