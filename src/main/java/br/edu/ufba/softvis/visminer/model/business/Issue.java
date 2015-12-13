package br.edu.ufba.softvis.visminer.model.business;

import java.util.Date;
import java.util.List;

import br.edu.ufba.softvis.visminer.constant.StatusType;

public class Issue {

  private int id;
  private String creator;
  private String assignee;
  private Date closedDate;
  private int commentsNumber;
  private Date createDate;
  private int number;
  private StatusType status;
  private String title;
  private Date updateDate;
  private String body; 
  private List<Label> labels;
  private Milestone milestone;

  public Issue(){}

  /**
   * @param id
   * @param creator
   * @param assignee
   * @param closedDate
   * @param commentsNumber
   * @param createDate
   * @param number
   * @param status
   * @param title
   * @param updateDate
   * @param body
   * @param labels
   */
  public Issue(int id, String creator, String assignee, Date closedDate, int commentsNumber, Date createDate,
      int number, StatusType status, String title, Date updateDate, String body) {

    super();
    this.id = id;
    this.creator = creator;
    this.assignee = assignee;
    this.closedDate = closedDate;
    this.commentsNumber = commentsNumber;
    this.createDate = createDate;
    this.number = number;
    this.status = status;
    this.title = title;
    this.updateDate = updateDate;
    this.body = body;
  }
  /**
   * @return the id
   */
  public int getId() {
    return id;
  }
  /**
   * @param id the id to set
   */
  public void setId(int id) {
    this.id = id;
  }
  /**
   * @return the creator
   */
  public String getCreator() {
    return creator;
  }
  /**
   * @param creator the creator to set
   */
  public void setCreator(String creator) {
    this.creator = creator;
  }
  /**
   * @return the assignee
   */
  public String getAssignee() {
    return assignee;
  }
  /**
   * @param assignee the assignee to set
   */
  public void setAssignee(String assignee) {
    this.assignee = assignee;
  }
  /**
   * @return the closedDate
   */
  public Date getClosedDate() {
    return closedDate;
  }
  /**
   * @param closedDate the closedDate to set
   */
  public void setClosedDate(Date closedDate) {
    this.closedDate = closedDate;
  }
  /**
   * @return the commentsNumber
   */
  public int getCommentsNumber() {
    return commentsNumber;
  }
  /**
   * @param commentsNumber the commentsNumber to set
   */
  public void setCommentsNumber(int commentsNumber) {
    this.commentsNumber = commentsNumber;
  }
  /**
   * @return the createDate
   */
  public Date getCreateDate() {
    return createDate;
  }
  /**
   * @param createDate the createDate to set
   */
  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }
  /**
   * @return the number
   */
  public int getNumber() {
    return number;
  }
  /**
   * @param number the number to set
   */
  public void setNumber(int number) {
    this.number = number;
  }
  /**
   * @return the status
   */
  public StatusType getStatus() {
    return status;
  }
  /**
   * @param status the status to set
   */
  public void setStatus(StatusType status) {
    this.status = status;
  }
  /**
   * @return the title
   */
  public String getTitle() {
    return title;
  }
  /**
   * @param title the title to set
   */
  public void setTitle(String title) {
    this.title = title;
  }
  /**
   * @return the updateDate
   */
  public Date getUpdateDate() {
    return updateDate;
  }
  /**
   * @param updateDate the updateDate to set
   */
  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }
  /**
   * @return the body
   */
  public String getBody() {
    return body;
  }
  /**
   * @param body the body to set
   */
  public void setBody(String body) {
    this.body = body;
  }
  /**
   * @return the labels
   */
  public List<Label> getLabels() {
    return labels;
  }
  /**
   * @param labels the labels to set
   */
  public void setLabels(List<Label> labels) {
    this.labels = labels;
  }
  /**
   * @return the milestone
   */
  public Milestone getMilestone() {
    return milestone;
  }
  /**
   * @param milestone the milestone to set
   */
  public void setMilestone(Milestone milestone) {
    this.milestone = milestone;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Issue other = (Issue) obj;
    if (id != other.id)
      return false;
    return true;
  }

}