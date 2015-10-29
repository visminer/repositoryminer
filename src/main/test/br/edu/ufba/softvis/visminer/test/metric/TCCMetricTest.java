package br.edu.ufba.softvis.visminer.test.metric;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import br.edu.ufba.softvis.visminer.constant.MetricUid;
import br.edu.ufba.softvis.visminer.model.business.Project;
import br.edu.ufba.softvis.visminer.model.business.Repository;
import br.edu.ufba.softvis.visminer.model.business.SoftwareUnit;
import br.edu.ufba.softvis.visminer.test.VisminerTest;

public class TCCMetricTest {

	private static VisminerTest visminerTest;
	private static Repository repository;
	private static Map<String,Double> lista;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		visminerTest = VisminerTest.getInstance();
		visminerTest.getVisminer();
		repository = visminerTest.getRepository(); 
		String path= " ";
		FileReader arq = new FileReader(".\\TCC.CSV"); 
		BufferedReader lerArq = new BufferedReader(arq);
		String array[] = new String[2];
		lista = new HashMap<String, Double>();
		lerArq.readLine(); //pular cabeçalho
		while(lerArq.ready()){
			
           array = lerArq.readLine().split(";");
           System.out.println(array[0]);
           lista.put(array[0]+".java", Double.valueOf(array[1]));
        }
		lerArq.close();
		arq.close();
	}
	@Test
	public void test() {
				
		Project project = repository.getProject();
		SoftwareUnit javaProject = project.getsoftwareUnit();
		double valueMetric = 0;
		int contpacote=0;
		int contclasse = 0;
		String nome;
	
		for (SoftwareUnit pacote : javaProject.getChildren()){
		++contpacote;
		System.out.println(pacote.getName());
			for(SoftwareUnit classe : pacote.getChildren()){
				
				++contclasse;
				nome = classe.getName();
				
				String[] fullnomeclasse = classe.getName().split("/");
				String nomeclasse = fullnomeclasse[fullnomeclasse.length -1];
				
				valueMetric = Double.valueOf(classe.getMetricValues().get(MetricUid.TCC));
				assertEquals(lista.get(nomeclasse).doubleValue(), valueMetric);
		
			System.out.println(classe.getName() + " " +valueMetric);
	/*		
			if(classe.getName().contains("/CommitAndCommitterAnalyzer.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}
			
			if(classe.getName().contains("/FileAnalyzer.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/IssueAnalyzer.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/GitRepository.java")){
				System.out.println("OK");
				assertEquals(0.4, valueMetric);
							
			}if(classe.getName().contains("/IRepositorySystem.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/SupportedRepository.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/MetricCalculator.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/MilestoneAnalyzer.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/GitHubRepository.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/IRepositoryWebService.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/RepositoryAnalyzer.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/TreeAnalyzer.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/AST.java")){
				System.out.println("OK");
				assertEquals(0.2857142857142857, valueMetric);
							
			}if(classe.getName().contains("/ClassOrInterfaceDeclaration.java")){
				System.out.println("OK");
					
				assertEquals(0.2, valueMetric);
							
			}if(classe.getName().contains("/Document.java")){
				System.out.println("OK");
				assertEquals(0.12087912087912088, valueMetric);
							
			}if(classe.getName().contains("/EnumConstantDeclaration.java")){
				System.out.println("OK");
				assertEquals(0.3333333333333333, valueMetric);
							
			}if(classe.getName().contains("/EnumDeclaration.java")){
				System.out.println("OK");
				assertEquals(1.0, valueMetric);
							
			}if(classe.getName().contains("/FieldDeclaration.java")){
				System.out.println("OK");
				assertEquals(0.14285714285714285, valueMetric);
							
			}if(classe.getName().contains("/ASTGeneratorFactory.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/CPPASTGenerator.java")){
				System.out.println("OK");
				assertEquals(0.4, valueMetric);
							
			}if(classe.getName().contains("/CPPASTLogger.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/CPPASTVisitor.java")){
				System.out.println("OK");
				assertEquals(1.0, valueMetric);
							
			}if(classe.getName().contains("/IASTGenerator.java")){
				System.out.println("OK");
				assertEquals(0.2, valueMetric);
							
			}if(classe.getName().contains("/JavaASTGenerator.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/MethodVisitor.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/NoneASTGenerator.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/ImportDeclaration.java")){
				System.out.println("OK");
				assertEquals(0.2, valueMetric);
							
			}if(classe.getName().contains("/MethodDeclaration.java")){
				System.out.println("OK");
				assertEquals(0.058823529411764705, valueMetric);
							
			}if(classe.getName().contains("/PackageDeclaration.java")){
				System.out.println("OK");
				assertEquals(0.3333333333333333, valueMetric);
							
			}if(classe.getName().contains("/ParameterDeclaration.java")){
				System.out.println("OK");
				assertEquals(0.3333333333333333, valueMetric);
							
			}if(classe.getName().contains("/Project.java")){
				System.out.println("OK");
				assertEquals(0.3333333333333333, valueMetric);
							
			}if(classe.getName().contains("/Statement.java")){
				System.out.println("OK");
				assertEquals(0.3333333333333333, valueMetric);
							
			}if(classe.getName().contains("/TypeDeclaration.java")){
				System.out.println("OK");
				assertEquals(0.2, valueMetric);
							
			}if(classe.getName().contains("/DBConfig.java")){
				System.out.println("OK");
				assertEquals(0.23076923076923078, valueMetric);
							
			}if(classe.getName().contains("/MetricConfig.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/DatabaseConfig.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/ListAllCommitsFromRepository.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/NavigateInProject.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/PersistRepository.java")){
				System.out.println("OK");
				assertEquals(0.14210526315789473, valueMetric);
							
			}if(classe.getName().contains("/VisMiner.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/CCMetric.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/IMetric.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/MethodBasedMetricTemplate.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/NOCAIMetric.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/SLOCMetric.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/WMCMetric.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/Commit.java")){
				System.out.println("OK");
				assertEquals(0.12087912087912088, valueMetric);
							
			}if(classe.getName().contains("/Committer.java")){
				System.out.println("OK");
				assertEquals(0.2, valueMetric);
							
			}if(classe.getName().contains("/File.java")){
				System.out.println("OK");
				assertEquals(0.15151515151515152, valueMetric);
							
			}if(classe.getName().contains("/FileState.java")){
				System.out.println("OK");
				assertEquals(0.2, valueMetric);
							
			}if(classe.getName().contains("/Metric.java")){
				System.out.println("OK");
				assertEquals(0.12087912087912088, valueMetric);
							
			}if(classe.getName().contains("/Project.java")){
				System.out.println("OK");
				assertEquals(0.3202614379084967, valueMetric);
							
			}if(classe.getName().contains("/Repository.java")){
				System.out.println("OK");
				assertEquals(0.052307692307692305, valueMetric);
							
			}if(classe.getName().contains("/SoftwareUnit.java")){
				System.out.println("OK");
				assertEquals(0.08496732026143791, valueMetric);
							
			}if(classe.getName().contains("/Tree.java")){
				System.out.println("OK");
				assertEquals(0.15384615384615385, valueMetric);
							
			}if(classe.getName().contains("/CommitDB.java")){
				System.out.println("OK");
				assertEquals(0.06060606060606061, valueMetric);
							
			}if(classe.getName().contains("/CommitterDB.java")){
				System.out.println("OK");
				assertEquals(0.10989010989010989, valueMetric);
							
			}if(classe.getName().contains("/FileDB.java")){
				System.out.println("OK");
				assertEquals(0.10989010989010989, valueMetric);
							
			}if(classe.getName().contains("/FileXCommitDB.java")){
				System.out.println("OK");
				assertEquals(0.12087912087912088, valueMetric);
							
			}if(classe.getName().contains("/FileXCommitPK.java")){
				System.out.println("OK");
				assertEquals(0.7333333333333333, valueMetric);
							
			}if(classe.getName().contains("/IssueDB.java")){
				System.out.println("OK");
				assertEquals(0.04, valueMetric);
							
			}if(classe.getName().contains("/MetricDB.java")){
				System.out.println("OK");
				assertEquals(0.07692307692307693, valueMetric);
							
			}if(classe.getName().contains("/MetricValueDB.java")){
				System.out.println("OK");
				assertEquals(0.2, valueMetric);
							
			}if(classe.getName().contains("/MetricValuePK.java")){
				System.out.println("OK");
				assertEquals(0.5714285714285714, valueMetric);
							
			}if(classe.getName().contains("/MilestoneDB.java")){
				System.out.println("OK");
				assertEquals(0.043478260869565216, valueMetric);
							
			}if(classe.getName().contains("/RepositoryDB.java")){
				System.out.println("OK");
				assertEquals(0.03218390804597701, valueMetric);
							
			}if(classe.getName().contains("/SoftwareUnitDB.java")){
				System.out.println("OK");
				assertEquals(0.06060606060606061, valueMetric);
							
			}if(classe.getName().contains("/SoftwareUnitXCommitDB.java")){
				System.out.println("OK");
				assertEquals(0.4666666666666667, valueMetric);
							
			}if(classe.getName().contains("/SoftwareUnitXCommitPK.java")){
				System.out.println("OK");
				assertEquals(0.7333333333333333, valueMetric);
							
			}if(classe.getName().contains("/TreeDB.java")){
				System.out.println("OK");
				assertEquals(0.058333333333333334, valueMetric);
							
			}if(classe.getName().contains("/CommitDAO.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/CommitterDAO.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/DAO.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/FileDAO.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/FileXCommitDAO.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/IssueDAO.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/MetricDAO.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/MetricValueDAO.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/MilestoneDAO.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/FileXCommitDAO.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/RepositoryDAO.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/SoftwareUnitDAO.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/SoftwareUnitXCommitDAO.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/TreeDAO.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/CommitDAOImpl.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/CommitterDAOImpl.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/DAOImpl.java")){
				System.out.println("OK");
				assertEquals(1.0, valueMetric);
							
			}if(classe.getName().contains("/FileDAOImpl.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/FileXCommitDAOImpl.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/IssueDAOImpl.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/MetricDAOImpl.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/MetricValueDAOImpl.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/MilestoneDAOImpl.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/RepositoryDAOImpl.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/SoftwareUnitDAOImpl.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/SoftwareUnitXCommitImpl.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/TreeDAOImpl.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/Database.java")){
				System.out.println("OK");
				assertEquals(0.6666666666666666, valueMetric);
							
			}if(classe.getName().contains("/MetricPersistance.java")){
				System.out.println("OK");
				assertEquals(0.5333333333333333, valueMetric);
							
			}if(classe.getName().contains("/SaveAST.java")){
				System.out.println("OK");
				assertEquals(1.0, valueMetric);
							
			}if(classe.getName().contains("/CommitRetriever.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/FileRetriever.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/MetricRetriever.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/RepositoryRetriever.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/Retriever.java")){
				System.out.println("OK");
				assertEquals(1.0, valueMetric);
							
			}if(classe.getName().contains("/SoftwareUnitRetriever.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/PropertyReader.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/StringUtils.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}if(classe.getName().contains("/XMLUtil.java")){
				System.out.println("OK");
				assertEquals(0.0, valueMetric);
							
			}
				*/
			}
			
		}
		System.out.println("Pacote"+contpacote);
		System.out.println("Classe"+contclasse);
		
		
		
	}

}
