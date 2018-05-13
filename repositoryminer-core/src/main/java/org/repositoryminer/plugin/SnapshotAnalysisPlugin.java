package org.repositoryminer.plugin;

import java.io.IOException;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.repositoryminer.RepositoryMinerException;
import org.repositoryminer.domain.SCMType;
import org.repositoryminer.persistence.RepositoryDAO;
import org.repositoryminer.scm.ISCM;
import org.repositoryminer.scm.SCMFactory;
import org.repositoryminer.util.RMFileUtils;
import org.repositoryminer.util.StringUtils;

import com.mongodb.client.model.Projections;

/**
 * This extension point is interesting for plugins that want to access the
 * repository and perform some sort of analysis in one or more of its versions,
 * such as static code analysis.
 *
 * @param <T>
 *            some configuration.
 */
public abstract class SnapshotAnalysisPlugin<T> {

	protected String tmpRepository;
	protected ObjectId repositoryId;
	protected ISCM scm;

	/**
	 * This method is responsible for preparing the repository to run the plugin,
	 * and should be called only once.
	 * 
	 * @param repositoryKey
	 *            the repository key
	 * @throws IOException
	 */
	public void init(String repositoryKey) throws IOException {
		Document repoDoc = new RepositoryDAO().findByKey(repositoryKey, Projections.include("_id", "path", "scm"));
		if (repoDoc == null) {
			throw new RepositoryMinerException("Repository with the key " + repositoryKey + " does not exists");
		}

		scm = SCMFactory.getSCM(SCMType.valueOf(repoDoc.getString("scm")));
		repositoryId = repoDoc.getObjectId("_id");
		tmpRepository = RMFileUtils.copyFolderToTmp(repoDoc.getString("path"),
				StringUtils.encodeToSHA1(repositoryId.toHexString()));

		scm.open(tmpRepository);
	}

	/**
	 * This method is the plugin entry point, by calling it, the plugin will execute
	 * its analysis in a given repository version.
	 * 
	 * @param snapshot
	 *            the commit reference
	 * @param config
	 *            some configuration
	 */
	public abstract void run(String snapshot, T config);

	/**
	 * This method is responsible for releasing the resources allocated to the
	 * plugin execution, and should be called only once.
	 * 
	 * @throws IOException
	 */
	public void finish() throws IOException {
		scm.close();
		RMFileUtils.deleteFolder(tmpRepository);
	}

}
