package org.visminer.main;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.visminer.git.remote.Connection;
import org.visminer.git.remote.ConnectionToRepository;
import org.visminer.model.Branch;
import org.visminer.model.Commit;
import org.visminer.model.Committer;
import org.visminer.model.File;
import org.visminer.model.Metric;
import org.visminer.model.Repository;
import org.visminer.model.Tag;
import org.visminer.persistence.BranchDAO;
import org.visminer.persistence.CommitDAO;
import org.visminer.persistence.CommitterDAO;
import org.visminer.persistence.FileDAO;
import org.visminer.persistence.MetricDAO;
import org.visminer.persistence.TagDAO;

public class VisMiner {

	private Repository repository = null;
	private Map<String, String> db_cfg = null;
	private Map<Integer, String> visminer_cfg_local = null;
	private Map<Integer, Object> visminer_cfg_remote = null;
	
	public static final int LOCAL_REPOSITORY_PATH = 0;
	public static final int LOCAL_REPOSITORY_OWNER = 1;
	public static final int LOCAL_REPOSITORY_NAME = 2;
	
	public static final int REMOTE_REPOSITORY_LOGIN = 3;
	public static final int REMOTE_REPOSITORY_PASSWORD = 4;
	
	//Object that implements the interface Connection, this object
	//is some remote repository that works with git
	public static final int REMOTE_REPOSITORY_GIT = 5;

	
	public VisMiner(Map<String, String> db_cfg, Map<Integer, String> visminer_cfg,
			Map<Integer, Object> visminer_cfg_remote) throws IOException, GitAPIException{
		
		this.db_cfg = db_cfg;
		this.visminer_cfg_local = visminer_cfg;
		this.visminer_cfg_remote = visminer_cfg_remote;
		
		this.repository = Initializer.init(this);
		
	}

	/**
	 * 
	 * @param login: for a specified git repository.
	 * @param password: for a specified git repository.
	 * @param repRemote: the object that implements {@link Connection} that is
	 * some remote repository that works with git
	 * 
	 * @return a object for a specified remote repository.
	 */
	public Object getRepositoryRemote(String login, String password, Connection repRemote){
		
		ConnectionToRepository ctr = new ConnectionToRepository(visminer_cfg_local.get(VisMiner.LOCAL_REPOSITORY_OWNER),
				visminer_cfg_local.get(VisMiner.LOCAL_REPOSITORY_NAME), repRemote);
		
		return ctr.getConnection(login, password);
		
	}
		
	public Map<String, String> getDb_cfg() {
		return db_cfg;
	}

	public Map<Integer, String> getVisminer_cfg_local() {
		return visminer_cfg_local;
	}

	public Map<Integer, Object> getVisminer_cfg_remote() {
		return visminer_cfg_remote;
	}

	public Repository getRepository(){
		
		return this.repository;
		
	}
	
	public List<Committer> getCommitters(){
		
		CommitterDAO committerDAO = new CommitterDAO();
		return committerDAO.getByRepository(repository);
	}
	
	public List<Tag> getTags(){
		
		TagDAO tagDAO = new TagDAO();
		return tagDAO.getByRepository(repository);
		
	}
	
	public List<Branch> getBranches(){
		
		BranchDAO branchDAO = new BranchDAO();
		return branchDAO.getByRepository(repository);
		
	}
	
	public List<Commit> getCommits(Committer committer){
		
		CommitDAO commitDAO = new CommitDAO();
		return commitDAO.getByCommitter(committer);
		
	}	
	
	public List<File> getFiles(Commit commit){
		
		FileDAO fileDAO = new FileDAO();
		return fileDAO.getByCommit(commit);
		
	}
	
	public List<Metric> getMetrics(){
		
		MetricDAO metricDAO = new MetricDAO();
		return metricDAO.getAll();
		
	}
	
	public Metric getMetric(String name){
		
		MetricDAO metricDAO = new MetricDAO();
		return metricDAO.getByName(name);
		
	}
	
}