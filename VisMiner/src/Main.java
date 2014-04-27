
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
import org.visminer.persistence.Connection;


public class Main {
	
	private static String login = ""; //login of the your GitHub's user
	private static String password = ""; //password of the your GitHub's user
	
	private static String ownerRepository = "";	//login of the owner of a specified repository GitHub
	private static String nameRepository = ""; //name of this specified repository
	

	public static void main(String[] args) throws IOException, GitAPIException {

		Map<String, String> props = new HashMap<String, String>();
		props.put(PersistenceUnitProperties.JDBC_DRIVER, "com.mysql.jdbc.Driver");
		props.put(PersistenceUnitProperties.JDBC_URL, "jdbc:mysql://localhost/visminer");
		props.put(PersistenceUnitProperties.JDBC_USER, "");
		props.put(PersistenceUnitProperties.JDBC_PASSWORD, ""); 
		props.put(PersistenceUnitProperties.DDL_GENERATION, "create-tables");
		Connection.setDataBaseInfo(props);
		
		//Make remote connection in the specified repository and get it
		ConnectionToRepository ctr = new ConnectionToRepository(ownerRepository, nameRepository, NameRepositories.GITHUB);
		GHRepository ghr = (GHRepository) ctr.getConnection(login, password);

		//initialize VisMiner if remote repository exist, and update milestones and issues
		if(ghr != null){
			
			VisMiner visminer = new VisMiner(props, "path...path.../folderGit",
				ownerRepository, nameRepository);
			
			IssueUpdate.updateIssue(ghr, visminer);
			MilestoneUpdate.updateMilestone(ghr, visminer);
			
		}
	
	}
	
}