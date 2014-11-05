import org.visminer.constant.RemoteServiceType;
import org.visminer.constant.RepositoryType;
import org.visminer.constant.TreeType;
import org.visminer.main.Visminer;
import org.visminer.model.business.File;
import org.visminer.model.business.Metric;
import org.visminer.model.business.Repository;

public class Test {

	public static void main(String[] args) {
		
		Visminer visminer = new Visminer();
		
		visminer.configure("config.properties");
		visminer.configureMetrics("metrics.xml");
		
		visminer.getAnalyzer().setRepositoryName("visminer")
		                      .setRepositoryPath("/home/felipe/git/VismierTest/.git")
		                      .setRepositoryRemoteName("visminer")
		                      .setRepositoryRemoteOwner("fool")
		                      .setRepositoryRemoteType(RemoteServiceType.GITHUB)
		                      .setRepositoryType(RepositoryType.GIT);
		
		Repository repository = visminer.analyze();
		
		for(File f : repository.getFiles()){
			
			System.out.println(f.getPath());
			
			if(f.getSoftwareEntities().size() == 0) continue;
			
			System.out.println("entity: "+f.getSoftwareEntities().get(0).getName());
			
			for(Metric m : f.getSoftwareEntities().get(0).getMetrics()){
				System.out.println(m.getName()+" "+m.getValue());
			}
		}
		
	}

}