package br.edu.ufba.softvis.visminer.persistence.dao;


import java.util.Iterator;

import br.edu.ufba.softvis.visminer.model.database.SoftwareUnitXCommitDB;
import br.edu.ufba.softvis.visminer.model.database.SoftwareUnitXCommitPK;

/**
 * Software_unit_x_commit table DAO interface.
 */
public interface SoftwareUnitXCommitDAO extends DAO<SoftwareUnitXCommitDB, SoftwareUnitXCommitPK> {

  public void batchSave(Iterator<Integer> suIds, int commitId);
  
}
