
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.kohsuke.github.GHRepository;

import org.visminer.git.remote.ConnectionToRepository;
import org.visminer.git.remote.IssueUpdate;
import org.visminer.git.remote.MilestoneUpdate;
import org.visminer.git.remote.NameRepositories;
import org.visminer.main.VisMiner;
import org.visminer.model.Committer;
import org.visminer.persistence.Connection;


public class Main {
	
	private static String loginGitHub = ""; //login of the your GitHub's user
	private static String passwordGitHub = ""; //password of the your GitHub's user
	
	private static String ownerRepositoryGitHub = "visminer";	//login of the owner of a specified repository GitHub
	private static String nameRepositoryGitHub = "Visminer"; //name of this specified repository
	private static String urlLocalGitRepository = "/home/heron/workspaces-eclipses/workspace_pibiti/Visminer/";

	public static void main(String[] args) throws IOException, GitAPIException {

		Map<String, String> props = new HashMap<String, String>();
		props.put(PersistenceUnitProperties.JDBC_DRIVER, "com.mysql.jdbc.Driver");
		props.put(PersistenceUnitProperties.JDBC_URL, "jdbc:mysql://localhost/visminer");
		props.put(PersistenceUnitProperties.JDBC_USER, "");
		props.put(PersistenceUnitProperties.JDBC_PASSWORD, ""); 
		props.put(PersistenceUnitProperties.DDL_GENERATION, "create-tables");
		Connection.setDataBaseInfo(props);
		
		
		//if you want to use github to access milestones and issues 
		if (!loginGitHub.equals("") && !passwordGitHub.equals("") && !ownerRepositoryGitHub.equals("") && !nameRepositoryGitHub.equals("")){
			//Make remote connection in the specified repository and get it
			ConnectionToRepository ctr = new ConnectionToRepository(ownerRepositoryGitHub, nameRepositoryGitHub, NameRepositories.GITHUB);
			GHRepository gh = (GHRepository) ctr.getConnection(loginGitHub, passwordGitHub);

			//initialize VisMiner if remote repository exist, and update milestones(insert, delete, update) 
			//and issues(insert, update)
			if(gh != null){
				
				//TODO ENHANCEMENT verifying to put automatic update the local repository path the database, if the user 
				//modify the local path in your computer
				VisMiner visminer = new VisMiner(props, urlLocalGitRepository,
					ownerRepositoryGitHub, nameRepositoryGitHub);
				
				IssueUpdate.updateIssue(gh, visminer);
				MilestoneUpdate.updateMilestone(gh, visminer);
				
			}
		}else{
			VisMiner visminer = new VisMiner(props, urlLocalGitRepository,
					null, null);
			
		}
		
	
	}
	
}