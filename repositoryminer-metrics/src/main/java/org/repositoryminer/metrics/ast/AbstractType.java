package org.repositoryminer.metrics.ast;

import java.util.List;

/**
 * This class represents an abstract type declaration.
 * 
 * @see AbstractClass
 */
public class AbstractType {

	private String name;
	private int startPosition;
	private int endPosition;
	private NodeType nodeType;
	private List<AbstractMethod> methods;
	private List<AbstractField> fields;
	private String body;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStartPosition() {
		return startPosition;
	}

	public void setStartPosition(int startPosition) {
		this.startPosition = startPosition;
	}

	public int getEndPosition() {
		return endPosition;
	}

	public void setEndPosition(int endPosition) {
		this.endPosition = endPosition;
	}

	public NodeType getNodeType() {
		return nodeType;
	}

	public void setNodeType(NodeType nodeType) {
		this.nodeType = nodeType;
	}

	public List<AbstractMethod> getMethods() {
		return methods;
	}

	public void setMethods(List<AbstractMethod> methods) {
		this.methods = methods;
	}

	public List<AbstractField> getFields() {
		return fields;
	}

	public void setFields(List<AbstractField> fields) {
		this.fields = fields;
	}
	
	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}


}