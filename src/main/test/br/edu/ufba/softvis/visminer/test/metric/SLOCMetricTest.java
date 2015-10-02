package br.edu.ufba.softvis.visminer.test.metric;

import static org.junit.Assert.assertEquals;

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

public class SLOCMetricTest {

	private static VisminerTest visminerTest;
	private static VisMiner visminer;
	private static Repository repository;
	private static RepositoryRetriever repoRetriever;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		visminerTest = VisminerTest.getInstance();
		visminer = visminerTest.getVisminer();
		repository = visminerTest.getRepository(); 
		repoRetriever = visminerTest.getRepositoryRetriever();
	}
	
	@Test
	public void testLocOfFiles() {
		
		Project project = repository.getProject();
		SoftwareUnit javaProject = project.getsoftwareUnit();
		//printSoftwareUnits(javaProject); // printa entidades de software do snapshot do ultimo commit
		
		for(SoftwareUnit s : javaProject.getChildren()){ 
			System.out.println("Type: "+s.getType()+" - " + visminerTest.removeRepositoryPathNameFromFileName(s.getName()) + " | SLOC " + s.getMetricValues().get(MetricUid.SLOC));
			String fileRelativeName = visminerTest.removeRepositoryPathNameFromFileName(s.getName());
			int valueMetric = 0;
			
			try {
				valueMetric = Integer.parseInt(s.getMetricValues().get(MetricUid.SLOC));
			} catch (NumberFormatException e) {
				//for the case where the metric is null FIX it later
			}
			
			
			if (fileRelativeName.equals("README.md"))
				assertEquals(3, valueMetric);
				
			if (fileRelativeName.equals("doc/overview-tree.html"))
				assertEquals(274, valueMetric);
			
			if (fileRelativeName.equals("doc/overview-frame.html"))
				assertEquals(34, valueMetric);

			if (fileRelativeName.equals("dependency-reduced-pom.xml"))
				assertEquals(80, valueMetric);			

			if (fileRelativeName.equals("src/main/java/br/edu/ufba/softvis/visminer/metric/SLOCMetric.java"))
				assertEquals(54, valueMetric);
			
		}
		
		
	}
	
	
	
	
	private static void printSoftwareUnits(SoftwareUnit javaProject){

		for(SoftwareUnit s : javaProject.getChildren()){ 
			// Pega pacotes, classes/enums e arquivos
			// Dos arquivos em geral só pega aqueles que podemos calular métricas, como um .txt
			
			if(s.getType() == SoftwareUnitType.CLASS_OR_INTERFACE){ // pego só as classes do pacote defualt
				printClassesInterfaces(s);
			} else if(s.getType() == SoftwareUnitType.PACKAGE){
				/* Alguns acostumados com a apresentação do eclipse podem estranhar os pacotes aparecerem como uma lista plana,
				mas isso é como as coisas acontecem no java.
			 	No java não existe o conceito de subpacotes, a apresentação do eclipse é só uma mera questão grafica. */
				System.out.println("Pacote " + visminerTest.removeRepositoryPathNameFromFileName(s.getName()) + " | NOCAI " + s.getMetricValues().get(MetricUid.NOCAI));
				printSoftwareUnits(s);
			} else if(s.getType() == SoftwareUnitType.ENUM || s.getType() == SoftwareUnitType.FILE){
				System.out.println("Nome " + visminerTest.removeRepositoryPathNameFromFileName(s.getName()) + " | SLOC " + s.getMetricValues().get(MetricUid.SLOC));
			}
			
		}

	}
	
	private static void printClassesInterfaces(SoftwareUnit s){

		System.out.println("Classe " + visminerTest.removeRepositoryPathNameFromFileName(s.getName()) + " | WMC " + s.getMetricValues().get(MetricUid.WMC) +
				" " + " | SLOC " + s.getMetricValues().get(MetricUid.SLOC));

		for(SoftwareUnit s2 : s.getChildren()){ // metodos e propriedades
			if(s2.getType() == SoftwareUnitType.METHOD){
				System.out.println("Método " + visminerTest.removeRepositoryPathNameFromFileName(s2.getName()) + " | CC " + s2.getMetricValues().get(MetricUid.CC));
			}
		}

	}

}
