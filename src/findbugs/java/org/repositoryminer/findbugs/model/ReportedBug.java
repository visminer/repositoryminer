package org.repositoryminer.findbugs.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

public class ReportedBug {

	private int rank;
	private String rankCategory;
	private int priority;
	private String priorityCategory;
	private String type;
	private String abbreviation;
	private String description;
	private String category;
	private String clazz;
	private String field;
	private String method;
	private String localVariable;
	private String shortMessage;
	private String longMessage;

	public ReportedBug() {
	}

	public ReportedBug(int rank, String rankCategory, int priority, String priorityCategory, String type,
			String abbreviation, String description, String category, String clazz, String shortMessage,
			String longMessage) {
		super();
		this.rank = rank;
		this.rankCategory = rankCategory;
		this.priority = priority;
		this.priorityCategory = priorityCategory;
		this.type = type;
		this.abbreviation = abbreviation;
		this.description = description;
		this.category = category;
		this.clazz = clazz;
		this.shortMessage = shortMessage;
		this.longMessage = longMessage;
	}

	public static List<Document> toDocumentList(List<ReportedBug> reportedBugs) {
		List<Document> list = new ArrayList<Document>();
		for (ReportedBug rb : reportedBugs) {
			Document doc = new Document();
			doc.append("rank", rb.getRank()).append("rank_category", rb.getRankCategory())
					.append("priority", rb.getPriority()).append("priority_category", rb.getPriorityCategory())
					.append("type", rb.getType()).append("abbreviation", rb.getAbbreviation())
					.append("description", rb.getDescription()).append("category", rb.getCategory())
					.append("class", rb.getClazz()).append("field", rb.getField()).append("method", rb.getMethod())
					.append("local_variable", rb.getLocalVariable()).append("short_message", rb.getShortMessage())
					.append("long_message", rb.getLongMessage());
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

	public String getRankCategory() {
		return rankCategory;
	}

	public void setRankCategory(String rankCategory) {
		this.rankCategory = rankCategory;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getPriorityCategory() {
		return priorityCategory;
	}

	public void setPriorityCategory(String priorityCategory) {
		this.priorityCategory = priorityCategory;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getLocalVariable() {
		return localVariable;
	}

	public void setLocalVariable(String localVariable) {
		this.localVariable = localVariable;
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