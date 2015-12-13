package br.edu.ufba.softvis.visminer.persistence.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.edu.ufba.softvis.visminer.model.database.FileDB;
import br.edu.ufba.softvis.visminer.persistence.dao.FileDAO;

/**
 * Implementation of interface {@link FileDAO}
 */

@SuppressWarnings("unchecked")
public class FileDAOImpl extends DAOImpl<FileDB, Integer> implements FileDAO {

  @Override
  public List<FileDB> getFilesByUids(List<String> uids) {

    List<FileDB> files = new ArrayList<FileDB>();
    if(uids.isEmpty())
      return files;

    EntityManager em = getEntityManager();
    TypedQuery<FileDB> query = em.createNamedQuery("FileDB.findByUids",
        FileDB.class);
    query.setParameter("uids", uids);
    return getResultList(query);

  }

}