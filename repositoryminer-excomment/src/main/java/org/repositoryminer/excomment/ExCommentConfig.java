package org.repositoryminer.excomment;

public class ExCommentConfig {

	private String commentsCSV;
	private String patternsCSV;
	private String heuristicsCSV;
	private char delimiter = ';';

	public ExCommentConfig() { }
	
	public ExCommentConfig(String commentsCSV, String patternsCSV, String heuristicsCSV) {
		this.commentsCSV = commentsCSV;
		this.patternsCSV = patternsCSV;
		this.heuristicsCSV = heuristicsCSV;
	}

	public boolean isValid() {
		return isValidValue(commentsCSV) && isValidValue(heuristicsCSV) && isValidValue(patternsCSV)
				&& delimiter != '\u0000';
	}

	private boolean isValidValue(String value) {
		return value != null && value.length() > 0;
	}

	public String getCommentsCSV() {
		return commentsCSV;
	}

	public void setCommentsCSV(String commentsCSV) {
		this.commentsCSV = commentsCSV;
	}

	public String getPatternsCSV() {
		return patternsCSV;
	}

	public void setPatternsCSV(String patternsCSV) {
		this.patternsCSV = patternsCSV;
	}

	public String getHeuristicsCSV() {
		return heuristicsCSV;
	}

	public void setHeuristicsCSV(String heuristicsCSV) {
		this.heuristicsCSV = heuristicsCSV;
	}

	public char getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(char delimiter) {
		this.delimiter = delimiter;
	}

}
