package br.edu.ufba.softvis.visminer.test.metric;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.edu.ufba.softvis.visminer.constant.MetricUid;
import br.edu.ufba.softvis.visminer.constant.SoftwareUnitType;
import br.edu.ufba.softvis.visminer.model.business.Project;
import br.edu.ufba.softvis.visminer.model.business.Repository;
import br.edu.ufba.softvis.visminer.model.business.SoftwareUnit;
import br.edu.ufba.softvis.visminer.test.TestCase;
import br.edu.ufba.softvis.visminer.test.VisminerTest;
import junitparams.FileParameters;
import junitparams.JUnitParamsRunner;
import junitparams.naming.TestCaseName;

@RunWith(JUnitParamsRunner.class)
public class TCCMetricTest {

  private static VisminerTest visminerTest;
  private static Repository repository;
  private static Map<String, SoftwareUnit> classes;

  private static void initClassesList(List<SoftwareUnit> list){

    for(SoftwareUnit su : list){
      if(su.getType() == SoftwareUnitType.CLASS_OR_INTERFACE){
        classes.put(su.getName(), su);
      }else if(su.getType() == SoftwareUnitType.PACKAGE){
        initClassesList(su.getChildren());
      }
    }

  }

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {

    visminerTest = VisminerTest.getInstance();
    repository = visminerTest.getRepository();

    Project project = repository.getProject();
    SoftwareUnit javaProject = project.getsoftwareUnit();
    classes = new HashMap<String, SoftwareUnit>();
    initClassesList(javaProject.getChildren());

    for(String a : classes.keySet()){
      System.out.println(a);
    }

  }

  @Test
  @FileParameters(value=TestCase.TCC_CLASSES_TEST)
  @TestCaseName("Tests TCC metric of element {0}.")
  public void testTCCClasses(String name, double expectedValue){

    String str = classes.get(name).getMetricValues().get(MetricUid.TCC);
    double actualValue = Double.parseDouble(str);
    assertEquals(actualValue, expectedValue, 3);

  }

}