package br.edu.ufba.softvis.visminer.analyzer;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import br.edu.ufba.softvis.visminer.analyzer.local.IRepositorySystem;
import br.edu.ufba.softvis.visminer.model.bean.Committer;
import br.edu.ufba.softvis.visminer.model.database.CommitterDB;
import br.edu.ufba.softvis.visminer.model.database.CommitterRoleDB;
import br.edu.ufba.softvis.visminer.model.database.RepositoryDB;
import br.edu.ufba.softvis.visminer.persistence.dao.CommitterDAO;
import br.edu.ufba.softvis.visminer.persistence.dao.CommitterRoleDAO;
import br.edu.ufba.softvis.visminer.persistence.impl.CommitterDAOImpl;
import br.edu.ufba.softvis.visminer.persistence.impl.CommitterRoleDAOImpl;

public class CommitterAnalyzer implements IAnalyzer<List<CommitterDB>> {

	@Override
	public List<CommitterDB> persist(Object... objects) {

		RepositoryDB repoDb = (RepositoryDB) objects[0];
		IRepositorySystem repoSys = (IRepositorySystem) objects[1];
		EntityManager entityManager = (EntityManager) objects[2];
		
		CommitterDAO committerDao = new CommitterDAOImpl();
		CommitterRoleDAO committerRoleDao = new CommitterRoleDAOImpl();
		
		committerDao.setEntityManager(entityManager);
		committerRoleDao.setEntityManager(entityManager);
		
		List<Committer> committersAux = repoSys.getCommitters();
		
		List<CommitterDB> committersDbResp = new ArrayList<CommitterDB>(committersAux.size()); // this will be returned
		List<CommitterDB> committersDbSave = new ArrayList<CommitterDB>(); // this will be used in batch persist
		List<CommitterRoleDB> committersRoleDb = new ArrayList<CommitterRoleDB>(committersAux.size()); // this will be used in batch persist
		
		/* verify committers that already exists, puts them in the return list and set its roles in repository*/
		for(int i = 0; i < committersAux.size(); i++){
			Committer committerBean = committersAux.get(i);
			CommitterDB committerDb = committerDao.findByEmail(committerBean.getEmail());
			if(committerDb != null){
				committersDbResp.add(committerDb);
				CommitterRoleDB role = new CommitterRoleDB(repoDb.getId(), committerDb.getId(), committerBean.isContribuitor());
				committersRoleDb.add(role);
				committersAux.remove(i);
			}
			
		}
		
		int nextRole = committersRoleDb.size();
		
		/* save the new committers and set its roles in repository */
		for(int i = 0; i < committersAux.size(); i++){
			
			Committer committerBean = committersAux.get(i);
			CommitterDB committerDb = new CommitterDB(committerBean.getEmail(), committerBean.getName());
			CommitterRoleDB role = new CommitterRoleDB(repoDb.getId(), 0, committerBean.isContribuitor());
			role.setCommitter(committerDb);
			
			committersDbSave.add(committerDb);
			committersRoleDb.add(role);
			
		}
		
		committerDao.batchSave(committersDbSave);
		committersDbResp.addAll(committersDbSave);
		
		/* set id of new committers in their repository roles */
		for(int i = nextRole; i < committersRoleDb.size(); i++){
			CommitterRoleDB role = committersRoleDb.get(i);
			role.getId().setCommitterId(role.getCommitter().getId());
		}
		
		committerRoleDao.batchSave(committersRoleDb);
		
		return committersDbResp;
		
	}

	@Override
	public List<CommitterDB> update(Object... objects) {
		return null;
	}

}
