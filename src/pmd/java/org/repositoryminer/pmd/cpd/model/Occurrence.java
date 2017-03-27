package org.repositoryminer.pmd.cpd.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.ast.Language;

public class Occurrence {

	private int lineCount;
	private int tokenCount;
	private String sourceCodeSlice;
	private Language language;
	private List<FileInfo> filesInfo;

	public Document toDocument() {
		Document doc = new Document();
		doc.append("line_count", lineCount).append("token_count", tokenCount)
				.append("source_code_slice", sourceCodeSlice).append("language", language.toString());
		doc.append("files_info", FileInfo.toDocumentList(filesInfo));
		
		return doc;
	}

	public static List<Document> toDocumentList(List<Occurrence> occurrences) {
		if (occurrences == null) {
			return new ArrayList<Document>();
		}
		
		List<Document> docs = new ArrayList<Document>();
		for (Occurrence o : occurrences) {
			docs.add(o.toDocument());
		}
		
		return docs;
	}
	
	public int getLineCount() {
		return lineCount;
	}

	public void setLineCount(int lineCount) {
		this.lineCount = lineCount;
	}

	public int getTokenCount() {
		return tokenCount;
	}

	public void setTokenCount(int tokenCount) {
		this.tokenCount = tokenCount;
	}

	public String getSourceCodeSlice() {
		return sourceCodeSlice;
	}

	public void setSourceCodeSlice(String sourceCodeSlice) {
		this.sourceCodeSlice = sourceCodeSlice;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public List<FileInfo> getFilesInfo() {
		return filesInfo;
	}

	public void setFilesInfo(List<FileInfo> filesInfo) {
		this.filesInfo = filesInfo;
	}

}