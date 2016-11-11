package org.repositoryminer.findbugs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.DetectorFactoryCollection;
import edu.umd.cs.findbugs.FindBugs2;
import edu.umd.cs.findbugs.Project;
import edu.umd.cs.findbugs.XMLBugReporter;
import edu.umd.cs.findbugs.config.AnalysisFeatureSetting;
import edu.umd.cs.findbugs.config.UserPreferences;

public class FindBugsExecutor {

	private String rootDir;
	private int bugPriority;
	private AnalysisFeatureSetting[] effort;
	private String userPrefsEffort;

	public FindBugsExecutor(String rootDir, int bugPriority, AnalysisFeatureSetting[] effort, String userPrefsEffort) {
		this.rootDir = rootDir;
		this.bugPriority = bugPriority;
		this.effort = effort;
		this.userPrefsEffort = userPrefsEffort;
	}

	public List<ReportedBug> execute() throws IOException, InterruptedException, IllegalStateException {
		FindBugs2 findBugs = new FindBugs2();
		Project project = getProject();

		findBugs.setProject(project);

		XMLBugReporter reporter = new XMLBugReporter(project);
		reporter.setPriorityThreshold(bugPriority);
		reporter.setAddMessages(true);
		reporter.setUseLongBugCodes(true);

		findBugs.setBugReporter(reporter);

		UserPreferences userPrefs = UserPreferences.createDefaultUserPreferences();
		userPrefs.setEffort(userPrefsEffort);
		findBugs.setUserPreferences(userPrefs);

		findBugs.setDetectorFactoryCollection(DetectorFactoryCollection.instance());
		findBugs.setAnalysisFeatureSettings(effort);
		findBugs.finishSettings();

		findBugs.execute();

		List<ReportedBug> reportedBugs = new ArrayList<ReportedBug>();
		for (BugInstance b : reporter.getBugCollection()) {
			ReportedBug rb = new ReportedBug(b.getPrimaryClass().getSourceLines().getSourcePath(), b.getBugRank(),
					b.getPriority(), b.getType(), b.getAbbrev(), b.getBugPattern().getCategory(),
					b.getPrimaryClass().getClassName(), b.getPrimarySourceLineAnnotation().getStartLine(),
					b.getAbridgedMessage(), b.getMessage());
			reportedBugs.add(rb);
		}

		findBugs.dispose();
		return reportedBugs;
	}

	private List<File> getFilesToAnalyze() {
		final String[] suffix = { "class", "java" };
		File root = new File(rootDir);
		Collection<File> files = FileUtils.listFiles(root, suffix, true);
		return new ArrayList<File>(files);
	}

	private Project getProject() throws IOException, IllegalStateException {
		Project findBugsProject = new Project();
		List<File> files = getFilesToAnalyze();

		boolean hasClass = false;

		for (File file : files) {
			String ext = FilenameUtils.getExtension(file.getName());
			findBugsProject.addFile(file.getCanonicalPath());
			if (ext.equals("class")) {
				hasClass = true;
			}
		}

		if (!hasClass) {
			throw new IllegalStateException("Findbugs needs compiled source codes.");
		}

		return findBugsProject;
	}

}