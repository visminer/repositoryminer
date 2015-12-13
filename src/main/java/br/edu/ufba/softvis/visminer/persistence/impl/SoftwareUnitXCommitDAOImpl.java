package br.edu.ufba.softvis.visminer.persistence.impl;

import java.util.Iterator;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.edu.ufba.softvis.visminer.model.database.SoftwareUnitXCommitDB;
import br.edu.ufba.softvis.visminer.model.database.SoftwareUnitXCommitPK;
import br.edu.ufba.softvis.visminer.persistence.dao.SoftwareUnitXCommitDAO;

/**
 * Implementation of interface {@link SoftwareUnitXCommitDAO}
 */

public class SoftwareUnitXCommitDAOImpl extends DAOImpl<SoftwareUnitXCommitDB,
SoftwareUnitXCommitPK> implements SoftwareUnitXCommitDAO{

  @Override
  public void batchSave(Iterator<Integer> suIds, int commitId) {

    int i = 0;
    EntityManager em = getEntityManager();
    EntityTransaction ts = em.getTransaction();
    ts.begin();

    while(suIds.hasNext()){

      SoftwareUnitXCommitPK pk = new SoftwareUnitXCommitPK(suIds.next(), commitId);
      em.persist(new SoftwareUnitXCommitDB(pk));

      if((i % 1000) == 0){
        ts.commit();
        em.clear();
        ts.begin();
      }
      i++;
      
    }
    
    ts.commit();
    em.clear();

  }

}