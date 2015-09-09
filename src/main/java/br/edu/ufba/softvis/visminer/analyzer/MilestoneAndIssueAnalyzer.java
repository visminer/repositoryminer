package br.edu.ufba.softvis.visminer.analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;

import br.edu.ufba.softvis.visminer.analyzer.remote.IRepositoryService;
import br.edu.ufba.softvis.visminer.analyzer.remote.RepositoryServiceFactory;
import br.edu.ufba.softvis.visminer.constant.IssueCommand;
import br.edu.ufba.softvis.visminer.model.database.CommitDB;
import br.edu.ufba.softvis.visminer.model.database.CommitReferenceIssueDB;
import br.edu.ufba.softvis.visminer.model.database.CommitReferenceIssuePK;
import br.edu.ufba.softvis.visminer.model.database.IssueDB;
import br.edu.ufba.softvis.visminer.model.database.LabelDB;
import br.edu.ufba.softvis.visminer.model.database.MilestoneDB;
import br.edu.ufba.softvis.visminer.model.database.RepositoryDB;
import br.edu.ufba.softvis.visminer.persistence.Database;
import br.edu.ufba.softvis.visminer.persistence.dao.CommitDAO;
import br.edu.ufba.softvis.visminer.persistence.dao.CommitReferenceIssueDAO;
import br.edu.ufba.softvis.visminer.persistence.dao.IssueDAO;
import br.edu.ufba.softvis.visminer.persistence.dao.LabelDAO;
import br.edu.ufba.softvis.visminer.persistence.dao.MilestoneDAO;
import br.edu.ufba.softvis.visminer.persistence.dao.RepositoryDAO;
import br.edu.ufba.softvis.visminer.persistence.impl.CommitDAOImpl;
import br.edu.ufba.softvis.visminer.persistence.impl.CommitReferenceIssueDAOImpl;
import br.edu.ufba.softvis.visminer.persistence.impl.IssueDAOImpl;
import br.edu.ufba.softvis.visminer.persistence.impl.LabelDAOImpl;
import br.edu.ufba.softvis.visminer.persistence.impl.MilestoneDAOImpl;
import br.edu.ufba.softvis.visminer.persistence.impl.RepositoryDAOImpl;

/**
 * Defines how to save or to persist informations about issues and milestones in database.
 */
public class MilestoneAndIssueAnalyzer{

	private MilestoneDAO mileDAO;
	private IssueDAO issueDAO;
	private RepositoryDAO repoDAO;
	private LabelDAO labelDAO;
	private CommitDAO commitDAO;
	private CommitReferenceIssueDAO commitRefIssueDAO;
	private RepositoryDB repoDB;
	private Map<Integer, Integer> milesMap;
	private Map<Integer, Integer> issuesMap;
	
	public MilestoneAndIssueAnalyzer(){

		EntityManager em = Database.getInstance().getEntityManager();

		repoDAO = new RepositoryDAOImpl();
		repoDAO.setEntityManager(em);

		mileDAO = new MilestoneDAOImpl();
		mileDAO.setEntityManager(em);

		issueDAO = new IssueDAOImpl();
		issueDAO.setEntityManager(em);
		
		labelDAO = new LabelDAOImpl();
		labelDAO.setEntityManager(em);

		commitDAO = new CommitDAOImpl();
		commitDAO.setEntityManager(em);
		
		commitRefIssueDAO = new CommitReferenceIssueDAOImpl();
		commitRefIssueDAO.setEntityManager(em);
		
	}

	public void analyze(String repositoryPath, String user, String password){
		IRepositoryService service = getService(repositoryPath);
		service.connect(repoDB.getOwner(), repoDB.getName(), user, password);
		processAll(service);
	}
	
	public void analyze(String repositoryPath, String token){
		IRepositoryService service = getService(repositoryPath);
		service.connect(repoDB.getOwner(), repoDB.getName(), token);
		processAll(service);
	}
	
	public void analyze(String repositoryPath){
		IRepositoryService service = getService(repositoryPath);
		service.connect(repoDB.getOwner(), repoDB.getName());
		processAll(service);
	}
	
	private IRepositoryService getService(String repositoryPath){
		
		repoDB = repoDAO.findByPath(repositoryPath);
		IRepositoryService service = RepositoryServiceFactory.getService(repoDB.getServiceType());
		return service;
		
	}
	
	// process issues and milestones
	private void processAll(IRepositoryService service){
		
		milesMap = mileDAO.minimalFindByRepository(repoDB.getId());
		List<MilestoneDB> milestones = service.getAllMilestones();
		List<Integer> milesToDel = processMilestones(milestones);
		
		issuesMap = issueDAO.minimalFindByRepository(repoDB.getId());
		List<IssueDB> issues = service.getAllIssues();
		processIssues(issues);

		analyzeCommitMessage();
		
		if(milesToDel != null)
			mileDAO.batchDelete2(milesToDel);

		repoDAO.getEntityManager().close();
		
	}
	
