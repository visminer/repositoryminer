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

public abstract class SnapshotAnalysisPlugin<T> {

	protected String tmpRepository;
	protected ObjectId repositoryId;
	protected ISCM scm;

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

	public abstract void run(String snapshot, T config);

	public void finish() throws IOException {
		scm.close();
		RMFileUtils.deleteFolder(tmpRepository);
	}

}