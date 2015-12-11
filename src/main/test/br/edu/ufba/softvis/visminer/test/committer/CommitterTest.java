package br.edu.ufba.softvis.visminer.test.committer;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.edu.ufba.softvis.visminer.model.business.Committer;
import br.edu.ufba.softvis.visminer.model.business.Repository;
import br.edu.ufba.softvis.visminer.test.TestCase;
import br.edu.ufba.softvis.visminer.test.VisminerTest;
import junitparams.FileParameters;
import junitparams.JUnitParamsRunner;
import junitparams.naming.TestCaseName;

@RunWith(JUnitParamsRunner.class)
public class CommitterTest {

  private static VisminerTest visminerTest;
  private static Repository repository;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    visminerTest = VisminerTest.getInstance();
    repository = visminerTest.getRepository(); 
  }

  @Test
  @FileParameters(value=TestCase.LIST_OF_COMMITTERS_TEST)
  @TestCaseName("Tests if committer are get correct from repository"
      + " according first commit ascending order.")
  public void testListOfCommitters(String email, int order){
    assertEquals(email, repository.getCommitters().get(order-1).getEmail());
  }

  @Test
  @FileParameters(value=TestCase.FIRST_LAST_COMMITTER_TEST)
  @TestCaseName("Tests if committer with name {2} and email {1} is the "
      + "author of the {0} commit.")
  public void testFirstAndLastCommitter(String order, String email, 
      String name){

    if(order.equals("first")){
      repository.getProject().firstCommit();
    }else{
      repository.getProject().lastCommit();
    }

    Committer committer = repository.getProject().getCurrentCommit()
        .getCommitter();

    assertEquals(email, committer.getEmail(), "teste");
    assertEquals(name, committer.getName());

  }

}