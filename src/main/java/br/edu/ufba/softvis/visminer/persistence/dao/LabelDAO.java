package br.edu.ufba.softvis.visminer.persistence.dao;

import java.util.List;

import br.edu.ufba.softvis.visminer.model.database.LabelDB;

/**
 * Label table DAO interface.
 */
public interface LabelDAO extends DAO<LabelDB, Integer>{

  public List<LabelDB> findByIssue(int issueId);

}
