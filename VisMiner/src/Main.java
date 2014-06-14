
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.visminer.git.remote.GitHub;
import org.visminer.main.VisMiner;
import org.visminer.persistence.ConfigConnectionPostgre;
import org.visminer.persistence.ConfigConnectionMysql;
import org.visminer.persistence.ConfigConnection;


public class Main {
	

	public static void main(String[] args) throws IOException, GitAPIException {

		  ConfigConnection config = new ConfigConnectionMysql();
		  //ConfigConnection config = new ConfigConnectionPostgre();
		   
		   HashMap<String, String> props = config.sgbdConnection();
		 
		   Map<Integer, String> api_cfg_local = new HashMap<Integer, String>(3);
			api_cfg_local.put(VisMiner.LOCAL_REPOSITORY_PATH,
					"C:\\Users\\Luana\\Documents\\GitHub\\Visminer\\.git");
			
			api_cfg_local.put(VisMiner.LOCAL_REPOSITORY_NAME, "visminer");
			api_cfg_local.put(VisMiner.LOCAL_REPOSITORY_OWNER, "luanalima");
		   

		  /*
		  for(Committer committer : visminer.getCommitters()){
		   for(Commit commit : visminer.getCommits(committer)){
		    if(visminer.getFiles(commit).size() > 0){
		     System.out.println(visminer.getFiles(commit).get(0).getPath());
		    }
		   }
		  } */
			Map<Integer, Object> api_cfg_remote = new HashMap<Integer, Object>(3);
			api_cfg_remote.put(VisMiner.REMOTE_REPOSITORY_LOGIN, null);
			api_cfg_remote.put(VisMiner.REMOTE_REPOSITORY_PASSWORD, null);		
			api_cfg_remote.put(VisMiner.REMOTE_REPOSITORY_GIT, new GitHub());
			
			VisMiner visminer = new VisMiner(props, api_cfg_local, api_cfg_remote);
		  System.out.println(visminer.getMetrics().get(0).getDescription());
	}
		
	
}