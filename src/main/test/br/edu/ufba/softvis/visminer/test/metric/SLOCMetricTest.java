package br.edu.ufba.softvis.visminer.test.metric;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import br.edu.ufba.softvis.visminer.constant.MetricUid;
import br.edu.ufba.softvis.visminer.constant.SoftwareUnitType;
import br.edu.ufba.softvis.visminer.model.business.Project;
import br.edu.ufba.softvis.visminer.model.business.Repository;
import br.edu.ufba.softvis.visminer.model.business.SoftwareUnit;
import br.edu.ufba.softvis.visminer.test.VisminerTest;

public class SLOCMetricTest {

	private static VisminerTest visminerTest;
	private static Repository repository;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		visminerTest = VisminerTest.getInstance();
		visminerTest.getVisminer();
		repository = visminerTest.getRepository(); 
		
	}
	
	@Test
	public void testLocOfFiles() {
		
		Project project = repository.getProject();
		SoftwareUnit javaProject = project.getsoftwareUnit();
		//printSoftwareUnits(javaProject); // printa entidades de software do snapshot do ultimo commit
		
		for(SoftwareUnit s : javaProject.getChildren()){ 
			System.out.println("Type: "+s.getType()+" - " + s.getName() + " | SLOC " + s.getMetricValues().get(MetricUid.SLOC));
			String fileRelativeName = s.getName();
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
			
			if(s.getType() == SoftwareUnitType.CLASS_OR_INTERFACE){
				
				printClassesInterfaces(s);
				
			} else if(s.getType() == SoftwareUnitType.PACKAGE){
				
				System.out.println("Pacote " + s.getName() + " | NOCAI " + s.getMetricValues().get(MetricUid.NOCAI));
				printSoftwareUnits(s);
				
			} else if(s.getType() == SoftwareUnitType.ENUM || s.getType() == SoftwareUnitType.FILE){
				
				System.out.println("Nome " + s.getName() + " | SLOC " + s.getMetricValues().get(MetricUid.SLOC));
				
			}
			
		}

	}
	
	private static void printClassesInterfaces(SoftwareUnit s){

		System.out.println("Classe " + s.getName() + " | WMC " + s.getMetricValues().get(MetricUid.WMC) +
				" " + " | SLOC " + s.getMetricValues().get(MetricUid.SLOC));

		for(SoftwareUnit s2 : s.getChildren()){
			
			if(s2.getType() == SoftwareUnitType.METHOD){
				System.out.println("Método " + s2.getName() + " | CC " + s2.getMetricValues().get(MetricUid.CC));
			}
			
		}

	}

}
