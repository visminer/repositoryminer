package br.edu.ufba.softvis.visminer.analyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;

import br.edu.ufba.softvis.visminer.analyzer.scm.SCM;
import br.edu.ufba.softvis.visminer.model.database.CommitDB;
import br.edu.ufba.softvis.visminer.model.database.CommitterDB;
import br.edu.ufba.softvis.visminer.model.database.RepositoryDB;
import br.edu.ufba.softvis.visminer.persistence.dao.CommitDAO;
import br.edu.ufba.softvis.visminer.persistence.dao.CommitterDAO;
import br.edu.ufba.softvis.visminer.persistence.impl.CommitDAOImpl;
import br.edu.ufba.softvis.visminer.persistence.impl.CommitterDAOImpl;

/**
 * Defines how to save or to increment informations about commits and committers in database.
 */
public class CommitAndCommitterAnalyzer{

  public List<CommitDB> persist(RepositoryDB repositoryDb, SCM repoSys, 
      EntityManager entityManager) {

    List<CommitDB> commitsDb = repoSys.getCommits();
    Collections.sort(commitsDb, new Comparator<CommitDB>() {

      @Override
      public int compare(CommitDB o1, CommitDB o2) {
        return o1.getDate().compareTo(o2.getDate());
      }

    });

    List<CommitterDB> committersDb = new ArrayList<CommitterDB>();
    List<CommitterDB> committersDbInsert = new ArrayList<CommitterDB>();

    CommitterDAO committerDao = new CommitterDAOImpl();
    CommitDAO commitDao = new CommitDAOImpl();
    committerDao.setEntityManager(entityManager);
    commitDao.setEntityManager(entityManager);

    for(CommitDB commit : commitsDb){

      CommitterDB committerDbAux = commit.getCommitter();
      CommitterDB committerDb = null;

      if(committersDb.contains(committerDbAux)){

        int index = committersDb.indexOf(committerDbAux);
        committerDb = committersDb.get(index);

      }else if(committersDbInsert.contains(committerDbAux)){

        int index = committersDbInsert.indexOf(committerDbAux);
        committerDb = committersDbInsert.get(index);

      }else{

        committerDb = committerDao.findByEmail(committerDbAux.getEmail());
        if(committerDb == null){
          committerDb = committerDbAux;
          committersDbInsert.add(committerDb);
        }else{
          committersDb.add(committerDb);
        }

        committerDb.setRepositories(Arrays.asList(repositoryDb));

      }

      commit.setCommitter(committerDb);

    }

    committerDao.batchSave(committersDbInsert);
    committerDao.batchMerge(committersDb);
    commitDao.batchSave(commitsDb);

    return commitsDb;

  }

}
