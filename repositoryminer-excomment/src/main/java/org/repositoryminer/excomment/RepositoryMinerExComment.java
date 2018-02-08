package org.repositoryminer.excomment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.bson.Document;
import org.repositoryminer.RepositoryMinerException;
import org.repositoryminer.domain.Commit;
import org.repositoryminer.domain.Repository;
import org.repositoryminer.excomment.model.Comment;
import org.repositoryminer.excomment.persistence.ExCommentDAO;
import org.repositoryminer.persistence.RepositoryDAO;
import org.repositoryminer.plugin.SnapshotAnalysisPlugin;
import org.repositoryminer.util.StringUtils;

import com.mongodb.client.model.Projections;

public class RepositoryMinerExComment extends SnapshotAnalysisPlugin<ExCommentConfig> {

	private String repositoryPath;
	
	@Override
	public void init(String repositoryKey) throws IOException {
		super.init(repositoryKey);
		
		RepositoryDAO repoHandler = new RepositoryDAO();
		Repository repository = Repository
				.parseDocument(repoHandler.findById(repositoryId, Projections.include("path")));
		
		this.repositoryPath = repository.getPath();
	}

	@Override
	public void run(String snapshot, ExCommentConfig config) {
		if (config == null || !config.isValid()) {
			throw new RepositoryMinerException("Invalid configuration, set the CSV files and the delimiter correctly.");
		}
		
		Commit commit = scm.resolve(snapshot);
		
		ExCommentDAO dao = new ExCommentDAO();
		dao.deleteByCommit(commit.getHash());
		
		ExCommentCSVReader csvReader = new ExCommentCSVReader(repositoryPath, config);
		
		try {
			csvReader.readCSVs();
		} catch (IOException e) {
			throw new RepositoryMinerException("An IO error had occurred while reading the files.");
		}
		
		List<Document> documents = new ArrayList<Document>(csvReader.getFilesMap().size());
		for (Entry<String, List<Integer>> entry : csvReader.getFilesMap().entrySet()) {
			Document doc = new Document();
			
			doc.append("reference", snapshot).
				append("commit", commit.getHash()).
				append("commit_date", commit.getCommitterDate()).
				append("repository", repositoryId).
				append("filename", entry.getKey()).
				append("filehash", StringUtils.encodeToCRC32(entry.getKey()));

			List<Comment> commentsAux = new ArrayList<Comment>(entry.getValue().size());
			for (Integer i : entry.getValue()) {
				commentsAux.add(csvReader.getCommentsMap().get(i));
			}
			doc.append("comments", Comment.toDocumentList(commentsAux));

			documents.add(doc);
		}

		dao.insertMany(documents);
	}

}
