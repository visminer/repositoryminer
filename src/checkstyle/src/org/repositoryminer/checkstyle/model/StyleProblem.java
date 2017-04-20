package org.repositoryminer.checkstyle.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

public class StyleProblem {

	private int line;
	private int column;
	private String message;
	private String severity;
	private String checker;

	public static List<Document> toDocumentList(List<StyleProblem> styleProblems) {
		List<Document> docs = new ArrayList<Document>();
		
		if (styleProblems == null) {
			return docs;
		}
		
		for (StyleProblem e : styleProblems) {
			docs.add(e.toDocument());
		}
		
		return docs;
	}
	
	public Document toDocument() {
		Document doc = new Document();
		return doc.append("line", line).append("column", column).append("message", message).append("severity", severity)
				.append("checker", checker);
	}

	public StyleProblem(int line, int column, String message, String severity, String checker) {
		this.line = line;
		this.column = column;
		this.message = message;
		this.severity = severity;
		this.checker = checker;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getChecker() {
		return checker;
	}

	public void setChecker(String checker) {
		this.checker = checker;
	}

}