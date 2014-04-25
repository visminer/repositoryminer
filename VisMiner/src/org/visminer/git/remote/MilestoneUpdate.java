package org.visminer.git.remote;

import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHMilestone;
import org.kohsuke.github.GHRepository;

import org.visminer.main.VisMiner;
import org.visminer.model.Milestone;
import org.visminer.model.Repository;
import org.visminer.persistence.MilestoneDAO;

public abstract class MilestoneUpdate {
	

	public static void updateMilestone(Object ghr, VisMiner visminer){
		
		org.visminer.model.Milestone milestone = new Milestone();
		org.visminer.model.Repository repository = new Repository();
		org.visminer.persistence.MilestoneDAO milestoneDAO = new MilestoneDAO();
		
		repository.setIdGit(visminer.getRepository().getIdGit());
		repository.setName(visminer.getRepository().getName());
		repository.setPath(visminer.getRepository().getPath());
		
		for(GHMilestone ghm: ((GHRepository) ghr).listMilestones(GHIssueState.OPEN).asList()){
			
			milestone.setClosedIssues(ghm.getClosedIssues());
			milestone.setCreate_date(ghm.getCreatedAt().getTime());
			milestone.setCreator(ghm.getCreator().getLogin());
			milestone.setDescription(ghm.getDescription());
			
			if(ghm.getDueOn() != null)
				milestone.setDue_date(ghm.getDueOn().getTime());
			
			milestone.setNumber(ghm.getNumber());
			milestone.setOpenedIssues(ghm.getOpenIssues());
			milestone.setRepository(repository);
			milestone.setState(ghm.getState().name());
			milestone.setTitle(ghm.getTitle());
			
			milestoneDAO.save(milestone);
			
		}
		
		
		for(GHMilestone ghm: ((GHRepository) ghr).listMilestones(GHIssueState.CLOSED).asList()){
			
			milestone.setClosedIssues(ghm.getClosedIssues());
			milestone.setCreate_date(ghm.getCreatedAt().getTime());
			milestone.setCreator(ghm.getCreator().getLogin());
			milestone.setDescription(ghm.getDescription());
			
			if(ghm.getDueOn() != null)
				milestone.setDue_date(ghm.getDueOn().getTime());
			
			milestone.setNumber(ghm.getNumber());
			milestone.setOpenedIssues(ghm.getOpenIssues());
			milestone.setRepository(repository);
			milestone.setState(ghm.getState().name());
			milestone.setTitle(ghm.getTitle());
			
			milestoneDAO.save(milestone);
	
		}	

	}
	
}
