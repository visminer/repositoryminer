package org.visminer.git.remote;

import java.util.HashMap;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHMilestone;
import org.kohsuke.github.GHRepository;

import org.visminer.model.Milestone;
import org.visminer.model.Repository;
import org.visminer.persistence.MilestoneDAO;

/**
 *To update milestones in the local database
 */
public abstract class MilestoneUpdate {
	
	/*
	 * This HashMap saves milestone's number existing in the specified remote repository.
	 * It is used to make a comparison between milestones that exist in local database
	 * but didn't exist more in the remote repository.
	 */
	private static HashMap<Integer, Integer> milestonesNumbers = new HashMap<Integer, Integer>();

	private static org.visminer.persistence.MilestoneDAO milestoneDAO = new MilestoneDAO();

	
	public static void updateMilestone(Object gr, Repository repository){
		
		boolean hasMilestones = false;
		
			if( ! ((GHRepository) gr).listMilestones(GHIssueState.OPEN).asList().isEmpty() ){
				
				update(gr, repository, GHIssueState.OPEN);
				hasMilestones = true;

			}
			
			if( ! ((GHRepository) gr).listMilestones(GHIssueState.CLOSED).asList().isEmpty() ){
				
				update(gr, repository, GHIssueState.CLOSED);
				hasMilestones = true;
				
			}
			
			if(!hasMilestones)
				System.out.println("Probable repository doesn't have milestones");

			/*
			 * Verifying if some database's register milestone doesn't exist more in the remote repository 
			 * and delete them of local repository.
			 */
			for(Milestone m: milestoneDAO.getAll(repository)){

				if( !milestonesNumbers.containsKey(m.getNumber()))
					milestoneDAO.deleteOne(m.getNumber(), repository);
				
			}
			
	}
		
		
	private static void update(Object ghr, Repository repository, GHIssueState status){
		
		org.visminer.model.Milestone milestone = new Milestone();
				
		for(GHMilestone ghm: ((GHRepository) ghr).listMilestones(status).asList()){
					
			milestone.setClosedIssues(ghm.getClosedIssues());
			milestone.setCreate_date(ghm.getCreatedAt());
			milestone.setCreator(ghm.getCreator().getLogin());
			milestone.setDescription(ghm.getDescription());
					
			if(ghm.getDueOn() != null)
				milestone.setDue_date(ghm.getDueOn());
					
			milestone.setNumber(ghm.getNumber());
			milestone.setOpenedIssues(ghm.getOpenIssues());
			milestone.setRepository(repository);
			milestone.setState(ghm.getState().name());
			milestone.setTitle(ghm.getTitle());
					
			milestoneDAO.save(milestone);
			milestonesNumbers.put(milestone.getNumber(), milestone.getNumber());
				
		}
				
	
	}
		
}
	

