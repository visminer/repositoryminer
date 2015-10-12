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
	public void testTotalLoc() {
	
		Project project = repository.getProject();
		SoftwareUnit javaProject = project.getsoftwareUnit();
		
		assertEquals(10321, javaProject.getMetricValues().get(MetricUid.SLOC));
	}
	
	@Test 
	public void testLocByPackage() {
		
		Project project = repository.getProject();
		SoftwareUnit javaProject = project.getsoftwareUnit();
		int valueMetric = 0;

		for (SoftwareUnit softwareUnit : javaProject.getChildren()){ 

			if (softwareUnit.getType() == SoftwareUnitType.PACKAGE) {
				
				try {
					valueMetric = Integer.parseInt(softwareUnit.getMetricValues().get(MetricUid.SLOC));
					
				} catch (NumberFormatException e) {
					//for the case where the metric is null FIX it later
				}
				
				if ("br.edu.ufba.softvis.visminer.persistence.dao".equals(softwareUnit.getName()))
					assertEquals(354, valueMetric);
				
				if ("br.edu.ufba.softvis.visminer.persistence.impl".equals(softwareUnit.getName()))
					assertEquals(531, valueMetric);
					
				if ("br.edu.ufba.softvis.visminer.annotations".equals(softwareUnit.getName()))
					assertEquals(68, valueMetric);
			
				if ("br.edu.ufba.softvis.visminer.analyzer.local".equals(softwareUnit.getName())) 
					assertEquals(478, valueMetric);
				
				if ("br.edu.ufba.softvis.visminer.persistence".equals(softwareUnit.getName()))
					assertEquals(1253, valueMetric);
				
				if ("br.edu.ufba.softvis.visminer.constant".equals(softwareUnit.getName()))
					assertEquals(278, valueMetric);
				
				if ("br.edu.ufba.softvis.visminer.analyzer.remote".equals(softwareUnit.getName()))
					assertEquals(21, valueMetric);
				
				if ("br.edu.ufba.softvis.visminer.utility".equals(softwareUnit.getName()))
					assertEquals(111, valueMetric);
				
				if ("br.edu.ufba.softvis.visminer.config".equals(softwareUnit.getName()))
					assertEquals(256, valueMetric);
				
				if ("br.edu.ufba.softvis.visminer.model.database".equals(softwareUnit.getName()))
					assertEquals(2947, valueMetric);
				
				if ("br.edu.ufba.softvis.visminer.main".equals(softwareUnit.getName()))
					assertEquals(102, valueMetric);
				
				if ("br.edu.ufba.softvis.visminer.analyzer".equals(softwareUnit.getName()))
					assertEquals(1058, valueMetric);
				
				if ("br.edu.ufba.softvis.visminer.metric".equals(softwareUnit.getName()))
					assertEquals(299, valueMetric);
				
				if ("br.edu.ufba.softvis.visminer.model.business".equals(softwareUnit.getName()))
					assertEquals(1593, valueMetric);
				
				if ("br.edu.ufba.softvis.visminer.ast".equals(softwareUnit.getName()))
					assertEquals(1691, valueMetric);
				
				if ("br.edu.ufba.softvis.visminer.ast.generator".equals(softwareUnit.getName()))
					assertEquals(1019, valueMetric);
				
				if ("br.edu.ufba.softvis.visminer.ast.generator.java".equals(softwareUnit.getName()))
					assertEquals(359, valueMetric);
				
				if ("br.edu.ufba.softvis.visminer.retriever".equals(softwareUnit.getName()))
					assertEquals(400, valueMetric);
				
				if ("br.edu.ufba.softvis.visminer.ast.generator.cpp".equals(softwareUnit.getName()))
					assertEquals(576, valueMetric);
				
				if ("br.edu.ufba.softvis.visminer.ast.generator.none".equals(softwareUnit.getName()))
					assertEquals(51, valueMetric);
				
				if ("br.edu.ufba.softvis.visminer.example".equals(softwareUnit.getName()))
					assertEquals(265, valueMetric);
				
			}
		}
		
	}
	
	@Test
	public void testLocOfClassOrInterface() {
	
		Project project = repository.getProject();
		SoftwareUnit javaProject = project.getsoftwareUnit();
		
		for (SoftwareUnit softwareUnit : javaProject.getChildren()) {
			
			if (softwareUnit.getType() == SoftwareUnitType.PACKAGE) {
				if ("br.edu.ufba.softvis.visminer.metric".equals(softwareUnit.getName()) ||
						"br.edu.ufba.softvis.visminer.model.business".equals(softwareUnit.getName())) {
					
					testLocOfClassOrInterface(softwareUnit);
				}
			}	
		}
		
	}
	
	private void testLocOfClassOrInterface(SoftwareUnit softwareUnit) {
		
		int valueMetric = 0;
		
		for (SoftwareUnit s : softwareUnit.getChildren()) {
		
			try {	
				valueMetric = Integer.parseInt(s.getMetricValues().get(MetricUid.SLOC));
			} catch(NumberFormatException e) {}
			
			if ("src/main/java/br/edu/ufba/softvis/visminer/metric/SLOCMetric.java".equals(s.getName()))
				assertEquals(54, valueMetric);
			
			if ("src/main/java/br/edu/ufba/softvis/visminer/metric/IMetric.java".equals(s.getName()))
				assertEquals(16, valueMetric);
			
			if ("src/main/java/br/edu/ufba/softvis/visminer/metric/CCMetric.java".equals(s.getName()))
				assertEquals(78, valueMetric);
			
			if ("src/main/java/br/edu/ufba/softvis/visminer/metric/WMCMetric.java".equals(s.getName()))
				assertEquals(37, valueMetric);
			
			if ("src/main/java/br/edu/ufba/softvis/visminer/metric/MethodBasedMetricTemplate.java".equals(s.getName()))
				assertEquals(54, valueMetric);
			
			if ("src/main/java/br/edu/ufba/softvis/visminer/metric/NOCAIMetric.java".equals(s.getName()))
				assertEquals(60, valueMetric);
			
			if ("src/main/java/br/edu/ufba/softvis/visminer/model/business/Repository.java".equals(s.getName()))
				assertEquals(249, valueMetric);
			
			if ("src/main/java/br/edu/ufba/softvis/visminer/model/business/FileState.java".equals(s.getName()))
				assertEquals(72, valueMetric);
			
			if ("src/main/java/br/edu/ufba/softvis/visminer/model/business/Project.java".equals(s.getName()))
				assertEquals(399, valueMetric);
			
			if ("src/main/java/br/edu/ufba/softvis/visminer/model/business/SoftwareUnit.java".equals(s.getName()))
				assertEquals(183, valueMetric);
			
			if ("src/main/java/br/edu/ufba/softvis/visminer/model/business/Commit.java".equals(s.getName()))
				assertEquals(148, valueMetric);
			
			if ("src/main/java/br/edu/ufba/softvis/visminer/model/business/File.java".equals(s.getName()))
				assertEquals(127, valueMetric);
			
			if ("src/main/java/br/edu/ufba/softvis/visminer/model/business/Tree.java".equals(s.getName()))
				assertEquals(138, valueMetric);
			
			if ("src/main/java/br/edu/ufba/softvis/visminer/model/business/Committer.java".equals(s.getName()))
				assertEquals(115, valueMetric);
			
			if ("src/main/java/br/edu/ufba/softvis/visminer/model/business/Metric.java".equals(s.getName()))
				assertEquals(162, valueMetric);
			
		}
	}
}
