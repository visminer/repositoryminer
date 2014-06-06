import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.kohsuke.github.GHRepository;
import org.visminer.git.remote.GitHub;
import org.visminer.git.remote.IssueUpdate;
import org.visminer.git.remote.MilestoneUpdate;
import org.visminer.main.VisMiner;
import org.visminer.model.Milestone;
import org.visminer.persistence.RepositoryDAO;


public class Main {
	

	public static void main(String[] args) throws IOException, GitAPIException {

		Map<String, String> props = new HashMap<String, String>(5);
		props.put(PersistenceUnitProperties.JDBC_DRIVER, "com.mysql.jdbc.Driver");
		props.put(PersistenceUnitProperties.JDBC_URL, "jdbc:mysql://localhost/visminer");
		props.put(PersistenceUnitProperties.JDBC_USER, "root");
		props.put(PersistenceUnitProperties.JDBC_PASSWORD, "1234"); 	
		props.put(PersistenceUnitProperties.DDL_GENERATION, "create-tables");

		Map<Integer, String> api_cfg = new HashMap<Integer, String>(3);
		api_cfg.put(VisMiner.LOCAL_REPOSITORY_PATH,
				"C:\\Users\\Felipe\\git\\TestVisMiner\\.git");
		
		/*
		 * TODO Insert the LOCAL_REPOSITORY_NAME and LOCAL_REPOSITORY_OWNER (idGit) without a validation,
		 * can get bad work when update(insert, delete) "Issues" and "Milestones" of remote repository.
		 */
		api_cfg.put(VisMiner.LOCAL_REPOSITORY_NAME, "");
		api_cfg.put(VisMiner.LOCAL_REPOSITORY_OWNER, "");
		
		//these isn't obligatory 
		Map<Integer, Object> api_cfg_remote = new HashMap<Integer, Object>(3);
		api_cfg_remote.put(VisMiner.REMOTE_REPOSITORY_LOGIN, null);
		api_cfg_remote.put(VisMiner.REMOTE_REPOSITORY_PASSWORD, null);
		
		//Object that implements the interface Connection, this object
		//is some remote repository that works with git.
		api_cfg_remote.put(VisMiner.REMOTE_REPOSITORY_GIT, new GitHub());
				
		VisMiner visminer = new VisMiner(props, api_cfg, api_cfg_remote);
		
		/*
		Object gr = visminer.getRepositoryRemote("login", "password", new GitHub());
		MilestoneUpdate.updateMilestone(gr, visminer.getRepository());
		IssueUpdate.updateIssue(gr, visminer.getRepository());*/
	
		System.out.print(visminer.getMetric("NOP").getDescription());

		
	}
	
}