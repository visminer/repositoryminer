package org.repositoryminer.web.scm.model;

public enum StatusType {

	OPEN("open"), CLOSED("closed");

	private String code;

	private StatusType(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

	public static StatusType parse(String code) {
		for (StatusType issueState : StatusType.values()) {
			if (issueState.getCode().equals(code)) {
				return issueState;
			}
		}
		return null;
	}

}