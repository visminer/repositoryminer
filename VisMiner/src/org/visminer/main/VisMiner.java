package org.visminer.main;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.errors.GitAPIException;
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
	private Map<Integer, String> visminer_cfg = null;
	
	public static final int LOCAL_REPOSITORY_PATH = 0;
	public static final int LOCAL_REPOSITORY_OWNER = 1;
	public static final int LOCAL_REPOSITORY_NAME = 2;
	
	//TODO : for the future
	public static final int REMOTE_REPOSITOY_USER = 3;
	public static final int REMOTE_REPOSITORY_PASSWORD = 4;
	public static final int REMOTE_REPOSITORY_URL = 5;
	
	public VisMiner(Map<String, String> db_cfg, Map<Integer, String> visminer_cfg) throws IOException, GitAPIException{
		
		this.db_cfg = db_cfg;
		this.visminer_cfg = visminer_cfg;
		
		this.repository = Initializer.init(db_cfg, visminer_cfg);
		
	}
	
	public Map<String, String> getDatabaseConfiguration(){
		return this.db_cfg;
	}
	
	public Map<Integer, String> getVisMinerConfiguration(){
		return this.visminer_cfg;
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
