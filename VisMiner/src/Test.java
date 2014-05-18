import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.visminer.main.VisMiner;
import org.visminer.model.Commit;
import org.visminer.model.Committer;
import org.visminer.persistence.Connection;


public class Test {
	

	public static void main(String[] args) throws IOException, GitAPIException {

		Map<String, String> props = new HashMap<String, String>();
		props.put(PersistenceUnitProperties.JDBC_DRIVER, "com.mysql.jdbc.Driver");
		props.put(PersistenceUnitProperties.JDBC_URL, "jdbc:mysql://localhost/visminer");
		props.put(PersistenceUnitProperties.JDBC_USER, "root");
		props.put(PersistenceUnitProperties.JDBC_PASSWORD, "frsdrfs"); 
		props.put(PersistenceUnitProperties.DDL_GENERATION, "create-tables");
		Connection.setDataBaseInfo(props);
		
		VisMiner visminer = new VisMiner(props, "D:/GIT/Visminer/.git", "visminer", "visminer");
		
		for(Committer committer : visminer.getCommitters()){
			for(Commit commit : visminer.getCommits(committer)){
				if(visminer.getFiles(commit).size() > 0){
					System.out.println(visminer.getFiles(commit).get(0).getPath());
				}
			}
		}
		System.out.println(visminer.getMetrics().get(0).getDescription());
		
		
		//github part
		/*//Make remote connection in the specified repository and get it
		ConnectionToRepository ctr = new ConnectionToRepository(ownerRepository, nameRepository, NameRepositories.GITHUB);
		GHRepository ghr = (GHRepository) ctr.getConnection(login, password);

		//initialize VisMiner if remote repository exist, and update milestones and issues
		if(ghr != null){
			
			VisMiner visminer = new VisMiner(props, "path...path.../folderGit",
				ownerRepository, nameRepository);
			
			IssueUpdate.updateIssue(ghr, visminer);
			MilestoneUpdate.updateMilestone(ghr, visminer);
			
		}*/
	
	}
	
}