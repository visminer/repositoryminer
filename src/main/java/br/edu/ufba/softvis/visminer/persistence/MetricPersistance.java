package br.edu.ufba.softvis.visminer.persistence;

import javax.persistence.EntityManager;

import br.edu.ufba.softvis.visminer.constant.MetricId;
import br.edu.ufba.softvis.visminer.constant.SoftwareUnitType;
import br.edu.ufba.softvis.visminer.model.bean.Commit;
import br.edu.ufba.softvis.visminer.model.bean.File;
import br.edu.ufba.softvis.visminer.model.bean.SoftwareUnit;
import br.edu.ufba.softvis.visminer.model.database.CommitDB;
import br.edu.ufba.softvis.visminer.model.database.FileDB;
import br.edu.ufba.softvis.visminer.model.database.MetricDB;
import br.edu.ufba.softvis.visminer.model.database.MetricValueDB;
import br.edu.ufba.softvis.visminer.model.database.MetricValuePK;
import br.edu.ufba.softvis.visminer.model.database.RepositoryDB;
import br.edu.ufba.softvis.visminer.model.database.SoftwareUnitDB;
import br.edu.ufba.softvis.visminer.model.database.SoftwareUnitXCommitDB;
import br.edu.ufba.softvis.visminer.model.database.SoftwareUnitXCommitPK;
import br.edu.ufba.softvis.visminer.persistence.dao.MetricValueDAO;
import br.edu.ufba.softvis.visminer.persistence.dao.SoftwareUnitDAO;
import br.edu.ufba.softvis.visminer.persistence.dao.SoftwareUnitXCommitDAO;
import br.edu.ufba.softvis.visminer.persistence.impl.MetricValueDAOImpl;
import br.edu.ufba.softvis.visminer.persistence.impl.SoftwareUnitDAOImpl;
import br.edu.ufba.softvis.visminer.persistence.impl.SoftwareUnitXCommitImpl;
import br.edu.ufba.softvis.visminer.utility.StringUtils;

public class MetricPersistance {

	private CommitDB commitDb;
	private RepositoryDB repositoryDb;
	private EntityManager entityManager;
	private MetricDB metricDB;

	private SoftwareUnitDAO softUnitDao;
	private SoftwareUnitXCommitDAO softUnitXCommitDao;
	private MetricValueDAO metricValueDao;

	public MetricPersistance(CommitDB commitDb, RepositoryDB repositoryDb, EntityManager entityManager) {

		this.entityManager = entityManager;
		this.repositoryDb = repositoryDb;
		this.commitDb = commitDb;

		this.softUnitDao = new SoftwareUnitDAOImpl();
		this.softUnitDao.setEntityManager(entityManager);

		this.softUnitXCommitDao = new SoftwareUnitXCommitImpl();
		this.softUnitXCommitDao.setEntityManager(entityManager);

		this.metricValueDao = new MetricValueDAOImpl();
		this.metricValueDao.setEntityManager(entityManager);

	}

	public void setMetricDB(MetricDB metricDB) {
		this.metricDB = metricDB;
	}

	public SoftwareUnit saveSoftwareUnit(File file, SoftwareUnit softwareUnit){

		String uid = generateUid(file, softwareUnit.getParentUnit(), softwareUnit.getFullName(), softwareUnit.getType());
		SoftwareUnitDB softUnitDb = softUnitDao.findByUid(uid);

		// insert in database
		if(softUnitDb == null){

			softwareUnit.setUid(uid);;
			softUnitDb = new SoftwareUnitDB(softwareUnit);
			softUnitDb.setRepository(repositoryDb);

			if(file != null){
				softUnitDb.setFile(new FileDB(file));
			}

			if(softwareUnit.getParentUnit() != null){
				softUnitDb.setSoftwareUnit( new SoftwareUnitDB(softwareUnit.getParentUnit()) );
			}

			softUnitDao.save(softUnitDb);

			SoftwareUnitXCommitPK softXCommitPk = new SoftwareUnitXCommitPK(commitDb.getId(), softUnitDb.getId());
			softUnitXCommitDao.save(new SoftwareUnitXCommitDB(softXCommitPk));

		}else{

			SoftwareUnitXCommitPK softXCommitPk = new SoftwareUnitXCommitPK(commitDb.getId(), softUnitDb.getId());
			SoftwareUnitXCommitDB softXCommitDb = softUnitXCommitDao.find(softXCommitPk);

			if(softXCommitDb == null){
				softXCommitDb = new SoftwareUnitXCommitDB(softXCommitPk);
				softUnitXCommitDao.save(softXCommitDb);
			}

		}

		return new SoftwareUnit(softUnitDb);

	}

	public SoftwareUnit saveMetricValue(File file, SoftwareUnit softwareUnit, String value){

		SoftwareUnit softUnit = saveSoftwareUnit(file, softwareUnit);
		MetricValuePK metricValPk = new MetricValuePK(softUnit.getId(), commitDb.getId(), metricDB.getId().getId());
		MetricValueDB metricValDb =  new MetricValueDB(metricValPk);
		metricValDb.setValue(value);

		metricValueDao.merge(metricValDb);

		return softUnit;

	}

	public SoftwareUnit getSoftwareUnit(File file, SoftwareUnit parent, String fullName, SoftwareUnitType type){

		String uid = generateUid(file, parent, fullName, type);
		SoftwareUnitDB softUnitDb = softUnitDao.findByUid(uid);

		if(softUnitDb == null){
			return null;
		}

		return new SoftwareUnit(softUnitDb);

	}


	public String getMetricValue(MetricId metricId, int softUnitId, Commit commitPrev){

		int id;
		if(commitPrev == null){
			id = this.commitDb.getId();
		}else{
			id = commitPrev.getId();
		}

		MetricValuePK pk = new MetricValuePK(softUnitId, id, metricId.getId());
		MetricValueDB metricValDb = metricValueDao.find(pk);

		if(metricValDb == null){
			return null;
		}

		return metricValDb.getValue();

	}


	// helpers

	// generates unique id for software units
	private String generateUid(File file, SoftwareUnit parent, String fullName, SoftwareUnitType type){

		String repositoryPath = repositoryDb.getPath();

		String filePath = null;
		if(file != null){
			filePath = file.getPath();
		}

		String parentPath = null;
		if(parent != null){
			parentPath = parent.getUid();
		}

		String uid = null;
		if(type == SoftwareUnitType.PROJECT || type == SoftwareUnitType.PACKAGE
				|| type == SoftwareUnitType.FILE){
			uid = repositoryPath+"/"+parentPath+"/"+filePath+"/"+type.toString()+"/"+fullName;
		}else{
			uid = repositoryPath+"/"+filePath+"/"+parentPath+"/"+type.toString()+"/"+fullName;
		}

		return StringUtils.sha1(uid);

	}

}