package br.edu.ufba.softvis.visminer.retriever;

import java.util.List;

import br.edu.ufba.softvis.visminer.model.business.Issue;
import br.edu.ufba.softvis.visminer.model.business.IssueCommand;
import br.edu.ufba.softvis.visminer.model.database.CommitReferenceIssueDB;
import br.edu.ufba.softvis.visminer.model.database.IssueDB;
import br.edu.ufba.softvis.visminer.persistence.dao.CommitReferenceIssueDAO;
import br.edu.ufba.softvis.visminer.persistence.dao.IssueDAO;
import br.edu.ufba.softvis.visminer.persistence.impl.CommitReferenceIssueDAOImpl;
import br.edu.ufba.softvis.visminer.persistence.impl.IssueDAOImpl;

public class IssueRetriever extends Retriever{

  public List<Issue> retrieverByRepository(int repositoryId){

    IssueDAO dao = new IssueDAOImpl();
    super.createEntityManager();
    super.shareEntityManager(dao);

    List<IssueDB> issuesDB = dao.findByRepository(repositoryId);
    super.closeEntityManager();
    return IssueDB.toBusiness(issuesDB);

  }

  public List<IssueCommand> retrieveByCommit(int commitId){

    CommitReferenceIssueDAO criDAO = new CommitReferenceIssueDAOImpl();
    super.createEntityManager();
    super.shareEntityManager(criDAO);

    List<CommitReferenceIssueDB> commandsDB = criDAO.findByCommit(commitId);
    super.closeEntityManager();
    return CommitReferenceIssueDB.toBusiness(commandsDB);

  }
}
