package org.repositoryminer.findbugs;

public class ReportedBug {

	private String filename;
	private int rank;
	private int priority;
	private String type;
	private String abbreviation;
	private String patternCategory;
	private String className;
	private int startline;
	private String shortMessage;
	private String longMessage;
	
	public ReportedBug() {
	}
	
	public ReportedBug(String filename, int rank, int priority, String type, String abbreviation,
			String patternCategory, String className, int startline, String shortMessage, String longMessage) {
		super();
		this.filename = filename;
		this.rank = rank;
		this.priority = priority;
		this.type = type;
		this.abbreviation = abbreviation;
		this.patternCategory = patternCategory;
		this.className = className;
		this.startline = startline;
		this.shortMessage = shortMessage;
		this.longMessage = longMessage;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
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

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public int getStartline() {
		return startline;
	}

	public void setStartline(int startline) {
		this.startline = startline;
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