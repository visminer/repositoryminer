package org.visminer.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.visminer.model.Branch;
import org.visminer.model.Repository;

public class BranchDAO {

	private Connection connection = Connection.getInstance();
	
	public Branch save(Branch branch){
		
		EntityManager em = connection.getEntityManager();
		em.getTransaction().begin();
		branch = em.merge(branch);
		em.getTransaction().commit();
		em.close();
		return branch;
		
	}
	
	public void saveMany(List<Branch> branches){
		
		EntityManager em = connection.getEntityManager();
		em.getTransaction().begin();
		
		for(Branch branch : branches){
			branch = em.merge(branch);
		}
		
		em.getTransaction().commit();
		em.close();		
		
	}
	
	public List<Branch> getByRepository(Repository repository){
		
		EntityManager em = connection.getEntityManager();
		TypedQuery<Branch> query = em.createQuery("select b from Branch b where b.repository.idGit = :arg0", Branch.class);
		query.setParameter("arg0", repository.getIdGit());
		try{
			return query.getResultList();
		}catch(NoResultException e){
			return null;
		}
	}
	
}