	private void processIssues(List<IssueDB> issues){

		if(issues == null)
			return;
		
		for(IssueDB i : issues){
			i.setRepository(repoDB);
			if(i.getMilestone() != null){
				int id = milesMap.get(i.getMilestone().getNumber());
				i.getMilestone().setId(id);
			}
		}
		
		if(issuesMap == null){
			
			issuesMap = new HashMap<Integer, Integer>();
			issueDAO.batchSave(issues);
			
		}else{

			for(IssueDB i : issues){
				Integer elem = issuesMap.get(i.getNumber());
				if(elem != null){
					i.setId(elem);
					processLabels(i.getLabels(), elem);
				}
			}

			issueDAO.batchMerge(issues);
		}
		
		for(IssueDB i : issues){
			issuesMap.put(i.getNumber(), i.getId());
		}
		
	}
	
	private void processLabels(List<LabelDB> labels, int issueId) {

		if(labels == null)
			return;
		
		List<LabelDB> labelsDB = labelDAO.findByIssue(issueId);
		for(int i = 0; i < labels.size();){
			
			LabelDB l = labels.get(i);
			int index = labelsDB.indexOf(l);
			
			if(index != -1){
				
				LabelDB l2 = labelsDB.get(index);
				if(l.getColor().equals(l2.getColor()))
					labels.remove(i);
				else{
					l.setId(l2.getId());
					i++;
				}
				
				labelsDB.remove(index);
			} else{
				i++;
			}
			
		}
		
		if(labelsDB.size() > 0)
			labelDAO.batchDelete(labelsDB);
		
	}

	// returns the milestone that should be deleted.
	private List<Integer> processMilestones(List<MilestoneDB> milestones){
		
		if(milesMap == null && milestones == null){ // No milestones at all, does nothing.
			return null;
		}else if(milesMap != null && milestones == null){ // Milestones were deleted in service
			milesMap = null;
			return new ArrayList<Integer>(milesMap.values());
		}
		
		for(MilestoneDB m : milestones)
			m.setRepository(repoDB);
		
		if (milesMap == null) {
			
			mileDAO.batchSave(milestones);
			milesMap = new HashMap<Integer, Integer>();
			
		} else {
		
			for(MilestoneDB m : milestones){
				Integer elem = milesMap.get(m.getNumber());
				if(elem != null){
					m.setId(elem);
					milesMap.remove(m.getNumber()); // remove the milestones which still have a number
				}
			}
			
			mileDAO.batchMerge(milestones);
			
		}
		
		List<Integer> milesToDel = new ArrayList<Integer>(milesMap.values());
		milesMap.clear();
		
		for(MilestoneDB m : milestones){
			milesMap.put(m.getNumber(), m.getId());
		}
		return milesToDel;
	}
	
	private void analyzeCommitMessage(){
		
		Map<String, Integer> commandMap = IssueCommand.toMap();
		List<CommitDB> commits = commitDAO.findNotRefIssue(repoDB.getId());
		
		// creates regex string
		StringBuilder builder = new StringBuilder();  
		builder.append("(");
		
		Iterator<String> it = commandMap.keySet().iterator();
		it.next(); // the first is the NONE constant, it doesn't should be considered.
		while(it.hasNext()){
			builder.append(it.next()+"|");
		}
		builder.replace(builder.length()-1, builder.length(), ")");
		builder.append(" #[0-9]+|#[0-9]+");
		Pattern pattern = Pattern.compile(builder.toString());
		
		List<CommitReferenceIssueDB> issuesRef = new ArrayList<CommitReferenceIssueDB>();
		for(CommitDB commit : commits){
			
			Matcher matcher = pattern.matcher(commit.getMessage());
			while(matcher.find()){
				
				String frag = matcher.group();
				CommitReferenceIssueDB ref = null;
				
				if(frag.startsWith("#")){
					ref = getCommitReferenceIssueDB(frag, null, commit.getId());
				} else {
					String[] parts = frag.split(" "); // [0] is the command, [1] is the issue.
					ref = getCommitReferenceIssueDB(parts[1], parts[0], commit.getId());
				}

				int index = issuesRef.indexOf(ref);
				if(index >= 0){
					CommitReferenceIssueDB aux = issuesRef.get(index);
					if(aux.getCommand() == IssueCommand.NONE)
						aux.setCommand(ref.getCommand());
				}else if(ref != null){
					issuesRef.add(ref);
				}
					
			}
				
		}
		
		commitRefIssueDAO.batchSave(issuesRef);
		
	}

	private CommitReferenceIssueDB getCommitReferenceIssueDB(String issue, String command,
			int commitId){
		
		Integer number = Integer.parseInt(issue.substring(1));
		Integer elem = issuesMap.get(number);
		
		if(elem == null) return null;
		
		CommitReferenceIssuePK pk = new CommitReferenceIssuePK(commitId, elem);
		CommitReferenceIssueDB issueRef = new CommitReferenceIssueDB(pk, IssueCommand.parse(command));
		return issueRef;
		
	}
	
}