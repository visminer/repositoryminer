package org.visminer.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.visminer.main.VisMiner;
import org.visminer.model.Committer;
import org.visminer.model.Repository;

public class CommitterDAO{
	
	private org.visminer.persistence.Connection connection = org.visminer.persistence.Connection.getInstance();
	
	public Committer save(Committer committer){
		
		EntityManager em = connection.getEntityManager();
		em.getTransaction().begin();
		committer = em.merge(committer);
		em.getTransaction().commit();
		em.close();
		return committer;
		
	}
	
	public void saveMany(List<Committer> committers){
		
		EntityManager em = connection.getEntityManager();
		em.getTransaction().begin();
		
		for(Committer committer : committers){
			em.merge(committer);
		}
		
		em.getTransaction().commit();
		em.close();
		
	}
	
	public Committer getOneByEmail(Committer committer){
		
		EntityManager em = connection.getEntityManager();
		
		TypedQuery<Committer> query = em.createQuery("select c from Committer c where c.email =:arg1"
				, Committer.class);
		
		query.setParameter("arg1", committer.getEmail());
		
		try{
			
			return query.getSingleResult();
		
		}catch(NoResultException e){
			
			return null;
			
		}
			
	}
	
	 
	public void insertInContributes(Committer committer, VisMiner visminer, Repository repository){
		
		Connection connection;
		try {
			
			connection = DriverManager.getConnection(visminer.getDb_cfg().get(PersistenceUnitProperties.JDBC_URL), 
					visminer.getDb_cfg().get(PersistenceUnitProperties.JDBC_USER),
					visminer.getDb_cfg().get(PersistenceUnitProperties.JDBC_PASSWORD));
			
			java.sql.PreparedStatement preparedStatement = connection.prepareStatement("insert into "
					+"committer_contributes_repository values (?, ?)");
			
				preparedStatement.setString(1, committer.getEmail());
				preparedStatement.setString(2, repository.getIdGit());
				
				preparedStatement.executeUpdate(); // update
				preparedStatement.close();
			
		} catch (SQLException e) {
			
			System.out.println(e.getMessage());
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
				
	}
	
	
	public List<Committer> getByRepository(Repository repository){
		
		EntityManager em = connection.getEntityManager();
		
		TypedQuery<Committer> query = em.createQuery("select c from Committer c where c.repositories =:arg1", Committer.class);
		query.setParameter("arg1", repository);
		
		try{
			return query.getResultList();
		}catch(NoResultException e){
			return null;
		}
		
	}
	
}
