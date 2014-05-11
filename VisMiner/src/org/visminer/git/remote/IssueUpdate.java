package org.visminer.git.remote;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jdt.internal.compiler.ast.OR_OR_Expression;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssue.Label;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRepository;
import org.visminer.main.VisMiner;
import org.visminer.model.Milestone;
import org.visminer.model.Repository;
import org.visminer.persistence.IssueDAO;
/**
 * To update Issues in the local database
 */
public abstract class IssueUpdate {
	
	/**
	 * 
	 * Update(insert, update) the issues of the local database for stay how
	 * is in the remote repository. If this method is called first the method "updateMilestone" 
	 * {@link MilestoneUpdate} it will create a milestone's register in the local database, if this milestone's
	 * register don't exist. Or will update milestone's register, if the issue is related with them.
	 * 
	 * @param gh :the git's repository remote object. {@link ConnectionToRepository}
	 * @param visminer :{@linkplain VisMiner} object
	 * @throws IOException
	 */
	public static void updateIssue(Object gh, VisMiner visminer) throws IOException{
			
		if( ((GHRepository) gh).hasIssues() ){

			update(gh, visminer, GHIssueState.OPEN);
			update(gh, visminer, GHIssueState.CLOSED);
			
		}
				
	}
	
	/**
	 * @param gh
	 * @param visminer
	 * @param status :the issues has two status "OPEN" or "CLOSED"
	 * @throws IOException
	 */
	private static void update(Object gh, VisMiner visminer, GHIssueState status) throws IOException{
		
			IssueDAO issueDAO = new IssueDAO();
			org.visminer.model.Issue issue = new org.visminer.model.Issue();
			org.visminer.model.Repository repository = new Repository();
			
			repository.setIdGit(visminer.getRepository().getIdGit());
			repository.setName(visminer.getRepository().getName());
			repository.setPath(visminer.getRepository().getPath());
			
			if( ((GHRepository) gh).getIssues(status) != null ){
				
				for(GHIssue ghIssue: ((GHRepository) gh).getIssues(status)){
					
					issue.setRepository(repository);
					
					if(ghIssue.getAssignee() != null)
						issue.setAssignee(ghIssue.getAssignee().getLogin());
					else
						issue.setAssignee(null);
					
					issue.setCreate_date(ghIssue.getCreatedAt().getTime());
					
					if(ghIssue.getClosedAt() != null)
						issue.setClosed_date(ghIssue.getClosedAt().getTime());

					ArrayList<String> labels = new ArrayList<String>();
					if(ghIssue.getLabels() != null){
						
						for(Label label: ghIssue.getLabels())
							labels.add(label.getName());
						
						issue.setLabels(labels);
						
					}else
						issue.setLabels(null);
					
					if(ghIssue.getMilestone() != null){
						
						org.visminer.model.Milestone milestone = new Milestone();
						
						milestone.setNumber(ghIssue.getMilestone().getNumber());
						milestone.setClosedIssues(ghIssue.getMilestone().getClosedIssues());
						milestone.setCreate_date(ghIssue.getMilestone().getCreatedAt().getTime());
						milestone.setCreator(ghIssue.getMilestone().getCreator().getLogin());
						milestone.setDescription(ghIssue.getMilestone().getDescription());
						
						if(ghIssue.getMilestone().getDueOn() != null)
							milestone.setDue_date(ghIssue.getMilestone().getDueOn().getTime());
						
						milestone.setOpenedIssues(ghIssue.getMilestone().getOpenIssues());
						milestone.setRepository(repository);
						milestone.setState(ghIssue.getMilestone().getState().name());
						milestone.setTitle(ghIssue.getMilestone().getTitle());
						
						issue.setMilestone(milestone);

					}
					else
						issue.setMilestone(null);
					
					issue.setNumber(ghIssue.getNumber());
					issue.setNumberOfComments(ghIssue.getCommentsCount());
					
					issue.setStatus(ghIssue.getState().name());
					issue.setTitle(ghIssue.getTitle());
					issue.setUpdated_date(ghIssue.getUpdatedAt().getTime());
					
					issueDAO.save(issue);

				}		

			}
		
	}
	
}
