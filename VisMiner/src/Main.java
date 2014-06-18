import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.visminer.git.remote.GitHub;
import org.visminer.main.VisMiner;
import org.visminer.model.Committer;

/*
 * Class for make tests!
 */
public class Main {
	
	public static void main(String[] args) throws IOException, GitAPIException {
		
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";
		String JDBC_URL = "jdbc:mysql://localhost/";
		String JDBC_USER = "";
		String JDBC_PASSWORD = "";
		String DDL_GENERATION = "create-tables";
		String DB_NAME = "visminer2";
		
		Map<String, String> db_cfg = new HashMap<String, String>(5);
		db_cfg.put(VisMiner.JDBC_DRIVER, JDBC_DRIVER);
		db_cfg.put(VisMiner.JDBC_URL, JDBC_URL);
		db_cfg.put(VisMiner.JDBC_USER, JDBC_USER);
		db_cfg.put(VisMiner.JDBC_PASSWORD, JDBC_PASSWORD); 	
		db_cfg.put(VisMiner.DDL_GENERATION, DDL_GENERATION);
		db_cfg.put(VisMiner.DB_NAME, DB_NAME);

		Map<Integer, String> api_cfg_local = new HashMap<Integer, String>(3);
		api_cfg_local.put(VisMiner.LOCAL_REPOSITORY_PATH,
				"/path/.git");

		api_cfg_local.put(VisMiner.LOCAL_REPOSITORY_NAME, "");
		api_cfg_local.put(VisMiner.LOCAL_REPOSITORY_OWNER, "");
		
		/*
		 * These isn't obligatory, all parameters of "api_cfg_remote" could be
		 * set how null or could be don't set.
		 */
		Map<Integer, Object> api_cfg_remote = new HashMap<Integer, Object>(3);
		api_cfg_remote.put(VisMiner.REMOTE_REPOSITORY_LOGIN, "");
		api_cfg_remote.put(VisMiner.REMOTE_REPOSITORY_PASSWORD, "");
		
		/*
		 * Object that implements the interface Connection, this object
		 * is some remote repository that works with git.
		 */
		api_cfg_remote.put(VisMiner.REMOTE_REPOSITORY_GIT, new GitHub());
			 
		VisMiner visminer = new VisMiner(db_cfg, api_cfg_local, api_cfg_remote);
		
		for(Committer c: visminer.getCommitters())
			System.out.println(c.getEmail());
			
		
	}
	
}