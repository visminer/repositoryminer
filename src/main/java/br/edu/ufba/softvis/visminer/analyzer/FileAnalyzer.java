package br.edu.ufba.softvis.visminer.analyzer;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import br.edu.ufba.softvis.visminer.analyzer.local.IVersioningSystem;
import br.edu.ufba.softvis.visminer.model.database.CommitDB;
import br.edu.ufba.softvis.visminer.model.database.FileDB;
import br.edu.ufba.softvis.visminer.model.database.FileXCommitDB;
import br.edu.ufba.softvis.visminer.persistence.dao.FileDAO;
import br.edu.ufba.softvis.visminer.persistence.dao.FileXCommitDAO;
import br.edu.ufba.softvis.visminer.persistence.impl.FileDAOImpl;
import br.edu.ufba.softvis.visminer.persistence.impl.FileXCommitDAOImpl;

/**
 * @version 0.9
 * Defines how to save or to increment informations about files in database.
 */

public class FileAnalyzer{

	public void persist(List<CommitDB> commitsDb, IVersioningSystem repoSys, EntityManager entityManager) {

		List<FileDB> filesDb = new ArrayList<FileDB>();
		List<FileXCommitDB> filesXCommitsDb = new ArrayList<FileXCommitDB>();
		
		FileXCommitDAO fileXCommitDao = new FileXCommitDAOImpl();
		FileDAO fileDao = new FileDAOImpl();
		
		fileDao.setEntityManager(entityManager);
		fileXCommitDao.setEntityManager(entityManager);
		
		for(CommitDB commitDb : commitsDb){
			
			List<FileDB> files = repoSys.getCommitedFiles(commitDb);
			for(FileDB file : files){
				
				int index = filesDb.indexOf(file);
				FileXCommitDB fxcDb = file.getFileXCommits().get(0);
				
				if(index == -1){
					filesDb.add(file);
					file.setFileXCommits(null);
					fxcDb.setFile(file);
				}else{
					FileDB fileDb = filesDb.get(index);
					fxcDb.setFile(fileDb);
				}

				filesXCommitsDb.add(fxcDb);
				
			}
			
		}
		
		fileDao.batchSave(filesDb);
		
		for(FileXCommitDB fxc : filesXCommitsDb){
			fxc.getId().setFileId(fxc.getFile().getId());
			fxc.setFile(null);
		}
		fileXCommitDao.batchSave(filesXCommitsDb);
		
	}

}
