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
}
