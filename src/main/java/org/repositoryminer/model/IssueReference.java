package org.repositoryminer.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.scm.hostingservice.IssueCommandType;

public class IssueReference {

	private IssueCommandType command;
	private int number;

	public IssueReference() {
	}

	public IssueReference(IssueCommandType command, int number) {
		super();
		this.command = command;
		this.number = number;
	}

	public static List<Document> toDocumentList(List<IssueReference> issuesRefs) {
		List<Document> list = new ArrayList<Document>();
		
		if (issuesRefs == null) {
			return list;
		}
		
		for (IssueReference ir : issuesRefs) {
			Document doc = new Document();
			doc.append("command", ir.getCommand().toString()).append("number", ir.getNumber());
			list.add(doc);
		}
		
		return list;
	}

	public static List<IssueReference> parseDocuments(List<Document> docs) {
		List<IssueReference> list = new ArrayList<IssueReference>();
		for (Document doc : docs) {
			IssueReference ir = new IssueReference(IssueCommandType.parse(doc.getString("command")),
					doc.getInteger("number"));
			list.add(ir);
		}
		return list;
	}

	public IssueReference(int number) {
		super();
		this.number = number;
	}

	public IssueCommandType getCommand() {
		return command;
	}

	public void setCommand(IssueCommandType command) {
		this.command = command;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

}