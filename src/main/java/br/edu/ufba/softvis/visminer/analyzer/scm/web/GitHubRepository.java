package br.edu.ufba.softvis.visminer.analyzer.scm.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Label;
import org.eclipse.egit.github.core.Milestone;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.MilestoneService;

import br.edu.ufba.softvis.visminer.constant.StatusType;
import br.edu.ufba.softvis.visminer.model.database.IssueDB;
import br.edu.ufba.softvis.visminer.model.database.LabelDB;
import br.edu.ufba.softvis.visminer.model.database.MilestoneDB;

/**
 * @see WebSCM
 * Implementation for GITHub repositories.
 */
public class GitHubRepository implements WebSCM {

  private IssueService issueServ;
  private MilestoneService milestoneServ;
  private RepositoryId repositoryId;

  @Override
  public void connect(String owner, String name, String user, 
      String password){
    GitHubClient client = new GitHubClient();
    client.setCredentials(user, password);
    init(name, owner, client);
  }

  @Override
  public void connect(String owner, String name){
    GitHubClient client = new GitHubClient();
    init(name, owner, client);
  }

  @Override
  public void connect(String owner, String name, String token) {
    GitHubClient client = new GitHubClient();
    client.setOAuth2Token(token);
    init(name, owner, client);
  }

  @Override
  public List<IssueDB> getAllIssues() {

    int number = 1;
    List<IssueDB> issuesDB = new ArrayList<IssueDB>();

    while(true){

      try{

        Issue issue = issueServ.getIssue(repositoryId, number++);
        IssueDB issueDB = new IssueDB(0, issue.getUser().getLogin(), 
            null, issue.getClosedAt(), issue.getComments(),
            issue.getCreatedAt(), issue.getNumber(), 
            StatusType.parse(issue.getState()), issue.getTitle(),
            issue.getUpdatedAt(),issue.getBody());

        if(issue.getAssignee() != null){
          issueDB.setAssignee(issue.getAssignee().getLogin());
        }

        if(issue.getMilestone() != null){
          MilestoneDB mileDB = new MilestoneDB();
          mileDB.setNumber(issue.getMilestone().getNumber());
          issueDB.setMilestone(mileDB);
        }

        if(issue.getLabels() != null){
          List<LabelDB> labelsDB = new ArrayList<LabelDB>();
          for(Label label : issue.getLabels()){
            LabelDB labelDB = new LabelDB(0, label.getColor(),
                label.getName(), issueDB);
            labelsDB.add(labelDB);
          }
          issueDB.setLabels(labelsDB);
        }

        issuesDB.add(issueDB);

      } catch(IOException e){
        break;
      }

    }

    if(issuesDB.size() == 0)
      return null;

    return issuesDB;

  }

  @Override
  public List<MilestoneDB> getAllMilestones() {

    int number = 1;
    List<MilestoneDB> milesDB = new ArrayList<MilestoneDB>();

    while(true){

      try{

        Milestone mile = milestoneServ.getMilestone(repositoryId, 
            number++);
        MilestoneDB mileDB = new MilestoneDB(0, mile.getClosedIssues(),
            mile.getCreatedAt(), null, mile.getDescription(),
            mile.getDueOn(), mile.getNumber(), mile.getOpenIssues(),
            StatusType.parse(mile.getState()),mile.getTitle());

        if(mile.getCreator() != null){
          mileDB.setCreator(mile.getCreator().getLogin());
        }

        milesDB.add(mileDB);

      }catch(IOException e){
        break;
      }

    }

    if(milesDB.size() == 0)
      return null;

    return milesDB;

  }

  // Initializes repository and needed services.
  private void init(String name, String owner, GitHubClient client){
    repositoryId = new RepositoryId(owner, name);
    issueServ = new IssueService(client);
    milestoneServ = new MilestoneService(client);
  }

}