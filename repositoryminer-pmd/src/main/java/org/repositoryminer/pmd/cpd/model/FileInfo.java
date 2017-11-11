package org.repositoryminer.pmd.cpd.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

public class FileInfo {

	private int beginLine;
	private int endLine;
	private String filename;
	private long filehash;
	private float duplicationPercentage;

	public Document toDocument() {
		Document doc = new Document();
		doc.append("begin_line", beginLine).append("end_line", endLine).append("filename", filename)
				.append("filehash", filehash).append("duplication_percentage", duplicationPercentage);

		return doc;
	}

	public static List<Document> toDocumentList(List<FileInfo> filesInfo) {
		if (filesInfo == null) {
			return new ArrayList<Document>();
		}

		List<Document> docs = new ArrayList<Document>();
		for (FileInfo fi : filesInfo) {
			docs.add(fi.toDocument());
		}

		return docs;
	}

	public int getBeginLine() {
		return beginLine;
	}

	public void setBeginLine(int beginLine) {
		this.beginLine = beginLine;
	}

	public int getEndLine() {
		return endLine;
	}

	public void setEndLine(int endLine) {
		this.endLine = endLine;
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

	public float getDuplicationPercentage() {
		return duplicationPercentage;
	}

	public void setDuplicationPercentage(float duplicationPercentage) {
		this.duplicationPercentage = duplicationPercentage;
	}

}