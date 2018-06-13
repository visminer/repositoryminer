package org.repositoryminer.findbugs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.bson.Document;
import org.repositoryminer.RepositoryMinerException;
import org.repositoryminer.domain.Commit;
import org.repositoryminer.findbugs.configuration.Effort;
import org.repositoryminer.findbugs.configuration.Priority;
import org.repositoryminer.findbugs.model.ReportedBug;
import org.repositoryminer.findbugs.persistence.FindBugsDAO;
import org.repositoryminer.plugin.SnapshotAnalysisPlugin;
import org.repositoryminer.util.RMFileUtils;
import org.repositoryminer.util.StringUtils;

import edu.umd.cs.findbugs.FindBugs;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.config.AnalysisFeatureSetting;
import edu.umd.cs.findbugs.config.UserPreferences;

public class RepositoryMinerFindBugs extends SnapshotAnalysisPlugin<FindBugsConfig> {

	private static final String[] EXTENSION_FILE_FILTER = { "java" };

	private static final Map<Priority, Integer> prioritiesMap = new HashMap<Priority, Integer>();
	private static final Map<Effort, AnalysisFeatureSetting[]> effortsMap = new HashMap<Effort, AnalysisFeatureSetting[]>();
	private static final Map<Effort, String> userPrefsEffortMap = new HashMap<Effort, String>();

	static {
		prioritiesMap.put(Priority.LOW, Priorities.IGNORE_PRIORITY);
		prioritiesMap.put(Priority.NORMAL, Priorities.NORMAL_PRIORITY);
		prioritiesMap.put(Priority.HIGH, Priorities.HIGH_PRIORITY);

		effortsMap.put(Effort.MIN, FindBugs.MIN_EFFORT);
		effortsMap.put(Effort.DEFAULT, FindBugs.DEFAULT_EFFORT);
		effortsMap.put(Effort.MAX, FindBugs.MAX_EFFORT);

		userPrefsEffortMap.put(Effort.MIN, UserPreferences.EFFORT_MIN);
		userPrefsEffortMap.put(Effort.DEFAULT, UserPreferences.EFFORT_DEFAULT);
		userPrefsEffortMap.put(Effort.MAX, UserPreferences.EFFORT_MAX);
	}

	@Override
	public void run(String snapshot, FindBugsConfig config) {
		scm.checkout(snapshot);
		Commit commit = scm.getHEAD();

		FindBugsDAO dao = new FindBugsDAO();
		dao.deleteByCommit(commit.getHash());
		
		FindBugsExecutor findBugsExecutor = new FindBugsExecutor();
		findBugsExecutor.setBugPriority(prioritiesMap.get(config.getPriority()));
		findBugsExecutor.setEffort(effortsMap.get(config.getEffort()));
		findBugsExecutor.setUserPrefsEffort(userPrefsEffortMap.get(config.getEffort()));

		if (config.isInsideRepository()) {
			findBugsExecutor.setAnalysisClasspath(
					new HashSet<String>(RMFileUtils.concatFilePath(tmpRepository, config.getAnalysisClasspath())));
		} else {
			findBugsExecutor.setAnalysisClasspath(config.getAnalysisClasspath());
		}

		Map<String, List<ReportedBug>> reportedBugs = null;
		try {
			reportedBugs = findBugsExecutor.execute();
		} catch (IllegalStateException | IOException | InterruptedException e) {
			throw new RepositoryMinerException("Can not execute findbugs.", e);
		}

		List<String> files = getFiles(tmpRepository);
		List<Document> documents = new ArrayList<>();

		for (Entry<String, List<ReportedBug>> bug : reportedBugs.entrySet()) {
			String filename = null;
			for (String file : files) {
				if (file.endsWith(bug.getKey())) {
					filename = file;
					break;
				}
			}
			
			if (filename != null) {
				Document doc = new Document();
				doc.append("reference", snapshot).
					append("commit", commit.getHash()).
					append("commit_date", commit.getCommitterDate()).
					append("repository", repositoryId).
					append("filename", filename).
					append("filehash", StringUtils.encodeToCRC32(filename)).
					append("bugs", ReportedBug.toDocumentList(bug.getValue()));
				documents.add(doc);
			}
		}

		dao.insertMany(documents);
	}

	private List<String> getFiles(String dir) {
		Collection<File> files = FileUtils.listFiles(new File(dir), EXTENSION_FILE_FILTER, true);
		List<String> filepaths = new ArrayList<String>(files.size());
		for (File f : files) {
			filepaths.add(FilenameUtils.normalize(f.getAbsolutePath().substring(dir.length() + 1), true));
		}
		return filepaths;
	}

}