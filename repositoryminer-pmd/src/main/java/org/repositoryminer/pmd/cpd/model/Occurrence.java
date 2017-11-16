package org.repositoryminer.pmd.cpd.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

public class Occurrence {

	private String filename;
	private long filehash;
	private int beginLine;
	private int lineCount;
	private double duplicationPercentage;
	private String sourceCodeSlice;

	public Document toDocument() {
		Document doc = new Document();
		doc.append("filename", filename).
			append("filehash", filehash).
			append("begin_line", beginLine).
			append("line_count", lineCount).
			append("duplication_percentage", duplicationPercentage).
			append("source_code_slice", sourceCodeSlice);
			
		return doc;
	}

	public static List<Document> toDocumentList(List<Occurrence> filesInfo) {
		if (filesInfo == null) {
			return new ArrayList<Document>();
		}

		List<Document> docs = new ArrayList<Document>();
		for (Occurrence fi : filesInfo) {
			docs.add(fi.toDocument());
		}

		return docs;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public long getFilehash() {
		return filehash;
	}

	public void setFilehash(long filehash) {
		this.filehash = filehash;
	}

	public int getBeginLine() {
		return beginLine;
	}

	public void setBeginLine(int beginLine) {
		this.beginLine = beginLine;
	}

	public int getLineCount() {
		return lineCount;
	}

	public void setLineCount(int lineCount) {
		this.lineCount = lineCount;
	}

	public double getDuplicationPercentage() {
		return duplicationPercentage;
	}

	public void setDuplicationPercentage(double duplicationPercentage) {
		this.duplicationPercentage = duplicationPercentage;
	}

	public String getSourceCodeSlice() {
		return sourceCodeSlice;
	}

	public void setSourceCodeSlice(String sourceCodeSlice) {
		this.sourceCodeSlice = sourceCodeSlice;
	}

}