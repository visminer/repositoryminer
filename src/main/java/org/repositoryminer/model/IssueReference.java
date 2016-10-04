package org.repositoryminer.model;

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