package org.visminer.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the label database table.
 * 
 */
@Entity
@Table(name="label")
@NamedQuery(name="Label.findAll", query="SELECT l FROM Label l")
public class Label implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private LabelPK id;

	@Column(name="color", nullable=false, length=6)
	private String color;

	@Column(name="name", nullable=false, length=255)
	private String name;

	@Column(name="url", nullable=false, length=255)
	private String url;

	//bi-directional many-to-one association to Issue
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="issue_number", referencedColumnName="number"),
		@JoinColumn(name="issue_repository_id_git", referencedColumnName="repository_id_git")
		})
	private Issue issue;

	public Label() {
	}

	public LabelPK getId() {
		return this.id;
	}

	public void setId(LabelPK id) {
		this.id = id;
	}

	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Issue getIssue() {
		return this.issue;
	}

	public void setIssue(Issue issue) {
		this.issue = issue;
	}

}