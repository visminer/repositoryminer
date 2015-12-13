package br.edu.ufba.softvis.visminer.analyzer.scm.web;

import java.util.List;

import br.edu.ufba.softvis.visminer.model.database.IssueDB;
import br.edu.ufba.softvis.visminer.model.database.MilestoneDB;

/**
 * Interface that define what is needed to support a web-based repository.
 */
public interface WebSCM {

  /**
   * @param owner
   * @param name
   * @param user
   * @param password
   * Initialize connection with web service using user and password.
   */
  public void connect(String owner, String name, String user, String password);

  /**
   * @param owner
   * @param name
   * Initialize connection with web service without make login, this type 
   * connection has limitations.
   */
  public void connect(String owner, String name);

  /**
   * @param owner
   * @param name
   * @param token
   * Initialize connection with web service using token.
   */
  public void connect(String owner, String name, String token);

  /**
   * @return All issues from web repository service.
   */
  public List<IssueDB> getAllIssues();

  /**
   * @return All milestones from web repository service.
   */
  public List<MilestoneDB> getAllMilestones();

}