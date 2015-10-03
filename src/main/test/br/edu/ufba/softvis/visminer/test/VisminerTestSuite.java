package br.edu.ufba.softvis.visminer.test;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.edu.ufba.softvis.visminer.test.commit.CommitTest;
import br.edu.ufba.softvis.visminer.test.committer.CommitterTest;
import br.edu.ufba.softvis.visminer.test.metric.CCMetricTest;
import br.edu.ufba.softvis.visminer.test.metric.NOCMetricTest;
import br.edu.ufba.softvis.visminer.test.metric.SLOCMetricTest;
import br.edu.ufba.softvis.visminer.test.metric.TCCMetricTest;
import br.edu.ufba.softvis.visminer.test.metric.WMCMetricTest;

@RunWith(Suite.class)
@SuiteClasses({ CommitTest.class, CommitterTest.class, CCMetricTest.class,
	NOCMetricTest.class, SLOCMetricTest.class, TCCMetricTest.class, WMCMetricTest.class})
public class VisminerTestSuite {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		VisminerTest.getInstance(); //just to make sure that the repository was started before the tests start
		
	}
	





	
}
