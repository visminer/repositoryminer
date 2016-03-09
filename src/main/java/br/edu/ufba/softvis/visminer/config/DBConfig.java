package br.edu.ufba.softvis.visminer.config;

import java.util.Arrays;

import br.edu.ufba.softvis.visminer.utility.PropertyReader;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

/**
 * @version 0.9 Database configuration bean.
 */
public class DBConfig {

	public static final String PROP_MONGO_HOST = "mongo.host";
	public static final String PROP_MONGO_PORT = "mongo.port";
	public static final String PROP_MONGO_DBNAME = "mongo.dbname";
	public static final String PROP_MONGO_USER = "mongo.user";
	public static final String PROP_MONGO_PASS = "mongo.password";

	private String host;
	private int port;
	private String dbname;
	private String user;
	private String password;

	public DBConfig() {
	}

	public DBConfig(PropertyReader properties) {
		this.host = properties.getProperty(PROP_MONGO_HOST);
		this.port = Integer.parseInt(properties.getProperty(PROP_MONGO_PORT));
		this.dbname = properties.getProperty(PROP_MONGO_DBNAME);
		this.user = properties.getProperty(PROP_MONGO_USER);
		this.password = properties.getProperty(PROP_MONGO_PASS);
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getDbname() {
		return dbname;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	public MongoClient configClient() {
		ServerAddress server = new ServerAddress(host, port);
		MongoCredential credential = MongoCredential.createCredential(user,
				dbname, password.toCharArray());
		MongoClient client = new MongoClient(server, Arrays.asList(credential));

		return client;
	}

	public MongoDatabase configDB() {
		MongoDatabase db = this.configClient().getDatabase(dbname);

		return db;
	}

}
