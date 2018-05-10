package org.repositoryminer.technicaldebt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.repositoryminer.RepositoryMinerException;
import org.repositoryminer.domain.Commit;
import org.repositoryminer.plugin.MiningPlugin;
import org.repositoryminer.scm.ISCM;
import org.repositoryminer.scm.SCMFactory;
import org.repositoryminer.technicaldebt.model.TDIndicator;
import org.repositoryminer.technicaldebt.model.TDItem;
import org.repositoryminer.technicaldebt.model.TDType;
import org.repositoryminer.technicaldebt.persistence.TechnicalDebtDAO;
import org.repositoryminer.technicaldebt.persistence.TechnicalDebtReportDAO;

import com.mongodb.client.model.Projections;

public class RepositoryMinerTechnicalDebt extends MiningPlugin<RMTDConfig>{

	public RepositoryMinerTechnicalDebt(String repositoryKey) {
		super(repositoryKey);
	}

	@Override
	public void mine(RMTDConfig config) {
		if (config == null || !config.isValid()) {
			throw new RepositoryMinerException("Invalid configuration, select at least one indicator and a reference.");
		}
		
		ISCM scm = SCMFactory.getSCM(repository.getScm());
		scm.open(repository.getPath());
		Commit commit = scm.resolve(config.getReference());
		scm.close();
		
        checkDuplicatedAnalysis(commit.getHash());
        
        ObjectId reportId = persistAnalysisReport(config.getReference(), commit, config.getIndicators());
        
        Collection<TDItem> items = new TDFinder().find(commit.getHash(), config.getIndicators());
        List<Document> documents = new ArrayList<>(items.size());

        for (TDItem item : items) {
            if (item.isDebt()) {
                Document doc = new Document("analysis_report", reportId);
                doc.append("reference", config.getReference()).
                    append("commit", commit.getHash()).
                    append("commit_date", commit.getCommitterDate()).
                    append("repository", repository.getId()).
                    append("checked", false).
                    append("intentional", 0);
                
                doc.putAll(item.toDocument());
                documents.add(doc);
            }
        }

        new TechnicalDebtDAO().insertMany(documents);
	}

	private ObjectId persistAnalysisReport(String reference, Commit commit, Set<TDIndicator> indicators) {
        TechnicalDebtReportDAO configDao = new TechnicalDebtReportDAO();
        
        List<String> indicatorsList = new ArrayList<String>();
        for (TDIndicator indicator : indicators) {
            indicatorsList.add(indicator.name());
        }
        
        Set<String> typesList = new HashSet<String>();
        for (TDIndicator indicator : indicators) {
            for (TDType type : indicator.getTypes()) {
                typesList.add(type.name());
            }
        }
        
        Document doc = new Document();
        doc.append("reference", reference)
            .append("commit", commit.getHash())
            .append("commit_date", commit.getCommitterDate())
            .append("analysis_date", new Date(System.currentTimeMillis()))
            .append("repository", repository.getId())
            .append("indicators", indicatorsList)
            .append("types", typesList);
        
        configDao.insert(doc);
        return doc.getObjectId("_id");
    }
	
	private void checkDuplicatedAnalysis(String hash) {
        TechnicalDebtReportDAO configDao = new TechnicalDebtReportDAO();
        Document doc = configDao.findByCommitHash(hash, Projections.include("_id"));
        if (doc != null) {
            configDao.deleteById(doc.getObjectId("_id"));
            new TechnicalDebtDAO().deleteByReport(doc.getObjectId("_id"));
        }
    }

}
