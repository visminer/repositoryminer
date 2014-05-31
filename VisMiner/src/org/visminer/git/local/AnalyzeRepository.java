package org.visminer.git.local;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.visminer.metric.IMetric;
import org.visminer.metric.SupportedMetrics;
import org.visminer.model.Branch;
import org.visminer.model.Commit;
import org.visminer.model.Committer;
import org.visminer.model.File;
import org.visminer.model.Metric;
import org.visminer.model.MetricValue;
import org.visminer.model.Repository;
import org.visminer.model.Tag;
import org.visminer.persistence.BranchDAO;
import org.visminer.persistence.CommitterDAO;
import org.visminer.persistence.FileDAO;
import org.visminer.persistence.RepositoryDAO;
import org.visminer.persistence.TagDAO;
import org.visminer.util.DetailAST;

public class AnalyzeRepository implements Runnable{

	private Repository repository;
	private GitUtil gitUtil;
	private List<Commit> commits;
	
	public AnalyzeRepository(String repository_path, String idGit) throws IOException, GitAPIException{
		
		gitUtil = new GitUtil(repository_path, idGit);
		RepositoryDAO repoDAO = new RepositoryDAO();
		repository = repoDAO.save(gitUtil.getRepository());
		
	}
	
	public Repository getRepository(){
		
		return this.repository;
		
	}
	
	private void saveCommitters(){
		
		this.commits = new ArrayList<Commit>();
		
		CommitterDAO committerDAO = new CommitterDAO();
		List<Committer> committers = gitUtil.getCommitters();
		
		for(Committer committer : committers){
			commits.addAll(committer.getCommits());
		}
		
		committerDAO.saveMany(committers);
	}
	
	private void saveBraches() throws GitAPIException{
		
		BranchDAO branchDAO = new BranchDAO();
		List<Branch> branches = gitUtil.getBranchs();
		branchDAO.saveMany(branches);
		
	}
	
	private void saveTags() throws GitAPIException, RevisionSyntaxException, MissingObjectException, IncorrectObjectTypeException, AmbiguousObjectException, IOException{
		
		TagDAO tagDAO = new TagDAO();
		List<Tag> tags = gitUtil.getTags();
		
		for(Tag tag : tags){
			tag.setMetricValues(getTagMetrics(tag));
		}
		
		tagDAO.saveMany(tags);
		
	}

	private List<MetricValue> getTagMetrics(Tag tag) throws RevisionSyntaxException, MissingObjectException, IncorrectObjectTypeException, AmbiguousObjectException, IOException{
		
		Commit lastCommit = gitUtil.getLastCommit(tag.getId().getName());
		DetailAST ast = new DetailAST();
		List<MetricValue> metricsValues = new ArrayList<MetricValue>();
		
		for(IMetric metric : SupportedMetrics.projectMetrics()){
			
			for(String fileName : gitUtil.getFilesNameInVersion(tag.getId().getName())){
				
				String content = gitUtil.getFileStates(lastCommit.getSha(), fileName);
				ast.parserFromString(content);
				metric.calculate(ast);
				
			}
			
			MetricValue metricValue = new MetricValue();
			metricValue.setMetric(new Metric(metric.getId().getValue()));
			metricValue.setValue(metric.getAccumulatedValue());
			metricValue.setTag(tag);
			metricsValues.add(metricValue);
			
		}
		
		return metricsValues;
	}
	
	
	
	private void saveFiles() throws MissingObjectException, IncorrectObjectTypeException, IOException{

		List<File> files = new ArrayList<File>();
		
		for(Commit commit : this.commits){
			
			for(String fileName : gitUtil.getFilesNameInCommit(commit.getSha())){
				File file = new File();
				file.setCommit(commit);
				file.setPath(fileName);
				file.setMetricValues(getMetricsValues(file, commit.getSha()));
				files.add(file);
			}
			
		}
		
		FileDAO fileDAO = new FileDAO();
		fileDAO.saveMany(files);
		
	}
	
	private List<MetricValue> getMetricsValues(File file, String commitSha) throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException{
		
		String content = gitUtil.getFileStates(commitSha, file.getPath());
		
		if(content == null)
			return null;

		List<MetricValue> metricsValues = new ArrayList<MetricValue>();
		
		DetailAST ast = new DetailAST();
		ast.parserFromString(content);
		
		for(IMetric metric : SupportedMetrics.codeMetrics()){
			MetricValue metricValue = new MetricValue();
			metricValue.setMetric(new Metric(metric.getId().getValue()));
			metricValue.setValue(metric.calculate(ast));
			metricValue.setFile(file);
			metricsValues.add(metricValue);
		}
		
		return metricsValues;
		
	}
	
	@Override
	public void run() {

		saveCommitters();
		
		try {
			saveFiles();
			saveBraches();
			saveTags();
		} catch (MissingObjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IncorrectObjectTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
