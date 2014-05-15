
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.kohsuke.github.GHRepository;
import org.omg.PortableServer.Servant;
import org.visminer.constants.Services;
import org.visminer.git.remote.ConnectionToRepository;
import org.visminer.git.remote.IssueUpdate;
import org.visminer.git.remote.MilestoneUpdate;
import org.visminer.main.VisMiner;
import org.visminer.model.Committer;
import org.visminer.persistence.Connection;


public class Main {

	public static void main(String[] args) throws IOException, GitAPIException {

		Map<String, String> db_cfg = new HashMap<String, String>();
		db_cfg.put(PersistenceUnitProperties.JDBC_DRIVER, "com.mysql.jdbc.Driver");
		db_cfg.put(PersistenceUnitProperties.JDBC_URL, "jdbc:mysql://localhost/visminer");
		db_cfg.put(PersistenceUnitProperties.JDBC_USER, "");
		db_cfg.put(PersistenceUnitProperties.JDBC_PASSWORD, ""); 
		db_cfg.put(PersistenceUnitProperties.DDL_GENERATION, "create-tables");
		
		Map<String, String> visminer_cfg = new HashMap<String, String>();
		visminer_cfg.put(VisMiner.LOCAL_REPOSITORY_PATH, "/home/heron/workspaces-eclipses/workspace_pibiti/Visminer/");//not opitional
		visminer_cfg.put(VisMiner.REMOTE_REPOSITORY_NAME, "Visminer");//opitional
		visminer_cfg.put(VisMiner.REMOTE_REPOSITORY_OWNER, "visminer");//opitional
		visminer_cfg.put(VisMiner.REMOTE_REPOSITORY_PASSWORD, "");//opitional
		visminer_cfg.put(VisMiner.REMOTE_REPOSITORY_SERVICE, Services.BITBUCKET.name());//opitional
		visminer_cfg.put(VisMiner.REMOTE_REPOSITORY_USER, "");//opitional
		//the optional fields can be removed, left they here only for example
		
		VisMiner visMiner = new VisMiner(visminer_cfg, db_cfg);
		
		/*
		//if you want to use github to access milestones and issues 
		if (!loginGitHub.equals("") && !passwordGitHub.equals("") && !ownerRepositoryGitHub.equals("") && !nameRepositoryGitHub.equals("")){
			//Make remote connection in the specified repository and get it
			ConnectionToRepository ctr = new ConnectionToRepository(ownerRepositoryGitHub, nameRepositoryGitHub, Services.GITHUB);
			GHRepository gh = (GHRepository) ctr.getConnection(loginGitHub, passwordGitHub);

			//initialize VisMiner if remote repository exist, and update milestones(insert, delete, update) 
			//and issues(insert, update)
			if(gh != null){
				
				//TODO ENHANCEMENT verifying to put automatic update the local repository path the database, if the user 
				//modify the local path in your computer
				VisMiner visminer = new VisMiner(db_cfg, urlLocalGitRepository,
					ownerRepositoryGitHub, nameRepositoryGitHub);
				
				IssueUpdate.updateIssue(gh, visminer);
				MilestoneUpdate.updateMilestone(gh, visminer);
				
			}
		}else{
			VisMiner visminer = new VisMiner(db_cfg, urlLocalGitRepository,
					null, null);
			
		}
		*/
		
	
	}
	
}