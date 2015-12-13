package br.edu.ufba.softvis.visminer.model.business;

import br.edu.ufba.softvis.visminer.constant.IssueCommandType;

public class IssueCommand {

  private Issue issue;
  private IssueCommandType command;

  public IssueCommand(){}

  /**
   * @param command
   */
  public IssueCommand(IssueCommandType command) {
    super();
    this.command = command;
  }
  /**
   * @return the issue
   */
  public Issue getIssue() {
    return issue;
  }
  /**
   * @param issue the issue to set
   */
  public void setIssue(Issue issue) {
    this.issue = issue;
  }
  /**
   * @return the command
   */
  public IssueCommandType getCommand() {
    return command;
  }
  /**
   * @param command the command to set
   */
  public void setCommand(IssueCommandType command) {
    this.command = command;
  }

}
