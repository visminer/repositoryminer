package br.edu.ufba.softvis.visminer.test.metric;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import br.edu.ufba.softvis.visminer.constant.MetricUid;
import br.edu.ufba.softvis.visminer.constant.SoftwareUnitType;
import br.edu.ufba.softvis.visminer.model.business.Project;
import br.edu.ufba.softvis.visminer.model.business.Repository;
import br.edu.ufba.softvis.visminer.model.business.SoftwareUnit;
import br.edu.ufba.softvis.visminer.test.VisminerTest;

public class NOCMetricTest {

	private static VisminerTest visminerTest;
	private static Repository repository;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		visminerTest = VisminerTest.getInstance();
		visminerTest.getVisminer();
		repository = visminerTest.getRepository(); 
		
		}
	
	@Test
	public void testTotalNOCAIFirstCommit() {			
			Project project = repository.getProject();
			SoftwareUnit softwareUnit = project.getsoftwareUnit();
			project.firstCommit(); 	
			project.nextCommit();
			softwareUnit = project.getsoftwareUnit();
			
			int valueMetricFirst = 0;
			
			try {
				valueMetricFirst = Integer.parseInt(softwareUnit.getMetricValues().get(MetricUid.NOCAI));
			}catch(NumberFormatException e) {}
			
			assertEquals(29, valueMetricFirst);			
	}
	
	
	@Test
	public void testTotalNOCAILastCommit() {			
		Project project = repository.getProject();
		SoftwareUnit softwareUnit = project.getsoftwareUnit();
		project.lastCommit();
		softwareUnit = project.getsoftwareUnit();
		
		int valueMetricLast = 0;
		
		try {
			valueMetricLast = Integer.parseInt(softwareUnit.getMetricValues().get(MetricUid.NOCAI));
		}catch(NumberFormatException e) {}
		
		assertEquals(108, valueMetricLast);			
	}

	@Test
	public void testNOCAIOtherCommits(){
		Project project = repository.getProject();
		SoftwareUnit softwareUnit = project.getsoftwareUnit();
		project.firstCommit();	
		for(int i=0; i <=13 ; i++){
			project.nextCommit();			
		}
	
		softwareUnit = project.getsoftwareUnit();
	
		int value = 0;
	
		try {
			value= Integer.parseInt(softwareUnit.getMetricValues().get(MetricUid.NOCAI));
		}catch(NumberFormatException e) {}
		
		assertEquals(36, value);		
	}
	
	@Test
	public void testNOCAIByPackageSecondCommit(){
		
		Project project = repository.getProject();
		SoftwareUnit javaProject = project.getsoftwareUnit();
		project.firstCommit();
		project.nextCommit();
		
		javaProject = project.getsoftwareUnit();		
		
		for (SoftwareUnit softwareUnit : javaProject.getChildren()){
			
			if (softwareUnit.getType() == SoftwareUnitType.PACKAGE) {
				
				if ("org.visminer.metric".equals(softwareUnit.getName())) {
					assertEquals("5", softwareUnit.getMetricValues().get(MetricUid.NOCAI));
				}
				if ("org.visminer.persistence".equals(softwareUnit.getName())) {
					assertEquals("10", softwareUnit.getMetricValues().get(MetricUid.NOCAI));
				}
				if ("org.visminer.model".equals(softwareUnit.getName())) {
					assertEquals("9", softwareUnit.getMetricValues().get(MetricUid.NOCAI));
				}
				if ("org.visminer.util".equals(softwareUnit.getName())) {
					assertEquals("3", softwareUnit.getMetricValues().get(MetricUid.NOCAI));
				}
				if ("org.visminer.constants".equals(softwareUnit.getName())) {
					assertEquals("0", softwareUnit.getMetricValues().get(MetricUid.NOCAI));
				}					
			}
		}
	}
	
	@Test
	public void testNOCAIByPackageLastCommit(){
		
		Project project = repository.getProject();
		SoftwareUnit javaProject = project.getsoftwareUnit();
		project.lastCommit();
		
		javaProject = project.getsoftwareUnit();		
		
		for (SoftwareUnit softwareUnit : javaProject.getChildren()){
			
			if (softwareUnit.getType() == SoftwareUnitType.PACKAGE) {
				
				if ("br.edu.ufba.softvis.visminer.constant".equals(softwareUnit.getName())) {
					assertEquals("0", softwareUnit.getMetricValues().get(MetricUid.NOCAI));
				}
				if ("br.edu.ufba.softvis.visminer.persistence.dao".equals(softwareUnit.getName())) {
					assertEquals("13", softwareUnit.getMetricValues().get(MetricUid.NOCAI));
				}
				if ("br.edu.ufba.softvis.visminer.persistence.impl".equals(softwareUnit.getName())) {
					assertEquals("13", softwareUnit.getMetricValues().get(MetricUid.NOCAI));
				}
				if ("br.edu.ufba.softvis.visminer.utility".equals(softwareUnit.getName())) {
					assertEquals("3", softwareUnit.getMetricValues().get(MetricUid.NOCAI));
				}
				if ("br.edu.ufba.softvis.visminer.annotations".equals(softwareUnit.getName())) {
					assertEquals("0", softwareUnit.getMetricValues().get(MetricUid.NOCAI));
				}
				if ("br.edu.ufba.softvis.visminer.config".equals(softwareUnit.getName())) {
					assertEquals("2", softwareUnit.getMetricValues().get(MetricUid.NOCAI));
				}
				if ("br.edu.ufba.softvis.visminer.analyzer.remote".equals(softwareUnit.getName())) {
					assertEquals("2", softwareUnit.getMetricValues().get(MetricUid.NOCAI));
				}
				if ("br.edu.ufba.softvis.visminer.analyzer.local".equals(softwareUnit.getName())) {
					assertEquals("3", softwareUnit.getMetricValues().get(MetricUid.NOCAI));
				}
				if ("br.edu.ufba.softvis.visminer.persistence".equals(softwareUnit.getName())) {
					assertEquals("3", softwareUnit.getMetricValues().get(MetricUid.NOCAI));
				}
				if ("br.edu.ufba.softvis.visminer.model.database".equals(softwareUnit.getName())) {
					assertEquals("15", softwareUnit.getMetricValues().get(MetricUid.NOCAI));
				}
				if ("br.edu.ufba.softvis.visminer.analyzer".equals(softwareUnit.getName())) {
					assertEquals("7", softwareUnit.getMetricValues().get(MetricUid.NOCAI));
				}
				if ("br.edu.ufba.softvis.visminer.model.business".equals(softwareUnit.getName())) {
					assertEquals("9", softwareUnit.getMetricValues().get(MetricUid.NOCAI));
				}
				if ("br.edu.ufba.softvis.visminer.metric".equals(softwareUnit.getName())) {
					assertEquals("6", softwareUnit.getMetricValues().get(MetricUid.NOCAI));
				}
				if ("br.edu.ufba.softvis.visminer.main".equals(softwareUnit.getName())) {
					assertEquals("1", softwareUnit.getMetricValues().get(MetricUid.NOCAI));
				}
				if ("br.edu.ufba.softvis.visminer.ast.generator".equals(softwareUnit.getName())) {
					assertEquals("2", softwareUnit.getMetricValues().get(MetricUid.NOCAI));
				}
				if ("br.edu.ufba.softvis.visminer.ast".equals(softwareUnit.getName())) {
					assertEquals("13", softwareUnit.getMetricValues().get(MetricUid.NOCAI));
				}
				if ("br.edu.ufba.softvis.visminer.ast.generator.java".equals(softwareUnit.getName())) {
					assertEquals("2", softwareUnit.getMetricValues().get(MetricUid.NOCAI));
				}
				if ("br.edu.ufba.softvis.visminer.ast.generator.cpp".equals(softwareUnit.getName())) {
					assertEquals("3", softwareUnit.getMetricValues().get(MetricUid.NOCAI));
				}
				if ("br.edu.ufba.softvis.visminer.retriever".equals(softwareUnit.getName())) {
					assertEquals("6", softwareUnit.getMetricValues().get(MetricUid.NOCAI));
				}
				if ("br.edu.ufba.softvis.visminer.ast.generator.none".equals(softwareUnit.getName())) {
					assertEquals("1", softwareUnit.getMetricValues().get(MetricUid.NOCAI));
				}
				if ("br.edu.ufba.softvis.visminer.example".equals(softwareUnit.getName())) {
					assertEquals("4", softwareUnit.getMetricValues().get(MetricUid.NOCAI));
				}
			}
		}
	}
		
}
