package org.repositoryminer.excomment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.bson.Document;
import org.repositoryminer.RepositoryMinerException;
import org.repositoryminer.domain.Commit;
import org.repositoryminer.excomment.model.Comment;
import org.repositoryminer.excomment.persistence.ExCommentDAO;
import org.repositoryminer.plugin.MiningPlugin;
import org.repositoryminer.scm.ISCM;
import org.repositoryminer.scm.SCMFactory;
import org.repositoryminer.util.StringUtils;

public class RepositoryMinerExComment extends MiningPlugin<ExCommentConfig> {

	public RepositoryMinerExComment(String repositoryKey) {
		super(repositoryKey);
	}

	@Override
	public void mine(ExCommentConfig config) {
		if (config == null || !config.isValid()) {
			throw new RepositoryMinerException(
					"Invalid configuration, set the CSV files, the delimiter and a reference correctly.");
		}
		
		ISCM scm = SCMFactory.getSCM(repository.getScm());
		scm.open(repository.getPath());
		Commit commit = scm.resolve(config.getReference());
		scm.close();
        
        ExCommentDAO dao = new ExCommentDAO();
        dao.deleteByCommit(commit.getHash());
        
        ExCommentCSVReader csvReader = new ExCommentCSVReader(config);
        
        try {
            csvReader.readCSVs();
        } catch (IOException e) {
            throw new RepositoryMinerException("An IO error had occurred while reading the files.");
        }
        
        List<Document> documents = new ArrayList<Document>(csvReader.getFilesMap().size());
        for (Entry<String, List<Integer>> entry : csvReader.getFilesMap().entrySet()) {
            Document doc = new Document();
            
            doc.append("reference", config.getReference()).
                append("commit", commit.getHash()).
                append("commit_date", commit.getCommitterDate()).
                append("repository", repository.getId()).
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
