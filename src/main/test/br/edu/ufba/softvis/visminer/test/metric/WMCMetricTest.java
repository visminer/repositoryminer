package br.edu.ufba.softvis.visminer.test.metric;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import br.edu.ufba.softvis.visminer.constant.MetricUid;
import br.edu.ufba.softvis.visminer.constant.SoftwareUnitType;
import br.edu.ufba.softvis.visminer.main.VisMiner;
import br.edu.ufba.softvis.visminer.model.business.Project;
import br.edu.ufba.softvis.visminer.model.business.Repository;
import br.edu.ufba.softvis.visminer.model.business.SoftwareUnit;
import br.edu.ufba.softvis.visminer.retriever.RepositoryRetriever;
import br.edu.ufba.softvis.visminer.test.VisminerTest;

public class WMCMetricTest {

	private static VisminerTest visminerTest;
	private static VisMiner visminer;
	private static Repository repository;
	private static RepositoryRetriever repoRetriever;
	//private static String repositoryPath = "C:\\Users\\Rafael\\Documents\\GitHub\\Visminer-Test";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		visminerTest = VisminerTest.getInstance();
		visminer = visminerTest.getVisminer();
		repository = visminerTest.getRepository(); 
		repoRetriever = visminerTest.getRepositoryRetriever();
	}
	
	@Test
	public void WMCCalculateByPackagetest() {
		
		Project project = repository.getProject();
		SoftwareUnit javaProject = project.getsoftwareUnit();
		int result = 0;
		repository.getProject().lastCommit();
		for(SoftwareUnit s : javaProject.getChildren()){

			if(s.getType() == SoftwareUnitType.PACKAGE ){
			
				
				if (s.getName().equals("br.edu.ufba.softvis.visminer.ast.generator")){
					
					if(s.getType() == SoftwareUnitType.CLASS_OR_INTERFACE){
						
						if(s.getName().equals("ASTGeneratorFactory.java")){
							result = Integer.parseInt(s.getMetricValues().get(MetricUid.WMC)) + result;
						}
						
						if(s.getName().equals("IASTGenerator.java")){
							result = Integer.parseInt(s.getMetricValues().get(MetricUid.WMC)) + result;
						}
				}
					
					assertEquals(5,result);
			}
			
			
		}
		
	}
		
		
		@Test
		public void WMCCalculatePackagetest() {

		Project project = repository.getProject();
		SoftwareUnit javaProject = project.getsoftwareUnit();
		repository.getProject().lastCommit();

		for(SoftwareUnit s : javaProject.getChildren()){
				if(s.getType() == SoftwareUnitType.PACKAGE ){
						if (s.getName().equals("br.edu.ufba.softvis.visminer.analyzer")){

								assertEquals(45,Integer.parseInt(s.getMetricValues().get(MetricUid.WMC)));
		}

						if (s.getName().equals("br.edu.ufba.softvis.visminer.analyzer.local")){

								assertEquals(56,Integer.parseInt(s.getMetricValues().get(MetricUid.WMC)));
		}
						if (s.getName().equals("br.edu.ufba.softvis.visminer.analyzer.remote")){

								assertEquals(0,Integer.parseInt(s.getMetricValues().get(MetricUid.WMC)));
		}

						if (s.getName().equals("br.edu.ufba.softvis.visminer.annotations")){

								assertEquals(0,Integer.parseInt(s.getMetricValues().get(MetricUid.WMC)));
		}
		}


		}

		}
	
	
	@Test
	public void WMCCalculateByClassInterface(){
		
		
		Project project = repository.getProject();
		SoftwareUnit javaProject = project.getsoftwareUnit();
		repository.getProject().lastCommit();
		
		for(SoftwareUnit s : javaProject.getChildren()){
			
			if(s.getType() == SoftwareUnitType.CLASS_OR_INTERFACE){
				
				if(s.getName().equals("CommitAndCommiterAnalyser.java")){
					assertEquals(4,s.getMetricValues().get(MetricUid.WMC));
				}
				
				if(s.getName().equals("FileAnalyzer.java")){
					assertEquals(2,s.getMetricValues().get(MetricUid.WMC));
				}
				
				
		}
		
	}
}
}
	


