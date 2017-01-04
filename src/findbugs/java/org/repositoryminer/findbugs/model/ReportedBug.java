package org.repositoryminer.findbugs.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

public class ReportedBug {

	private int rank;
	private String priority;
	private String type;
	private String abbreviation;
	private String patternCategory;
	private String clazz;
	private String method;
	private int startline;
	private int endLine;
	private String shortMessage;
	private String longMessage;

	public ReportedBug() {
	}

	public ReportedBug(int rank, String priority, String type, String abbreviation, String patternCategory, String clazz,
			int startline, int endLine, String shortMessage, String longMessage) {
		super();
		this.rank = rank;
		this.priority = priority;
		this.type = type;
		this.abbreviation = abbreviation;
		this.patternCategory = patternCategory;
		this.clazz = clazz;
		this.startline = startline;
		this.endLine = endLine;
		this.shortMessage = shortMessage;
		this.longMessage = longMessage;
	}

	public static List<Document> toDocumentList(List<ReportedBug> reportedBugs) {
		List<Document> list = new ArrayList<Document>();
		for (ReportedBug rb : reportedBugs) {
			Document doc = new Document();
			doc.append("rank", rb.getRank()).append("priority", rb.getPriority()).append("type", rb.getType())
					.append("abbreviation", rb.getAbbreviation()).append("pattern_category", rb.getPatternCategory())
					.append("class", rb.getClazz()).append("method", rb.getMethod())
					.append("startline", rb.getStartline()).append("endline", rb.getEndLine())
					.append("short_message", rb.getShortMessage()).append("long_message", rb.getLongMessage());
			list.add(doc);
		}
		return list;
	}
	
	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getPatternCategory() {
		return patternCategory;
	}

	public void setPatternCategory(String patternCategory) {
		this.patternCategory = patternCategory;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public int getStartline() {
		return startline;
	}

	public void setStartline(int startline) {
		this.startline = startline;
	}

	public int getEndLine() {
		return endLine;
	}

	public void setEndLine(int endLine) {
		this.endLine = endLine;
	}

	public String getShortMessage() {
		return shortMessage;
	}

	public void setShortMessage(String shortMessage) {
		this.shortMessage = shortMessage;
	}

	public String getLongMessage() {
		return longMessage;
	}

	public void setLongMessage(String longMessage) {
		this.longMessage = longMessage;
	}

}