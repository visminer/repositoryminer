package br.edu.ufba.softvis.visminer.test.commit;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import br.edu.ufba.softvis.visminer.main.VisMiner;
import br.edu.ufba.softvis.visminer.model.business.Commit;
import br.edu.ufba.softvis.visminer.model.business.Repository;
import br.edu.ufba.softvis.visminer.test.VisminerTest;

public class CommitTest {
	
	private static VisminerTest visminerTest;
	private static VisMiner visminer;
	private static Repository repository;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		visminerTest = VisminerTest.getInstance();
		visminer = visminerTest.getVisminer();
		repository = visminerTest.getRepository(); 
		
	}
	
	@Test
	public void testSizeOfWholeListOfCommits() {
		List<Commit> commits = repository.getProject().getCommits();
		assertEquals(103, commits.size());
	}

	@Test
	public void testFirstCommit() {
		repository.getProject().firstCommit();
		Commit commit = repository.getProject().getCurrentCommit();
		assertEquals(1, commit.getId());
	}
	
	@Test
	public void testLastCommit() {
		repository.getProject().lastCommit();
		Commit commit = repository.getProject().getCurrentCommit();
		assertEquals(17, commit.getId());
	}
	
	@Test 
	public void testDateLastCommitDifferentDateFirstCommit() {
		repository.getProject().firstCommit();
		Commit firstCommit = repository.getProject().getCurrentCommit();
		
		repository.getProject().lastCommit();
		Commit lastCommit = repository.getProject().getCurrentCommit();
		
		assertNotEquals(firstCommit.getDate(), lastCommit.getDate());
	}
	
	@Test
	public void testSecondCommitMoreFileCommitedFirstCommit() {
		
		Commit firstcommit = null;
		Commit secondCommit = null;
		
		repository.getProject().firstCommit();
		firstcommit = repository.getProject().getCurrentCommit();
		
		repository.getProject().nextCommit();
		secondCommit = repository.getProject().getCurrentCommit();
		
		assertTrue(secondCommit.getCommitedFiles().size() > firstcommit.getCommitedFiles().size());
	}

	@Test
	public void testTotalOfCommits(){
		repository.getProject().lastCommit();
		
		List<Commit> commits = repository.getProject().getCommits();
		assertEquals(103, commits.size());
	}
	
	@Test
	public void testDateOfFirstCommit() {		
		repository.getProject().firstCommit(); 
		Commit firstCommit = repository.getProject().getCurrentCommit();
		
		assertEquals("Wed Apr 09 11:55:44 BRT 2014",firstCommit.getDate().toString());
	}
	
	
	@Test
	public void testDateOfLastCommit(){		
		repository.getProject().lastCommit(); 
		Commit lastCommit = repository.getProject().getCurrentCommit();
		
		assertEquals("Mon Aug 17 20:45:57 BRT 2015",lastCommit.getDate().toString());
	}	
	
	@Test
	public void testNameFirstCommit(){		
		repository.getProject().firstCommit(); 
		Commit firstCommit = repository.getProject().getCurrentCommit();
		
		assertEquals("97bfbe9870f7df252a354bcfa54fb2abd3c4a59e",firstCommit.getName());
	}
		
	@Test
	public void testNameLastCommit(){		
		repository.getProject().lastCommit(); 
		Commit lastCommit = repository.getProject().getCurrentCommit();
				
		assertEquals("2a7b33c7e40f2b10e5a43081c7f608cf454e824f",lastCommit.getName());
	}	
	
	@Test
	public void testCommitterInformationOfTheFirstCommit(){
		repository.getProject().firstCommit(); 
		Commit firstCommit = repository.getProject().getCurrentCommit();
		
		assertEquals("visminer",firstCommit.getCommitter().getName());
		assertEquals(Integer.parseInt("1"),firstCommit.getCommitter().getId());
		assertEquals("visminerproject@gmail.com",firstCommit.getCommitter().getEmail());
	}
	
	@Test
	public void testCommitterInformationOfTheLastCommit(){
		repository.getProject().lastCommit(); 
		Commit lastCommit = repository.getProject().getCurrentCommit();
		
		assertEquals("felipe gustavo de souza gomes",lastCommit.getCommitter().getName());
		assertEquals(Integer.parseInt("4"),lastCommit.getCommitter().getId());
		assertEquals("felipegustavo1000@gmail.com",lastCommit.getCommitter().getEmail());
	}
	
	@Test
	public void testCommits(){
		repository.getProject().firstCommit(); 
		for(int i =0;i <=21;i++){
			repository.getProject().nextCommit();
		}
		Commit currentCommit = repository.getProject().getCurrentCommit();
		
		assertEquals("87c7f90d37730281f79bd4558edb974b4b10cefb", currentCommit.getName());
		assertEquals("Renato Novais",currentCommit.getCommitter().getName());
		assertEquals(Integer.parseInt("3"),currentCommit.getCommitter().getId());
		assertEquals("renatoln@yahoo.com.br",currentCommit.getCommitter().getEmail());
	}

}
