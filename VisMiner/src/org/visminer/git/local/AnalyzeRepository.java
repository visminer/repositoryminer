package org.visminer.git.local;

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
import org.visminer.persistence.MetricValueDAO;
import org.visminer.persistence.RepositoryDAO;
import org.visminer.persistence.VersionDAO;
import org.visminer.util.DetailAST;

/**
 * <p>
 * Save informations from local git repository in database
 * </p>
 * 
 * @author Felipe
 * @version 1.0
 */
public class AnalyzeRepository implements Runnable{

	private Repository repository;
	private GitLocal gitLocal;
	private Set<Commit> commits;
	
	/**
	 * 
	 * @param repository_path : local git repository path
	 * @param idGit : <repository owner>/<repository name>
	 * @throws IOException
	 * @throws GitAPIException
	 */
	public AnalyzeRepository(String repository_path, String idGit) throws IOException, GitAPIException{
		
		gitLocal = new GitLocal(repository_path);
		RepositoryDAO repoDAO = new RepositoryDAO();
		repository = gitLocal.getRepository();
		repository.setIdGit(idGit);
		repoDAO.save(repository);
		
	}
	
	/**
	 * 
	 * @return repository analyzed
	 */
	public Repository getRepository(){
		return this.repository;
	}
	
	/**
	 * <p>
	 * Save repository committers in database
	 * </p>
	 */
	private void saveCommitters(){
		
		CommitterDAO committerDAO = new CommitterDAO();
		List<Committer> committers = gitLocal.getCommitters();
		for(Committer committer : committers){
			
			committer.setRepository(repository);
			committer = committerDAO.save(committer);
			repository.addCommitter(committer);
			
		}
		
	}
	
	/**
	 * <p>
	 * Save repository versions in database
	 * </p>
	 * @throws GitAPIException
	 */
	private void saveVersions() throws GitAPIException{
		
		VersionDAO versionDAO = new VersionDAO();
		List<Version> versions = gitLocal.getVersions();
		for(Version version : versions){
			
			version.setRepository(repository);
			version = versionDAO.save(version);
			repository.addVersion(version);
			
		}
		
	}
	
	/**
	 * <p>
	 * Save repository commits in database
	 * </p>
	 * 
	 * @throws RevisionSyntaxException
	 * @throws MissingObjectException
	 * @throws IncorrectObjectTypeException
	 * @throws AmbiguousObjectException
	 * @throws IOException
	 */
	private void saveCommits() throws RevisionSyntaxException, MissingObjectException, IncorrectObjectTypeException, AmbiguousObjectException, IOException{
		
		CommitDAO commitDAO = new CommitDAO();
		VersionDAO versionDAO = new VersionDAO();
		this.commits = new HashSet<Commit>();
		
		for(Version version : repository.getVersions()){
			List<Commit> commits = new ArrayList<Commit>();
			for(Committer committer : repository.getCommitters()){
				for(Commit commit : gitLocal.getCommits(version.getPath(), committer)){
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
	
	/**
	 * <p>
	 * Save files in repository commit and the metrics values of  files states
	 * </p>
	 * 
	 * @throws MissingObjectException
	 * @throws IncorrectObjectTypeException
	 * @throws IOException
	 */
	private void saveFiles() throws MissingObjectException, IncorrectObjectTypeException, IOException{
		
		FileDAO fileDAO = new FileDAO();
		for(Commit commit : this.commits){
			
			for(String path : gitLocal.getFilesNameInCommit(commit.getSha())){
				
				File file = new File();
				file.setCommit(commit);
				file.setPath(path);
				
				file = fileDAO.save(file);
				saveFileMetrics(file, commit.getSha());
				
			}
			
		}
		
	}

	//save file metrics values
	private void saveFileMetrics(File file, String commitSha) throws MissingObjectException, IncorrectObjectTypeException, CorruptObjectException, IOException{
		
		MetricValueDAO metricValueDAO = new MetricValueDAO();
		
		String source = gitLocal.getFileStates(commitSha, file.getPath());
		
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

	//save versions metrics values
	private void saveVersionMetrics(Version version) throws RevisionSyntaxException, MissingObjectException, IncorrectObjectTypeException, AmbiguousObjectException, IOException{
		
		MetricValueDAO dao = new MetricValueDAO();
		Commit lastCommit = gitLocal.getLastCommit(version.getPath());
		List<String> files = gitLocal.getFilesNameInVersion(version.getPath());
		
		for(IMetric metric : SupportedMetrics.projectMetrics()){
			for(String file : files){
				
				String content = gitLocal.getFileStates(lastCommit.getSha(), file);
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
