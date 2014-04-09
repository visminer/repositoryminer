import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.visminer.main.VisMiner;


public class Main {

	public static void main(String[] args) throws IOException, GitAPIException {

		Map<String, String> props = new HashMap<String, String>();
		props.put(PersistenceUnitProperties.JDBC_DRIVER, "com.mysql.jdbc.Driver");
		props.put(PersistenceUnitProperties.JDBC_URL, "jdbc:mysql://localhost/visminer");
		props.put(PersistenceUnitProperties.JDBC_USER, "root");
		props.put(PersistenceUnitProperties.JDBC_PASSWORD, "1234");
		props.put(PersistenceUnitProperties.DDL_GENERATION, "create-tables");

		VisMiner visMiner = new VisMiner(props, "C:/Users/Felipe/git/Test/.git");

	}
	

	
}