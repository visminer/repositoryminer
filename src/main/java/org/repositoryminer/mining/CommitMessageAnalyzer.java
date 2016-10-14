package org.repositoryminer.mining;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.repositoryminer.model.IssueReference;
import org.repositoryminer.scm.hostingservice.IssueCommandType;

public class CommitMessageAnalyzer {

	Map<String, IssueCommandType> commandMap;
	Pattern pattern;

	public CommitMessageAnalyzer() {
		commandMap = IssueCommandType.toMap();
		createPattern();
	}

	// Creates regex pattern
	private void createPattern() {
		StringBuilder builder = new StringBuilder();
		builder.append("(");

		Iterator<String> it = commandMap.keySet().iterator();
		while (it.hasNext()) {
			builder.append(it.next() + "|");
		}

		builder.replace(builder.length() - 1, builder.length(), ")");
		builder.append(" #[0-9]+|#[0-9]+");
		pattern = Pattern.compile(builder.toString());
	}

	public List<IssueReference> analyzeMessage(String message) {
		Matcher matcher = pattern.matcher(message);
		List<IssueReference> issueReferences = new ArrayList<IssueReference>();
		
		while (matcher.find()) {
			String frag = matcher.group();
			IssueReference issueRef = null;

			if (frag.startsWith("#") && StringUtils.isNumeric(frag.substring(1))) {
				issueRef = new IssueReference(Integer.valueOf(frag.substring(1)));
			} else {
				// [0] is the command, [1] is the issue
				String[] parts = frag.split(" ");
				if (StringUtils.isNumeric(parts[1].substring(1))) {
					issueRef = new IssueReference(IssueCommandType.parse(parts[0]),
							Integer.valueOf(parts[1].substring(1)));
				}
			}
			
			if (issueRef != null) {
				issueReferences.add(issueRef);
			}
		}
		
		return issueReferences;
	}

}