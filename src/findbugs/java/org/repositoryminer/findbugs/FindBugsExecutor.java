package org.repositoryminer.findbugs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.repositoryminer.findbugs.model.ReportedBug;

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

	public FindBugsExecutor(String rootDir) {
		this.rootDir = rootDir;
	}

	public Map<String, List<ReportedBug>> execute() throws IOException, InterruptedException, IllegalStateException {
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

		Map<String, List<ReportedBug>> reportedBugs = new HashMap<String, List<ReportedBug>>();
		for (BugInstance b : reporter.getBugCollection()) {
			String filename = b.getPrimarySourceLineAnnotation().getSourcePath();

			if (!reportedBugs.containsKey(filename)) {
				reportedBugs.put(filename, new ArrayList<ReportedBug>());
			}

			ReportedBug rb = new ReportedBug(b.getBugRank(), b.getBugRankCategory().toString(), b.getPriority(),
					b.getPriorityString(), b.getType(), b.getAbbrev(), b.getBugPattern().getDetailPlainText(),
					b.getBugPattern().getCategory(), b.getPrimaryClass().getClassName(),
					b.getAbridgedMessage(), b.getMessage());

			if (b.getPrimaryMethod() != null) {
				String methodName = b.getPrimaryMethod().getFullMethod(b.getPrimaryClass());
				rb.setMethod(methodName.substring(methodName.lastIndexOf(".")+1));
			}

			if (b.getPrimaryField() != null) {
				rb.setField(b.getPrimaryField().getFieldName());
			}
			
			if (b.getPrimaryLocalVariableAnnotation() != null) {
				rb.setLocalVariable(b.getPrimaryLocalVariableAnnotation().getName());
			}
			
			reportedBugs.get(filename).add(rb);
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

	public void setBugPriority(int bugPriority) {
		this.bugPriority = bugPriority;
	}

	public void setEffort(AnalysisFeatureSetting[] effort) {
		this.effort = effort;
	}

	public void setUserPrefsEffort(String userPrefsEffort) {
		this.userPrefsEffort = userPrefsEffort;
	}

}