package org.repositoryminer.checkstyle.audit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.repositoryminer.checkstyle.model.StyleProblem;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;

public class RepositoryMinerAudit implements AuditListener {

	private Map<String, List<StyleProblem>> fileErrors = new HashMap<String, List<StyleProblem>>();
	private String currFile;
	private int repositoryPathEnd;

	public Map<String, List<StyleProblem>> getFileErrors() {
		return fileErrors;
	}

	public void setRepositoryPathLength(int repositoryPathLength) {
		this.repositoryPathEnd = repositoryPathLength + 1;
	}

	@Override
	public void auditStarted(AuditEvent event) {
		// Nothing is needed here
	}

	@Override
	public void auditFinished(AuditEvent event) {
		// Nothing is needed here
	}

	@Override
	public void fileStarted(AuditEvent event) {
		String filename = FilenameUtils.normalize(event.getFileName());
		filename = filename.substring(repositoryPathEnd);
		
		currFile = filename;
		fileErrors.put(currFile, new ArrayList<StyleProblem>());
	}

	@Override
	public void fileFinished(AuditEvent event) {
		if (fileErrors.get(currFile).isEmpty()) {
			fileErrors.remove(currFile);
		}
	}

	@Override
	public void addError(AuditEvent event) {
		int checkerIndex = event.getSourceName().lastIndexOf('.') + 1;
		
		StyleProblem sp = new StyleProblem(event.getLine(), event.getColumn(), event.getMessage(),
				event.getSeverityLevel().getName(), event.getSourceName().substring(checkerIndex));
		fileErrors.get(currFile).add(sp);
	}

	@Override
	public void addException(AuditEvent event, Throwable throwable) {
		// Nothing is needed here
	}

}