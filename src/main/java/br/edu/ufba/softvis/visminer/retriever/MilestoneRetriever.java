package br.edu.ufba.softvis.visminer.retriever;

import java.util.List;

import br.edu.ufba.softvis.visminer.model.business.Milestone;
import br.edu.ufba.softvis.visminer.model.database.MilestoneDB;
import br.edu.ufba.softvis.visminer.persistence.dao.MilestoneDAO;
import br.edu.ufba.softvis.visminer.persistence.impl.MilestoneDAOImpl;

public class MilestoneRetriever extends Retriever{

	public List<Milestone> retrieverByRepository(int repositoryId){
		
		MilestoneDAO dao = new MilestoneDAOImpl();
		super.createEntityManager();
		super.shareEntityManager(dao);
		
		List<MilestoneDB> milesDB = dao.findByRepository(repositoryId);
		super.closeEntityManager();
		return MilestoneDB.toBusiness(milesDB);
		
	}
	
}
