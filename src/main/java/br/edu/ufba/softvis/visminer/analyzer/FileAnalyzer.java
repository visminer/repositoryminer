package br.edu.ufba.softvis.visminer.analyzer;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import br.edu.ufba.softvis.visminer.analyzer.local.IRepositorySystem;
import br.edu.ufba.softvis.visminer.model.bean.File;
import br.edu.ufba.softvis.visminer.model.bean.FileState;
import br.edu.ufba.softvis.visminer.model.database.CommitDB;
import br.edu.ufba.softvis.visminer.model.database.FileDB;
import br.edu.ufba.softvis.visminer.model.database.FileXCommitDB;
import br.edu.ufba.softvis.visminer.model.database.FileXCommitPK;
import br.edu.ufba.softvis.visminer.persistence.dao.FileDAO;
import br.edu.ufba.softvis.visminer.persistence.dao.FileXCommitDAO;
import br.edu.ufba.softvis.visminer.persistence.impl.FileDAOImpl;
import br.edu.ufba.softvis.visminer.persistence.impl.FileXCommitDAOImpl;

/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * @see CommitAnalyzer
 * @see CommitterAnalyzer
 * @see IssueAnalyzer
 * @see MilestoneAnalyzer
 * @see RepositoryAnalyzer
 * @see TreeAnalyzer
 * @see IAnalyzer
 * 
 * Defines how to save or to increment informations about files in database.
 */

public class FileAnalyzer implements IAnalyzer<Void>{

	@SuppressWarnings("unchecked")
	@Override
	public Void persist(Object... objects) {

		List<CommitDB> commitsDb = (List<CommitDB>) objects[0];
		IRepositorySystem repoSys = (IRepositorySystem) objects[1];
		EntityManager entityManager = (EntityManager) objects[2];
		
		List<FileDB> filesDb = new ArrayList<FileDB>();
		List<FileXCommitDB> filesXCommitsDb = new ArrayList<FileXCommitDB>();
		
		FileXCommitDAO fileXCommitDao = new FileXCommitDAOImpl();
		FileDAO fileDao = new FileDAOImpl();
		
		fileDao.setEntityManager(entityManager);
		fileXCommitDao.setEntityManager(entityManager);
		
		for(CommitDB commitDb : commitsDb){
			
			List<File> files = repoSys.getCommitedFiles(commitDb.getName());
			for(File file : files){
				
				FileDB fileDb = new FileDB(0, file.getPath(), file.getUid());
				int index = filesDb.indexOf(fileDb);
				if(index == -1){
					filesDb.add(fileDb);
				}else{
					fileDb = filesDb.get(index);
				}

				FileXCommitPK fxcPk = new FileXCommitPK(0, commitDb.getId());
				FileState fileState = file.getFileState();
				FileXCommitDB fileXCommitDb = new FileXCommitDB(fxcPk, fileState.getLinesAdded(),
						fileState.getLinesRemoved(), fileState.isDeleted());
				fileXCommitDb.setFile(fileDb);
				filesXCommitsDb.add(fileXCommitDb);
				
			}
			
		}
		
		fileDao.batchSave(filesDb);
		
		for(FileXCommitDB fileXCommitDb : filesXCommitsDb){
			fileXCommitDb.getId().setFileId(fileXCommitDb.getFile().getId());
		}
		
		fileXCommitDao.batchSave(filesXCommitsDb);
		return null;
		
	}

	@Override
	public Void increment(Object... objects) {
		// TODO Auto-generated method stub
		return null;
	}

}
