package br.edu.ufba.softvis.visminer.test.metric;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import br.edu.ufba.softvis.visminer.constant.MetricUid;
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
		
		for(SoftwareUnit s : javaProject.getChildren()){ 

			System.out.println(s.getName());
			
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
	
}
