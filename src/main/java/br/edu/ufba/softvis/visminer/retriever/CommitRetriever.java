package br.edu.ufba.softvis.visminer.retriever;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.ufba.softvis.visminer.model.Commit;
import br.edu.ufba.softvis.visminer.model.File;
import br.edu.ufba.softvis.visminer.model.FileState;
import br.edu.ufba.softvis.visminer.model.database.CommitDB;
import br.edu.ufba.softvis.visminer.model.database.FileXCommitDB;
import br.edu.ufba.softvis.visminer.persistence.dao.CommitDAO;
import br.edu.ufba.softvis.visminer.persistence.dao.FileDAO;
import br.edu.ufba.softvis.visminer.persistence.dao.FileXCommitDAO;
import br.edu.ufba.softvis.visminer.persistence.impl.relational.CommitDAOImpl;
import br.edu.ufba.softvis.visminer.persistence.impl.relational.FileDAOImpl;
import br.edu.ufba.softvis.visminer.persistence.impl.relational.FileXCommitDAOImpl;

public class CommitRetriever extends Retriever {

	// this map is used as temporary memory to avoid go to database many times
	private Map<Integer, File> filesDBAux;
	
	private List<File> retrieveFiles(Commit commit) {

		FileDAO fileDAO = new FileDAOImpl();
		super.shareEntityManager(fileDAO);

		FileXCommitDAO fxcDAO = new FileXCommitDAOImpl();
		super.shareEntityManager(fxcDAO);
		
		List<File> files = new ArrayList<File>();
		
		for (FileXCommitDB fxc : fxcDAO.findByCommit(commit.getId())) {
			
			FileState fileState = new FileState(fxc.getLinesAdded(),
					fxc.getLinesRemoved(), fxc.getChangeType());
			
			File fileTemp = filesDBAux.get(fxc.getId().getFileId());
			if(fileTemp == null){
				fileTemp = fileDAO.find(fxc.getId().getFileId()).toBusiness();
				filesDBAux.put(fxc.getId().getFileId(), fileTemp);
			}
			
			File file = new File(fileTemp.getId(), fileTemp.getPath(), fileTemp.getUid());
			file.setFileState(fileState);
			files.add(file);
			
		}
		
		return files;
		
	}

	public List<Commit> retrieveByTree(int treeId) {

		List<Commit> commits = new ArrayList<Commit>();
		CommitDAO dao = new CommitDAOImpl();

		super.createEntityManager();
		super.shareEntityManager(dao);

		filesDBAux = new HashMap<Integer, File>();
		List<CommitDB> commitsDb = dao.findByTree(treeId);
		if (commitsDb != null) {
			commits.addAll(CommitDB.toBusiness(commitsDb));
			for (Commit commit : commits) {
				commit.setCommitedFiles(retrieveFiles(commit));
			}
		}

		filesDBAux = null;
		super.closeEntityManager();
		return commits;
		
	}

	public List<Commit> retrieveByRepository(String repositoryPath){
		
		List<Commit> commits = new ArrayList<Commit>();
		CommitDAO dao = new CommitDAOImpl();

		super.createEntityManager();
		super.shareEntityManager(dao);

		filesDBAux = new HashMap<Integer, File>();
		List<CommitDB> commitsDb = dao.findByRepository(repositoryPath);
		if (commitsDb != null) {
			commits.addAll(CommitDB.toBusiness(commitsDb));
			for (Commit commit : commits) {
				commit.setCommitedFiles(retrieveFiles(commit));
			}
		}

		filesDBAux = null;
		super.closeEntityManager();
		return commits;
		
	}

}