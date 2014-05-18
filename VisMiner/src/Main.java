import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.visminer.main.VisMiner;
import org.visminer.model.Branch;
import org.visminer.model.Commit;
import org.visminer.model.Committer;
import org.visminer.model.File;
import org.visminer.model.Metric;
import org.visminer.model.Tag;
import org.visminer.persistence.Connection;
import org.visminer.persistence.RepositoryDAO;


public class Main {
	

	public static void main(String[] args) throws IOException, GitAPIException {

		Map<String, String> props = new HashMap<String, String>();
		props.put(PersistenceUnitProperties.JDBC_DRIVER, "com.mysql.jdbc.Driver");
		props.put(PersistenceUnitProperties.JDBC_URL, "jdbc:mysql://localhost/visminer");
		props.put(PersistenceUnitProperties.JDBC_USER, "root");
		props.put(PersistenceUnitProperties.JDBC_PASSWORD, "1234"); 
		props.put(PersistenceUnitProperties.DDL_GENERATION, "create-tables");
		
		Map<Integer, String> api_cfg = new HashMap<Integer, String>();
		api_cfg.put(VisMiner.LOCAL_REPOSITORY_PATH, "/home/felipe/git/junit/.git");
		api_cfg.put(VisMiner.LOCAL_REPOSITORY_NAME, "junit");
		api_cfg.put(VisMiner.LOCAL_REPOSITORY_OWNER, "felipe");
		
		VisMiner visminer = new VisMiner(props, api_cfg);
		
	
	}
	
}