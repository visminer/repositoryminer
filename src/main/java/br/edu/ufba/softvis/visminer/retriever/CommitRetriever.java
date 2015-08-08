package br.edu.ufba.softvis.visminer.retriever;

import java.util.ArrayList;
import java.util.List;

import br.edu.ufba.softvis.visminer.model.business.Commit;
import br.edu.ufba.softvis.visminer.model.business.File;
import br.edu.ufba.softvis.visminer.model.business.FileState;
import br.edu.ufba.softvis.visminer.model.business.Tree;
import br.edu.ufba.softvis.visminer.model.database.CommitDB;
import br.edu.ufba.softvis.visminer.model.database.FileXCommitDB;
import br.edu.ufba.softvis.visminer.persistence.dao.CommitDAO;
import br.edu.ufba.softvis.visminer.persistence.dao.FileXCommitDAO;
import br.edu.ufba.softvis.visminer.persistence.impl.CommitDAOImpl;
import br.edu.ufba.softvis.visminer.persistence.impl.FileXCommitDAOImpl;

public class CommitRetriever extends Retriever {

	private List<File> retrieveFiles(Commit commit) {
		List<File> files = new ArrayList<File>();

		FileXCommitDAO fxcDao = new FileXCommitDAOImpl();
		super.shareEntityManager(fxcDao);

		for (FileXCommitDB fxc : fxcDao.findByCommit(commit.getId())) {
			FileState fileState = new FileState(fxc.getLinesAdded(),
					fxc.getLinesRemoved(), fxc.isRemoved());
			File file = fxc.getFile().toBusiness();
			file.setFileState(fileState);
			files.add(file);
		}

		return files;
	}

	public List<Commit> retrieveByTree(Tree tree) {
		List<Commit> commits = new ArrayList<Commit>();

		CommitDAO dao = super.newDAO(CommitDAOImpl.class);

		List<CommitDB> commitsDb = dao.findByTree(tree.getId());
		if (commitsDb != null) {

			commits.addAll(CommitDB.toBusiness(commitsDb));
			for (Commit commit : commits) {
				commit.setCommitedFiles(retrieveFiles(commit));
			}
		}

		super.closeDAO(dao);

		return commits;
	}

}
