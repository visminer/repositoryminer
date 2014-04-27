package org.visminer.git.remote;

import java.util.HashMap;

import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHMilestone;
import org.kohsuke.github.GHRepository;

import org.visminer.main.VisMiner;
import org.visminer.model.Milestone;
import org.visminer.model.Repository;
import org.visminer.persistence.MilestoneDAO;

public abstract class MilestoneUpdate {
	
	
	public static void updateMilestone(Object ghr, VisMiner visminer){
		
		boolean hasMilestones = false;
		
			if( ! ((GHRepository) ghr).listMilestones(GHIssueState.OPEN).asList().isEmpty() ){
				
				update(ghr, visminer, GHIssueState.OPEN);
				hasMilestones = true;

			}
			
			if( ! ((GHRepository) ghr).listMilestones(GHIssueState.CLOSED).asList().isEmpty() ){
				
				update(ghr, visminer, GHIssueState.CLOSED);
				hasMilestones = true;
				
			}
			
			//if doesn't exist at least one milestone in the specified remote repository
			//delete all milestone the local database
			if(!hasMilestones){

				System.out.println("Probable repository doesn't have milestones");
				org.visminer.persistence.MilestoneDAO milestoneDAO = new MilestoneDAO();
				milestoneDAO.deleteAll(visminer.getRepository());
				
			}
			
	}
		
		
	private static void update(Object ghr, VisMiner visminer, GHIssueState status){
		
		org.visminer.model.Milestone milestone = new Milestone();
		org.visminer.model.Repository repository = new Repository();
		org.visminer.persistence.MilestoneDAO milestoneDAO = new MilestoneDAO();
		
		repository.setIdGit(visminer.getRepository().getIdGit());
		repository.setName(visminer.getRepository().getName());
		repository.setPath(visminer.getRepository().getPath());
		
	
		//save milestone's number existing in the specified remote repository
		HashMap<Integer, Integer> milestonesNumbers = new HashMap<Integer, Integer>();
				
		for(GHMilestone ghm: ((GHRepository) ghr).listMilestones(status).asList()){
					
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
			milestonesNumbers.put(milestone.getNumber(), milestone.getNumber());
				
		}
				
		//verifying if some database's milestone doesn't exist more in the remote repository and delete them
		//of local repository
		String st;
		
		if(GHIssueState.OPEN == status)
			st = "open";
		else
			st = "closed";
				
		for(Milestone m: milestoneDAO.getByStatus(repository, st)){

			if( !milestonesNumbers.containsKey(m.getNumber()))
				milestoneDAO.deleteOne(m.getNumber(), repository);
			
		}
	
	}
		
}
	

