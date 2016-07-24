package org.repositoryminer.mining;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.repositoryminer.exceptions.ErrorMessage;
import org.repositoryminer.exceptions.VisMinerAPIException;
import org.repositoryminer.persistence.handler.IssueDocumentHandler;
import org.repositoryminer.persistence.handler.MilestoneDocumentHandler;
import org.repositoryminer.persistence.handler.RepositoryDocumentHandler;
import org.repositoryminer.persistence.model.ContributorDB;
import org.repositoryminer.persistence.model.IssueDB;
import org.repositoryminer.persistence.model.MilestoneDB;
import org.repositoryminer.scm.hostingservice.HostingService;
import org.repositoryminer.scm.hostingservice.HostingServiceFactory;
import org.repositoryminer.scm.hostingservice.HostingServiceType;
import org.repositoryminer.utility.StringUtils;

public class HostingServiceMiner {

	private String repositoryPath;
	private String owner;
	private String name;
	private HostingServiceType serviceType;

	private HostingService service;

	public HostingServiceMiner() {}
	
	public HostingServiceMiner(String repositoryPath, String owner, String name, HostingServiceType serviceType) {
		super();
		this.repositoryPath = repositoryPath;
		this.owner = owner;
		this.name = name;
		this.serviceType = serviceType;
	}

	public void mine(String user, String password) throws IOException{
		service = HostingServiceFactory.getHostingService(serviceType);
		service.connect(owner, name, user, password);
		process();
	}

	public void mine(String token) throws IOException{
		service = HostingServiceFactory.getHostingService(serviceType);
		service.connect(owner, name, token);
		process();
	}

	@SuppressWarnings("unchecked")
	private void process() throws IOException {
		String canonicalPath = StringUtils.treatPath(repositoryPath);
		String id = StringUtils.encodeToSHA1(canonicalPath);

		RepositoryDocumentHandler repoDocHandler = new RepositoryDocumentHandler();
		if (!repoDocHandler.checkIfRepositoryExists(id)) {
			throw new VisMinerAPIException(ErrorMessage.REPOSITORY_NOT_FOUND.toString());
		}

		IssueDocumentHandler issueDocHandler = new IssueDocumentHandler();
		MilestoneDocumentHandler mileDocHandler = new MilestoneDocumentHandler();

		issueDocHandler.deleteByRepository(id);
		mileDocHandler.deleteByRepository(id);

		Document repoDoc = repoDocHandler.findOnlyContributors(id);
		List<ContributorDB> contributorsDb = service.getAllContributors();
		
		for (Document contributorDoc : (List<Document>) repoDoc.get("contributors")) {
			String name = contributorDoc.getString("name");
			for (ContributorDB contributorDb : contributorsDb) {
				if (name.equals(contributorDb.getName())) {
					contributorDoc.put("login", contributorDb.getLogin());
					contributorDoc.put("avatar_url", contributorDb.getAvatarUrl());
					contributorDoc.put("collaborator", contributorDb.isCollaborator());
					break;
				}
			}
		}
		
		repoDocHandler.updateOnlyContributors(repoDoc);
		List<IssueDB> issues = service.getAllIssues();
		List<MilestoneDB> milestones = service.getAllMilestones();

		// connect issues to milestones
		if (milestones.size() > 0) {
			for (MilestoneDB m : milestones) {
				m.setIssues(new ArrayList<Integer>());
				for (IssueDB i : issues) {
					if (i.getMilestone() == m.getNumber()) {
						m.getIssues().add(i.getNumber());
					}
				}
			}
		}

		List<Document> issuesDocs = new ArrayList<Document>(issues.size());
		List<Document> milesDocs = new ArrayList<Document>(milestones.size());

		if (issues.size() > 0) {
			for (IssueDB issue : issues) {
				issue.setRepository(id);
				issuesDocs.add(issue.toDocument());
			}
			issueDocHandler.insertMany(issuesDocs);
		}

		if (milestones.size() > 0) {
			for (MilestoneDB mile : milestones) {
				mile.setRepository(id);
				milesDocs.add(mile.toDocument());
			}
			mileDocHandler.insertMany(milesDocs);
		}
	}

	/**
	 * @return the repositoryPath
	 */
	public String getRepositoryPath() {
		return repositoryPath;
	}

	/**
	 * @param repositoryPath the repositoryPath to set
	 */
	public void setRepositoryPath(String repositoryPath) {
		this.repositoryPath = repositoryPath;
	}

	/**
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the serviceType
	 */
	public HostingServiceType getServiceType() {
		return serviceType;
	}

	/**
	 * @param serviceType the serviceType to set
	 */
	public void setServiceType(HostingServiceType serviceType) {
		this.serviceType = serviceType;
	}

}