package org.visminer.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.visminer.metric.IMetric;
import org.visminer.metric.SupportedMetrics;
import org.visminer.model.Commit;
import org.visminer.model.Committer;
import org.visminer.model.File;
import org.visminer.model.Metric;
import org.visminer.model.MetricValue;
import org.visminer.model.Repository;
import org.visminer.model.Version;
import org.visminer.persistence.CommitDAO;
import org.visminer.persistence.CommitterDAO;
import org.visminer.persistence.FileDAO;
import org.visminer.persistence.MetricDAO;
import org.visminer.persistence.MetricValueDAO;
import org.visminer.persistence.RepositoryDAO;
import org.visminer.persistence.VersionDAO;

public class AnalyzeRepository implements Runnable{

	private Repository repository;
	private GitUtil gitUtil;
	private Set<Commit> commits;
	
	public AnalyzeRepository(String repository_path, String idGit) throws IOException, GitAPIException{
		
		gitUtil = new GitUtil(repository_path, idGit);
		RepositoryDAO repoDAO = new RepositoryDAO();
		repository = repoDAO.save(gitUtil.getRepository());
		
	}
	
	public Repository getRepository(){
		return this.repository;
	}
	
	private void saveCommitters(){
		
		CommitterDAO committerDAO = new CommitterDAO();
		List<Committer> committers = gitUtil.getCommitters();
		for(Committer committer : committers){
			
			committer.setRepository(repository);
			committer = committerDAO.save(committer);
			repository.addCommitter(committer);
			
		}
		
	}
	
	private void saveVersions() throws GitAPIException{
		
		VersionDAO versionDAO = new VersionDAO();
		List<Version> versions = gitUtil.getVersions();
		for(Version version : versions){
			
			version.setRepository(repository);
			version = versionDAO.save(version);
			repository.addVersion(version);
			
		}
		
	}
	
	private void saveCommits() throws RevisionSyntaxException, MissingObjectException, IncorrectObjectTypeException, AmbiguousObjectException, IOException{
		
		CommitDAO commitDAO = new CommitDAO();
		VersionDAO versionDAO = new VersionDAO();
		this.commits = new HashSet<Commit>();
		
		for(Version version : repository.getVersions()){
			List<Commit> commits = new ArrayList<Commit>();
			for(Committer committer : repository.getCommitters()){
				for(Commit commit : gitUtil.getCommits(version.getPath(), committer)){
					commitDAO.save(commit);
					commits.add(commit);
					this.commits.add(commit);
				}
			}
			
			version.setCommits(commits);
			versionDAO.save(version);
			saveVersionMetrics(version);
		}
		
	}
	
	private void saveFiles() throws MissingObjectException, IncorrectObjectTypeException, IOException{
		
		FileDAO fileDAO = new FileDAO();
		for(Commit commit : this.commits){
			
			for(String path : gitUtil.getFilesNameInCommit(commit.getSha())){
				
				File file = new File();
				file.setCommit(commit);
				file.setPath(path);
				
				file = fileDAO.save(file);
				saveFileMetrics(file, commit.getSha());
				
			}
			
		}
		
	}

	private void saveFileMetrics(File file, String commitSha) throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException{
		
		MetricValueDAO metricValueDAO = new MetricValueDAO();
		
		String source = gitUtil.getFileStates(commitSha, file.getPath());
		
		if(source == null)
			return;
		
		DetailAST ast = new DetailAST();
		ast.parserFromString(source);
		
		for(IMetric metric : SupportedMetrics.codeMetrics()){
			MetricValue metricValue = new MetricValue();
			Metric metric2 = new Metric(metric.getId().getValue());
			metricValue.setFile(file);
			metricValue.setMetric(metric2);
			metricValue.setValue(metric.calculate(ast));
			
			metricValueDAO.save(metricValue);
			
		}
	}

	private void saveVersionMetrics(Version version) throws RevisionSyntaxException, MissingObjectException, IncorrectObjectTypeException, AmbiguousObjectException, IOException{
		
		MetricValueDAO dao = new MetricValueDAO();
		Commit lastCommit = gitUtil.getLastCommit(version.getPath());
		List<String> files = gitUtil.getFilesNameInVersion(version.getPath());
		
		for(IMetric metric : SupportedMetrics.projectMetrics()){
			for(String file : files){
				
				String content = gitUtil.getFileStates(lastCommit.getSha(), file);
				DetailAST ast = new DetailAST();
				ast.parserFromString(content);
				metric.calculate(ast);
				
			}
			
			MetricValue metricValue = new MetricValue();
			metricValue.setMetric(new Metric(metric.getId().getValue()));
			metricValue.setValue(metric.getAccumulatedValue());
			metricValue.setVersion(version);
			dao.save(metricValue);
		}
		
	}
	
	@Override
	public void run() {

		saveCommitters();
		
		try {
			saveVersions();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
		
		try {
			saveCommits();
			saveFiles();
		} catch (MissingObjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IncorrectObjectTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
